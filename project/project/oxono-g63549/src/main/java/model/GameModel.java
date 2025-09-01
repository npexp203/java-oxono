package model;

import Util.MoveStrategy;
import Util.Observable;
import Util.Observer;
import java.util.ArrayList;
import java.util.List;


public class GameModel implements Observable {

    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Player winner;
    private boolean gameOver;

    private Board board;
    private Totem totemX;
    private Totem totemO;

    private TurnPhase currentPhase = TurnPhase.MOVE_TOTEM;
    private Symbol lastMovedSymbol = null;

    private Position selectedTotemPosition;

    private List<Observer> observers = new ArrayList<>();
    private boolean isNotifying = false;

    /**
     * Creates a new game with default board size of 6.
     */
    public GameModel() {
        this(6);
    }

    /**
     * Creates a new game with the specified board size.
     *
     * @param boardSize the size of the game board
     */
    public GameModel(int boardSize) {
        initializeGame(boardSize);
    }

    private void initializeGame(int boardSize) {
        this.player1 = new Player(Colors.PINK);
        this.player2 = new Player(Colors.BLACK, new RandomMoveStrategy());
        this.currentPlayer = player1;
        this.board = new Board(boardSize);
        this.totemX = new Totem(Symbol.X);
        this.totemO = new Totem(Symbol.O);
        this.gameOver = false;
        this.winner = null;
        this.selectedTotemPosition = null;

        placeTotemsAtCenter(boardSize);
    }

    /**
     * Starts a new game with the current board size.
     */
    public void startGame() {
        startGame(board.getSize());
    }

    /**
     * Starts a new game with the specified board size.
     * Validates board size and initializes the game state.
     *
     * @param boardSize the size of the game board (must be between 4 and 10)
     * @throws IllegalArgumentException if board size is invalid
     */
    public void startGame(int boardSize) {
        if (boardSize < 4 || boardSize > 10) {
            throw new IllegalArgumentException("Board size must be between 4 and 10, got: " + boardSize);
        }

        initializeGame(boardSize);
        currentPhase = TurnPhase.MOVE_TOTEM;

        System.out.println("Game started with board size: " + boardSize);

        notifyObservers("GAME_STARTED", boardSize);

        notifyObservers("CURRENT_PLAYER_CHANGED", currentPlayer);
        notifyObservers("PHASE_CHANGED", currentPhase);
    }

    /**
     * Forfeits the current game.
     * The opponent of the current player becomes the winner.
     *
     * @throws IllegalStateException if the game is already over
     */
    public void forfeitGame() {
        if (gameOver) {
            throw new IllegalStateException("The game is already over.");
        }

        this.winner = (currentPlayer == player1) ? player2 : player1;
        this.gameOver = true;
        this.selectedTotemPosition = null;

        System.out.println("Player " + winner.getColor() + " wins because the opponent forfeited!");
        notifyObservers("GAME_FORFEITED", winner);
    }

    /**
     * Selects a totem at the specified position.
     * Notifies observers of the selection change.
     *
     * @param position the position of the totem to select
     * @throws IllegalArgumentException if no totem exists at the position
     */
    public void selectTotem(Position position) {
        if (!hasTotemAt(position)) {
            throw new IllegalArgumentException("No totem at position: " + position);
        }

        Position oldSelection = selectedTotemPosition;
        selectedTotemPosition = position;

        if (oldSelection != null) {
            notifyObservers("TOTEM_DESELECTED", oldSelection);
        }
        notifyObservers("TOTEM_SELECTED", position);
    }

    /**
     * Deselects the currently selected totem.
     * Notifies observers if a totem was previously selected.
     */
    public void deselectTotem() {
        if (selectedTotemPosition != null) {
            Position oldSelection = selectedTotemPosition;
            selectedTotemPosition = null;
            notifyObservers("TOTEM_DESELECTED", oldSelection);
        }
    }

    /**
     * Checks if a totem is currently selected.
     *
     * @return true if a totem is selected, false otherwise
     */
    public boolean hasSelectedTotem() {
        return selectedTotemPosition != null;
    }

    /**
     * Gets the position of the currently selected totem.
     *
     * @return the position of the selected totem, or null if none is selected
     */
    public Position getSelectedTotemPosition() {
        return selectedTotemPosition;
    }

    /**
     * Checks if the specified position is currently selected.
     *
     * @param position the position to check
     * @return true if the position is selected, false otherwise
     */
    public boolean isPositionSelected(Position position) {
        return selectedTotemPosition != null && selectedTotemPosition.equals(position);
    }

    /**
     * Moves a totem to the specified target position.
     * Delegates the move validation to GameRules.
     *
     * @param symbol the symbol of the totem to move (X or O)
     * @param target the target position for the totem
     * @return true if the move was successful, false otherwise
     * @throws IllegalStateException if the game is over
     */
    public boolean moveTotem(Symbol symbol, Position target) {
        if (gameOver) {
            throw new IllegalStateException("The game is over.");
        }
        if (currentPhase != TurnPhase.MOVE_TOTEM) {
            return false;
        }

        Totem totem = (symbol == Symbol.X) ? totemX : totemO;
        boolean moved = GameRules.moveTotem(board, totem, target);

        if (moved) {
            currentPhase = TurnPhase.PLACE_TOKEN;
            lastMovedSymbol = symbol;
            deselectTotem();

            notifyObservers("TOTEM_MOVED", target);
            notifyObservers("PHASE_CHANGED", currentPhase);
            checkGameOver();
        }

        return moved;
    }

    /**
     * Places a token at the specified position.
     * Delegates the placement validation to GameRules.
     *
     * @param position the position where to place the token
     * @param symbol the symbol of the token to place (X or O)
     * @return true if the placement was successful, false otherwise
     */
    public boolean placeToken(Position position, Symbol symbol) {
        if (gameOver || currentPhase != TurnPhase.PLACE_TOKEN) {
            return false;
        }

        if (currentPlayer.getBag().countSymbol(symbol) <= 0) {
            return false;
        }

        Totem totem = (symbol == Symbol.X) ? totemX : totemO;
        Position totemPos = board.findTotemPosition(totem);

        if (!GameRules.isValidTokenPlacement(board, totemPos, position)) {
            return false;
        }

        Piece removedPiece = currentPlayer.getBag().removePiece(symbol);
        if (removedPiece == null) {
            return false;
        }

        board.putPiece(position, removedPiece);

        notifyObservers("TOKEN_PLACED", position);
        notifyTokenCountChanged();

        checkGameOver();
        if (!gameOver) {
            endTurn();
        }

        return true;
    }

    private void notifyTokenCountChanged() {
        if (currentPlayer.getBag().countSymbol(Symbol.X) > 0 || currentPlayer.getBag().countSymbol(Symbol.O) > 0) {
            int remainingX = currentPlayer.getBag().countSymbol(Symbol.X);
            int remainingO = currentPlayer.getBag().countSymbol(Symbol.O);
            notifyObservers("TOKEN_COUNT_CHANGED", "X: " + remainingX + ", O: " + remainingO);
        }
    }

    /**
     * Ends the current turn and switches to the next player.
     * Resets the game phase to totem movement.
     */
    public void endTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        currentPhase = TurnPhase.MOVE_TOTEM;

        System.out.println("Next turn: " + currentPlayer.getColor());
        notifyObservers("CURRENT_PLAYER_CHANGED", currentPlayer);
        notifyObservers("PHASE_CHANGED", currentPhase);
    }

    /**
     * Advances the game phase from totem movement to token placement or vice versa.
     */
    public void advancePhase() {
        if (currentPhase == TurnPhase.MOVE_TOTEM) {
            currentPhase = TurnPhase.PLACE_TOKEN;
        } else {
            currentPhase = TurnPhase.MOVE_TOTEM;
        }
        notifyObservers("PHASE_CHANGED", currentPhase);
    }

    private void checkGameOver() {
        System.out.println("DEBUG: Checking game over, current phase: " + currentPhase);

        if (GameRules.checkWin(board)) {
            winner = currentPlayer;
            gameOver = true;
            selectedTotemPosition = null;
            System.out.println("Game over! The winner is: " + winner.getColor());
            notifyObservers("GAME_WON", winner);

        } else if (!GameRules.isMovePossible(board)) {
            gameOver = true;
            selectedTotemPosition = null;
            System.out.println("Game over! It's a draw, no moves left.");
            notifyObservers("GAME_DRAW", null);

        } else {
            System.out.println("DEBUG: Game continues, phase should stay: " + currentPhase);
        }
    }

    /**
     * Executes an automatic move for the current AI player.
     *
     * @param commandManager the command manager to execute the move
     */
    public void executeAutomaticMove(CommandManager commandManager) {
        if (!currentPlayer.isAutomated()) return;

        System.out.println("Automatic player (" + currentPlayer.getColor() + ") is playing...");

        int maxAttempts = 10;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                Move move = currentPlayer.getMoveStrategy().calculateMove(this);
                if (move == null) {
                    System.out.println("No move returned by strategy.");
                    return;
                }

                CompositeMoveCommand compositeCommand = new CompositeMoveCommand(this, move);
                commandManager.executeCommand(compositeCommand);

                System.out.println("Automatic player executed move: " + move + " (attempt " + attempt + ")");

            } catch (IllegalStateException e) {
                System.out.println("AI move attempt " + attempt + " failed: " + e.getMessage());
                if (attempt == maxAttempts) {
                    System.err.println("AI failed to find a valid move after " + maxAttempts + " attempts");
                    endTurn();
                }
            } catch (Exception e) {
                System.err.println("Unexpected error in AI turn: " + e.getMessage());
                return;
            }
        }
    }

    /**
     * Sets the AI level for player 2.
     *
     * @param level the AI level ("random", "level0", "level1", or "intelligent")
     */
    public void setAILevel(String level) {
        MoveStrategy strategy = switch (level.toLowerCase()) {
            case "random", "level0" -> new RandomMoveStrategy();
            case "level1", "intelligent" -> new IntelligentMoveStrategy();
            default -> new RandomMoveStrategy();
        };
        this.player2 = new Player(Colors.BLACK, strategy);
        System.out.println("Bot set to level: " + level);
    }

    /**
     * Checks if hover effects should be displayed for the given position.
     *
     * @param position the position to check
     * @return true if hover effects should be shown, false otherwise
     */
    public boolean shouldShowHoverEffect(Position position) {
        if (!isCurrentPlayerPink() || !isTotemMovementPhase()) {
            return false;
        }

        if (getPieceAt(position) != null) {
            return false;
        }

        return hasSelectedTotem();
    }

    /**
     * Checks if the specified position is a valid target for the selected totem.
     *
     * @param targetPosition the target position to validate
     * @return true if the position is a valid move target, false otherwise
     */
    public boolean isValidMoveTarget(Position targetPosition) {
        if (!hasSelectedTotem()) {
            return false;
        }

        return isValidTotemMove(selectedTotemPosition, targetPosition);
    }

    /**
     * Validates if a totem move from one position to another is valid.
     * Delegates validation to GameRules.
     *
     * @param from the starting position
     * @param to the target position
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidTotemMove(Position from, Position to) {
        try {
            Piece piece = getPieceAt(from);
            if (!(piece instanceof Totem totem)) return false;

            return GameRules.getValidTotemPositions(board, totem).contains(to);
        } catch (Exception e) {
            return false;
        }
    }

    private void placeTotemsAtCenter(int size) {
        int center1 = size / 2 - 1;
        int center2 = size / 2;

        if (center1 >= 0 && center2 < size) {
            board.putPiece(new Position(center1, center1), totemX);
            board.putPiece(new Position(center2, center2), totemO);
        } else {
            board.putPiece(new Position(1, 1), totemX);
            board.putPiece(new Position(size-2, size-2), totemO);
        }
    }

    /**
     * Restores the game state to the specified player and phase.
     * Used for undo/redo operations.
     *
     * @param currentPlayer the player to restore
     * @param phase the phase to restore
     */
    public void restoreGameState(Player currentPlayer, TurnPhase phase) {
        this.currentPlayer = currentPlayer;
        this.currentPhase = phase;
        notifyObservers("CURRENT_PLAYER_CHANGED", currentPlayer);
        notifyObservers("PHASE_CHANGED", phase);
    }

    /**
     * Restores a token to the current player's bag.
     *
     * @param piece the piece to restore
     */
    public void restoreTokenToCurrentPlayer(Piece piece) {
        if (piece.getColor() == Colors.PINK) {
            player1.getBag().addPiece(piece);
        } else {
            player2.getBag().addPiece(piece);
        }
    }

    /**
     * Restores a token to the specified player's bag.
     *
     * @param piece the piece to restore
     * @param playerColors the color of the player to restore to
     */
    public void restoreTokenToPlayer(Piece piece, Colors playerColors) {
        if (playerColors == Colors.PINK) {
            player1.getBag().addPiece(piece);
        } else {
            player2.getBag().addPiece(piece);
        }
    }

    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public Player getWinner() { return winner; }
    public Board getBoard() { return board; }
    public int getBoardSize() { return board.getSize(); }
    public Totem getTotemX() { return totemX; }
    public Totem getTotemO() { return totemO; }
    public TurnPhase getCurrentPhase() { return currentPhase; }
    public Symbol getLastMovedSymbol() { return lastMovedSymbol; }
    public boolean isGameOver() { return gameOver; }

    public boolean isCurrentPlayerHuman() { return !currentPlayer.isAutomated(); }
    public boolean isCurrentPlayerPink() { return currentPlayer.getColor() == Colors.PINK; }
    public boolean isCurrentPlayerBot() { return currentPlayer.isAutomated(); }
    public boolean isTotemMovementPhase() { return currentPhase == TurnPhase.MOVE_TOTEM; }
    public boolean isTokenPlacementPhase() { return currentPhase == TurnPhase.PLACE_TOKEN; }
    public boolean canCurrentPlayerPlay() { return !gameOver && !currentPlayer.isAutomated(); }
    public Colors getCurrentPlayerColor() { return currentPlayer.getColor(); }

    public Piece getPieceAt(Position position) { return board.getPiece(position); }
    public boolean hasTotemAt(Position position) {
        Piece piece = board.getPiece(position);
        return piece instanceof Totem;
    }
    public Symbol getSymbolAt(Position position) {
        Piece piece = board.getPiece(position);
        return piece != null ? piece.getSymbol() : null;
    }

    public int getRemainingTokens(Symbol symbol) { return currentPlayer.countSymbol(symbol); }
    public boolean wasTotemMoveSuccessful() { return currentPhase == TurnPhase.PLACE_TOKEN; }
    public void removeToken(Position position) { board.removePiece(position); }
    public Position getTotemPosition(Symbol symbol) {
        Totem totem = (symbol == Symbol.X) ? totemX : totemO;
        return board.findTotemPosition(totem);
    }

    public void setLastMovedSymbol(Symbol symbol) { this.lastMovedSymbol = symbol; }
    public int getEmptyCellsCount() {
        int count = 0;
        int boardSize = board.getSize();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Position pos = new Position(i, j);
                if (board.isEmpty(pos)) count++;
            }
        }
        return count;
    }
    public int getRemainingTokensForPlayer(Player player, Symbol symbol) {
        return player.getBag().countSymbol(symbol);
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String event, Object value) {
        if (isNotifying) return;

        isNotifying = true;
        try {
            List<Observer> observersCopy = new ArrayList<>(observers);

            for (Observer obs : observersCopy) {
                try {
                    obs.update(event, value);
                } catch (Exception e) {
                    System.err.println("Error notifying observer: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            isNotifying = false;
        }
    }
}