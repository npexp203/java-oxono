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
     * Redémarre une partie en réinitialisant le plateau, les joueurs, et l'état du jeu.
     */
    public void startGame() {
        gameOver = false;
        board.reset();

        totemX = new Totem(Symbol.X);
        totemO = new Totem(Symbol.O);
        board.putPiece(new Position(0, 0), totemX);
        board.putPiece(new Position(5, 5), totemO);

        player1 = new Player(Color.PINK);
        player2 = new Player(Color.BLACK, new RandomMoveStrategy());

        currentPlayer = player1;

        System.out.println("Game started!");
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
     * Déplace un totem vers une position donnée, en vérifiant les règles via GameRules.
     */
    public boolean moveTotem(Symbol symbol, Position target) {
        if (gameOver) {
            throw new IllegalStateException("The game is over.");
        }
        if (currentPhase != TurnPhase.MOVE_TOTEM) {
            // Pas la bonne phase
            return false;
        }

        boolean moved = GameRules.moveTotem(board, (symbol == Symbol.X ? totemX : totemO), target);
        if (moved) {
            checkGameOver();
            notifyObservers("TotemMoved", target);
            currentPhase = TurnPhase.PLACE_TOKEN; // On passe à la phase de placement
        }
        return moved;
    }

    /**
     * Place un token adjacent au totem correspondant au symbole donné,
     * si les règles le permettent (via GameRules).
     */
    public boolean placeToken(Position position, Symbol symbol) {
        if (gameOver || currentPhase != TurnPhase.PLACE_TOKEN) {
            return false;
        }

        Totem totem = (symbol == Symbol.X) ? totemX : totemO;
        Position totemPos = board.findTotemPosition(totem);

        if (!GameRules.isValidTokenPlacement(board, totemPos, position)) {
            return false;
        }

        board.putPiece(position, new Token(currentPlayer.getColor(), symbol));
        checkGameOver();
        notifyObservers("TokenPlaced", position);

        // Mise à jour des tokens restants
        int remainingX = currentPlayer.getBag().countSymbol(Symbol.X);
        int remainingO = currentPlayer.getBag().countSymbol(Symbol.O);
        notifyObservers("TokenCountChanged", "X: " + remainingX + ", O: " + remainingO);

        endTurn();
        return true;
    }



    /**
     * Met fin au tour du joueur actuel et passe à l’autre.
     */
    public void endTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        System.out.println("Next turn: " + currentPlayer.getColor());
        notifyObservers("Next turn :", currentPlayer.getColor());
    }

    /**
     * Vérifie les conditions de victoire ou d’égalité et met gameOver à true le cas échéant.
     */
    private void checkGameOver() {
        if (checkWin()) {
            gameOver = true;
            System.out.println("Game over! The winner is: " + getWinner().getColor());
        } else if (!isMovePossible()) {
            gameOver = true;
            System.out.println("Game over! It's a draw, no moves left.");
        }
    }

    /**
     * Vérifie s’il y a un alignement gagnant sur le plateau.
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
                        checkDirection(pos, 0, 1, piece) ||
                        checkDirection(pos, 1, 1, piece) ||
                        checkDirection(pos, 1, -1, piece)) {
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
     * Retourne le joueur gagnant s’il y en a un.
     */
    public Player getWinner() {
        if (checkWin()) {
            return currentPlayer;
        }
        return null;
    }

    /**
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
     * Le joueur actuel est-il automatisé ?
     * Si oui, on exécute un coup automatique.
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

        if (!currentTotemPos.equals(totemTarget)) {
            boolean totemMoved = moveTotem(symbol, totemTarget);
            if (totemMoved) {
                System.out.println("Automatic player moved the " + symbol + " totem from " + currentTotemPos + " to " + totemTarget);
            } else {
                System.out.println("Automatic player attempted to move the totem, but failed.");
            }
        } else {
            System.out.println("Automatic player does not move the totem " + symbol + ".");
        }

        if (tokenTarget != null) {
            boolean tokenPlaced = placeToken(tokenTarget, symbol);
            if (tokenPlaced) {
                System.out.println("Automatic player placed a " + symbol + " token at " + tokenTarget);
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

    @Override
    public void notifyObservers(String event, Object value) {
        for (Observer obs : observers) {
            obs.update(event, value);
        }
    }
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }


    private TurnPhase currentPhase = TurnPhase.MOVE_TOTEM;

    /**
     * Retourne la phase actuelle du tour.
     */
    public TurnPhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * Passe à la phase suivante.
     */
    public void advancePhase() {
        if (currentPhase == TurnPhase.MOVE_TOTEM) {
            currentPhase = TurnPhase.PLACE_TOKEN;
        } else {
            currentPhase = TurnPhase.MOVE_TOTEM;
        }
        notifyObservers("PhaseChanged", currentPhase); // Informe la vue
    }


    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2(){
        return player2;
    }
}

















