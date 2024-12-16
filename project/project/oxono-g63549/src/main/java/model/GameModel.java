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


    public GameModel() {
        this.player1 = new Player(Color.PINK);
        this.player2 = new Player(Color.BLACK, new RandomMoveStrategy());
        this.currentPlayer = player1;
        this.totemX = new Totem(Symbol.X);
        this.totemO = new Totem(Symbol.O);
        this.board = new Board();
        this.gameOver = false;

        board.putPiece(new Position(0, 0), totemX);
        board.putPiece(new Position(5, 5), totemO);
    }

    /**
     * Starts a new game by resetting the board, players, and game state.
     * Starts a new game by resetting the board, players, and game state.
     * Redémarre une partie en réinitialisant le plateau, les joueurs, et l'état du jeu.
     */
    public void startGame() {
        gameOver = false;
        board.reset();

        totemX = new Totem(Symbol.X);
        totemO = new Totem(Symbol.O);
        board.putPiece(new Position(2, 2), totemX);
        board.putPiece(new Position(3, 3), totemO);

        player1 = new Player(Color.PINK);
        player2 = new Player(Color.BLACK, new RandomMoveStrategy());

        currentPlayer = player1;

        System.out.println("Game started!");
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
            checkGameOver();
            LastMovedSymbol = symbol;
            notifyObservers("TotemMoved", target);

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
            System.out.println("GAME OVER");
            return false;

        }


        Totem totem = (symbol == Symbol.X) ? totemX : totemO;
        Position totemPos = board.findTotemPosition(totem);

        if (!GameRules.isValidTokenPlacement(board, totemPos, position)) {
            return false;
        }

        board.putPiece(position, new Token(currentPlayer.getColor(), symbol));

        int previousRemainingX = currentPlayer.getBag().countSymbol(Symbol.X);
        int previousRemainingO = currentPlayer.getBag().countSymbol(Symbol.O);

        notifyObservers("TokenPlaced", position);

        int remainingX = currentPlayer.getBag().countSymbol(Symbol.X);
        int remainingO = currentPlayer.getBag().countSymbol(Symbol.O);

        if (remainingX != previousRemainingX || remainingO != previousRemainingO) {
            notifyObservers("TokenCountChanged", "X: " + remainingX + ", O: " + remainingO);
        }

        endTurn();
        checkGameOver();
        System.out.println(gameOver + "CACACACACACAC");
        notifyObservers("TokenCountChanged", "X: " + remainingX + ", O: " + remainingO);

        return true;
    }


    /**
     * Ends the turn of the current player and switches to the other.
     * Met fin au tour du joueur actuel et passe à l’autre.
     */
    public void endTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        System.out.println("Next turn: " + currentPlayer.getColor());
        notifyObservers("Next turn :", currentPlayer.getColor());
    }

    /**
     * Checks for the game over conditions and sets gameOver to true if necessary.
     */
    private void checkGameOver() {
        if (checkWin()) {
            gameOver = true;
            System.out.println("Game over! The winner is: " + getWinner().getColor());
        } else if (!isMovePossible()) {
            gameOver = true;
            System.out.println("Game over! It's a draw, no moves left.");
        } else {
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

                if (checkDirection(pos, 1, 0, piece) ||
                        checkDirection(pos, 0, 1, piece)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(Position start, int dx, int dy, Piece piece) {
        int count = 0;

        for (int step = 0; step < 4; step++) {
            int x = start.x() + step * dx;
            int y = start.y() + step * dy;
            Position pos = new Position(x, y);

            if (!board.isValidCoordinate(pos) || !piece.equals(board.getPiece(pos))) {
                return false;
            }

            count++;
        }

        return count == 4;
    }

    /**
     * Returns the winner player if there is one.
     * Retourne le joueur gagnant s’il y en a un.
     */
    public Player getWinner() {
        if (checkWin()) {
            return currentPlayer;
        }
        return null;
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

        System.out.println(getCurrentPhase() + "CACACACACAACACACA");
        advancePhase();
        System.out.println(getCurrentPhase() + "CACACACACAACACACA");
        boolean totemMoved = moveTotem(symbol, totemTarget);
        notifyObservers("ToTemMoved", tokenTarget);

        if (totemMoved) {
            System.out.println("Automatic player moved the " + symbol + " totem from " + currentTotemPos + " to " + totemTarget);
            advancePhase();
        } else {
            System.out.println("Automatic player attempted to move the totem, but failed.");
        }


        if (tokenTarget != null) {
            boolean tokenPlaced = placeToken(tokenTarget, symbol);
            System.out.println(tokenPlaced);
            notifyObservers("TokenPlaced", tokenTarget);

            if (tokenPlaced) {
                System.out.println("Automatic player placed a " + symbol + " token at " + tokenTarget);
                endTurn();
                notifyObservers("TokenPlaced", tokenTarget);


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

        Player winner = (currentPlayer == player1) ? player2 : player1;
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


}