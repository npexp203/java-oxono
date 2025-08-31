package model;

import Util.Observable;
import Util.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the main game logic for Oxono.
 * Manages players, the game board, and the rules.
 */

public class GameModel implements Observable {

    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Totem totemX;
    private Totem totemO;
    private Board board;
    private boolean gameOver;
    private List<Observer> observers = new ArrayList<>();
    private Symbol LastMovedSymbol = null;
    private TurnPhase currentPhase = TurnPhase.MOVE_TOTEM;
    private Player winner = null;



    public GameModel() {
        this.player1 = new Player(Color.PINK);
        this.player2 = new Player(Color.BLACK, new RandomMoveStrategy());
        this.currentPlayer = player1;
        this.totemX = new Totem(Symbol.X);
        this.totemO = new Totem(Symbol.O);
        this.board = new Board();
        this.gameOver = false;

        board.putPiece(new Position(2, 2), totemX);
        board.putPiece(new Position(3, 3), totemO);
    }

    /**
     * Starts a new game by resetting the board, players, and game state.
     * Starts a new game by resetting the board, players, and game state.
     * Redémarre une partie en réinitialisant le plateau, les joueurs, et l'état du jeu.
     */
    public void startGame() {
        gameOver = false;
        winner = null; // Remettre le gagnant à null

        board.reset();

        totemX = new Totem(Symbol.X);
        totemO = new Totem(Symbol.O);
        board.putPiece(new Position(2, 2), totemX);
        board.putPiece(new Position(3, 3), totemO);

        player1 = new Player(Color.PINK);
        player2 = new Player(Color.BLACK, new RandomMoveStrategy());

        currentPlayer = player1;
        currentPhase = TurnPhase.MOVE_TOTEM; // S'assurer que la phase est correcte


        System.out.println("Game started!");
        notifyObservers("CurrentPlayerChanged", currentPlayer);
        notifyObservers("PhaseChanged", currentPhase);
        notifyObservers("GameStarted", null);
    }

    public int getRemainingTokens(Symbol symbol) {
        return currentPlayer.countSymbol(symbol);
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public Totem getTotemX() {
        return totemX;
    }

    public Totem getTotemO() {
        return totemO;
    }

    /**
     * Moves a totem to a given position, checking the rules via GameRules.
     * Déplace un totem vers une position donnée, en vérifiant les règles via GameRules.
     */
    public boolean moveTotem(Symbol symbol, Position target) {
        if (gameOver) {
            throw new IllegalStateException("The game is over.");
        }
        if (currentPhase != TurnPhase.MOVE_TOTEM) {
            return false;
        }

        boolean moved = GameRules.moveTotem(board, (symbol == Symbol.X ? totemX : totemO), target);
        if (moved) {
            System.out.println("on rentre la?");
            currentPhase = TurnPhase.PLACE_TOKEN; // Avancer à la phase suivante
            LastMovedSymbol = symbol;
            notifyObservers("TotemMoved", target);
            checkGameOver();
        }
        return moved;
    }

    public Symbol getLastMovedSymbol() {
        return LastMovedSymbol;
    }

    /**
     * Places a token adjacent to the totem corresponding to the given symbol,
     * if the rules allow it (via GameRules).
     * Place un token adjacent au totem correspondant au symbole donné,
     * si les règles le permettent (via GameRules).
     */
    public boolean placeToken(Position position, Symbol symbol) {
        if (gameOver || currentPhase != TurnPhase.PLACE_TOKEN) {
            return false;
        }

        // Vérifier que le joueur a encore des pièces de ce symbole
        if (currentPlayer.getBag().countSymbol(symbol) <= 0) {
            return false;
        }

        Totem totem = (symbol == Symbol.X) ? totemX : totemO;
        Position totemPos = board.findTotemPosition(totem);

        if (!GameRules.isValidTokenPlacement(board, totemPos, position)) {
            return false;
        }

        // Retirer la pièce du sac du joueur
        Piece removedPiece = currentPlayer.getBag().removePiece(symbol);
        if (removedPiece == null) {
            return false;
        }

        board.putPiece(position, removedPiece);
        notifyObservers("TokenPlaced", position);

        // Notifier le changement de count
        int remainingX = currentPlayer.getBag().countSymbol(Symbol.X);
        int remainingO = currentPlayer.getBag().countSymbol(Symbol.O);
        notifyObservers("TokenCountChanged", "X: " + remainingX + ", O: " + remainingO);

        checkGameOver();
        if (!gameOver) {
            endTurn();
        }

        return true;
    }


    /**
     * Ends the turn of the current player and switches to the other.
     * Met fin au tour du joueur actuel et passe à l’autre.
     */
    public void endTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        currentPhase = TurnPhase.MOVE_TOTEM; // Remettre la phase pour le nouveau joueur
        System.out.println("Next turn: " + currentPlayer.getColor());
        notifyObservers("CurrentPlayerChanged", currentPlayer);
        notifyObservers("PhaseChanged", currentPhase);
    }

    /**
     * Checks for the game over conditions and sets gameOver to true if necessary.
     */
    private void checkGameOver() {
        System.out.println("DEBUG: Checking game over, current phase: " + currentPhase);
        if (checkWin()) {
            winner = currentPlayer; // Stocker avant endTurn()
            gameOver = true;
            System.out.println("Game over! The winner is: " + getWinner().getColor());
        } else if (!isMovePossible()) {
            gameOver = true;
            System.out.println("Game over! It's a draw, no moves left.");
        } else {
            System.out.println("DEBUG: Game continues, phase should stay: " + currentPhase);

            gameOver = false;
        }
    }

    /**
     * Checks if there is a winning alignment on the board.
     */
    public boolean checkWin() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Position pos = new Position(i, j);
                if (board.isEmpty(pos)) {
                    continue;
                }

                Piece piece = board.getPiece(pos);
                // Les totems ne comptent pas dans les alignements
                if (piece instanceof Totem) {
                    continue;
                }

                // Vérifier alignements horizontal et vertical
                if (checkColorAlignment(pos, 1, 0) ||
                        checkColorAlignment(pos, 0, 1) ||
                        checkSymbolAlignment(pos, 1, 0) ||
                        checkSymbolAlignment(pos, 0, 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkColorAlignment(Position start, int dx, int dy) {
        Piece startPiece = board.getPiece(start);
        if (startPiece instanceof Totem) {
            return false;
        }

        Color targetColor = startPiece.getColor();
        System.out.println("Checking color alignment from " + start + " direction (" + dx + "," + dy + ") for color " + targetColor);

        for (int step = 0; step < 4; step++) {
            int x = start.x() + step * dx;
            int y = start.y() + step * dy;
            Position pos = new Position(x, y);

            if (!board.isValidCoordinate(pos)) {
                System.out.println("  Step " + step + ": Invalid coordinate " + pos);
                return false;
            }

            Piece piece = board.getPiece(pos);
            System.out.println("  Step " + step + ": Position " + pos + " has piece: " + piece);

            if (piece == null) {
                System.out.println("  Step " + step + ": No piece at " + pos);
                return false;
            }

            if (piece instanceof Totem) {
                System.out.println("  Step " + step + ": Totem found at " + pos + ", breaking alignment");
                return false;
            }

            if (piece.getColor() != targetColor) {
                System.out.println("  Step " + step + ": Wrong color at " + pos + " (expected " + targetColor + ", got " + piece.getColor() + ")");
                return false;
            }

            System.out.println("  Step " + step + ": Match found at " + pos + " with color " + piece.getColor());
        }

        System.out.println("COLOR ALIGNMENT FOUND: 4 pieces of " + targetColor + " from " + start + " in direction (" + dx + "," + dy + ")");
        return true;
    }

    private boolean checkSymbolAlignment(Position start, int dx, int dy) {
        Piece startPiece = board.getPiece(start);
        if (startPiece instanceof Totem) {
            return false;
        }

        Symbol targetSymbol = startPiece.getSymbol();

        for (int step = 0; step < 4; step++) {
            int x = start.x() + step * dx;
            int y = start.y() + step * dy;
            Position pos = new Position(x, y);

            if (!board.isValidCoordinate(pos)) {
                return false;
            }

            Piece piece = board.getPiece(pos);
            if (piece == null || piece instanceof Totem || piece.getSymbol() != targetSymbol) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the winner player if there is one.
     * Retourne le joueur gagnant s’il y en a un.
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Checks if there is at least one empty cell left on the board (for a draw).
     * Vérifie s’il reste au moins une case vide sur le plateau (pour détecter une égalité).
     */
    public boolean isMovePossible() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Position pos = new Position(i, j);
                if (board.isEmpty(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Is the current player automated?
     * If yes, it executes an automatic move.
     */
    public void executeAutomaticMove() {
        if (!currentPlayer.isAutomated()) return;

        System.out.println("Automatic player (" + currentPlayer.getColor() + ") is playing...");

        Move move = currentPlayer.getMoveStrategy().calculateMove(this);
        if (move == null) {
            System.out.println("No move returned by strategy. Automatic player does nothing.");
            return;
        }

        Symbol symbol = move.getTokenSymbol();
        Totem totem = (symbol == Symbol.X) ? getTotemX() : getTotemO();
        Position currentTotemPos = board.findTotemPosition(totem);
        Position totemTarget = move.getTotemTarget();
        Position tokenTarget = move.getTokenTarget();

        // Supprimer le advancePhase() d'ici
        boolean totemMoved = moveTotem(symbol, totemTarget);
        notifyObservers("TotemMoved", totemTarget);

        if (totemMoved) {
            System.out.println("Automatic player moved the " + symbol + " totem from " + currentTotemPos + " to " + totemTarget);
            // Le advancePhase() se fait déjà dans moveTotem()
        } else {
            System.out.println("Automatic player attempted to move the totem, but failed.");
            return; // Arrêter si le totem n'a pas bougé
        }

        if (tokenTarget != null) {
            boolean tokenPlaced = placeToken(tokenTarget, symbol);
            notifyObservers("TokenPlaced", tokenTarget);

            if (tokenPlaced) {
                System.out.println("Automatic player placed a " + symbol + " token at " + tokenTarget);
            } else {
                System.out.println("Automatic player attempted to place a token, but failed.");
            }
        } else {
            System.out.println("Automatic player does not place a token.");
        }
    }


    public Position getTotemPosition(Symbol symbol) {
        Totem totem = (symbol == Symbol.X) ? totemX : totemO;
        return board.findTotemPosition(totem);
    }

    public void removeToken(Position position) {
        board.removePiece(position);
    }

    public void forfeitGame() {
        if (gameOver) {
            throw new IllegalStateException("The game is already over.");
        }

        this.winner = (currentPlayer == player1) ? player2 : player1;
        gameOver = true;
        System.out.println("Player " + winner.getColor() + " wins because the opponent forfeited!");
    }


    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    private boolean isNotifying = false;

    @Override
    public void notifyObservers(String event, Object value) {
        if (isNotifying) {
            return; // Bloque les notifications récursives
        }
        isNotifying = true;
        for (Observer obs : observers) {
            obs.update(event, value);
        }
        isNotifying = false;
    }


    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }


    /**
     * Retourne la phase actuelle du tour.
     */
    public TurnPhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * Next phase
     */
    public void advancePhase() {
        if (currentPhase == TurnPhase.MOVE_TOTEM) {
            currentPhase = TurnPhase.PLACE_TOKEN;
        } else {
            currentPhase = TurnPhase.MOVE_TOTEM;
        }
        notifyObservers("PhaseChanged", currentPhase);
    }


    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
    // Dans GameModel.java
    public void restoreTokenToCurrentPlayer(Piece piece) {
        if (piece.getColor() == Color.PINK) {
            player1.getBag().addPiece(piece);
        } else {
            player2.getBag().addPiece(piece);
        }
    }

    public void restoreTokenToPlayer(Piece piece, Color playerColor) {
        if (playerColor == Color.PINK) {
            player1.getBag().addPiece(piece);
        } else {
            player2.getBag().addPiece(piece);
        }
    }

    // Pour l'état du jeu
    public void restoreGameState(Player currentPlayer, TurnPhase phase) {
        this.currentPlayer = currentPlayer;
        this.currentPhase = phase;
        notifyObservers("CurrentPlayerChanged", currentPlayer);
        notifyObservers("PhaseChanged", phase);
    }
    // Dans GameModel
    public boolean canSelectTotem(Position position) {
        if (currentPhase != TurnPhase.MOVE_TOTEM) return false;
        Piece piece = board.getPiece(position);
        return piece instanceof Totem;
    }

//    public SelectionResult selectTotem(Position position) {
//        // Validation + logique + notifications
//        if (!canSelectTotem(position)) {
//            return SelectionResult.INVALID;
//        }
//        // ... logique
//        notifyObservers("TotemSelected", position);
//        return SelectionResult.SUCCESS;
//    }


}