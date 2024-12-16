package Controller;

import Util.Observer;
import View.MainView;
import model.*;

/**
 * The GameController class coordinates the state of the game and updates the views.
 * It implements the Observer interface to receive events from the GameModel.
 * It also contains methods to handle events from the views.
 */
public class GameController implements Observer {
    private final GameModel model;
    private MainView mainView;
    private Position selectedTotemPosition;
    private final CommandManager commandManager;


    /**
     * Constructs a new GameController with the given GameModel.
     * The GameController registers itself as an observer of the GameModel.
     *
     * @param model The GameModel to be used.
     */
    public GameController(GameModel model) {
        this.model = model;
        this.commandManager = new CommandManager();
        this.model.addObserver(this);
    }

    private void HandleBot() {
        if (model.getCurrentPlayer().isAutomated()) {
            System.out.println("Executing automatic move for AI.");
            model.executeAutomaticMove();
            mainView.getBoardView().updateBoard(model);
            model.endTurn();
        }
    }

    /**
     * Sets the view to be used by the GameController.
     * The GameController updates the view after setting it.
     *
     * @param mainView The view to be used.
     */
    public void setView(MainView mainView) {
        this.mainView = mainView;

        // Mise à jour initiale des vues
        updateView();
    }

    /**
     * Starts a new game with the given board size and AI level.
     * The GameController updates the view after starting the game.
     *
     * @param size    The size of the board to be used.
     * @param aiLevel The level of the AI to be used.
     */
    public void startGame(int size, String aiLevel) {
        model.startGame();
        mainView.getBoardView().initBoard(size);
        updateView();
        System.out.println("Game started with board size: " + size + " and AI level: " + aiLevel);
    }

    /**
     * Handles a click event on the board.
     * Determines the current phase of the game and delegates the action accordingly.
     * Updates the view after each action.
     *
     * @param row The row index of the clicked cell.
     * @param col The column index of the clicked cell.
     */
    public void handleCellClick(int row, int col) {
        Position clickedPosition = new Position(row, col);
        if (model.getCurrentPlayer().getColor() == Color.PINK) {
            if (model.getCurrentPhase() == TurnPhase.MOVE_TOTEM) {
                handleTotemMovement(clickedPosition);
            } else if (model.getCurrentPhase() == TurnPhase.PLACE_TOKEN) {
                handleTokenPlacement(clickedPosition);
                HandleBot();
                model.advancePhase();
            }
        }


        updateView();
    }

    /**
     * Handles a click event on the board during the totem movement phase.
     * If a totem belonging to the current player is clicked, it gets selected.
     * If an empty cell is clicked while a totem is selected, attempts to move the totem.
     *
     * @param clickedPosition The position of the clicked cell.
     */
    private void handleTotemMovement(Position clickedPosition) {
        System.out.println("Clicked position: " + clickedPosition);

        Piece piece = model.getBoard().getPiece(clickedPosition);
        System.out.println(piece);
        System.out.println(model.getCurrentPlayer().getColor());
        if (piece instanceof Totem) {

            System.out.println("Valid totem selected at: " + clickedPosition);

            // Highlight the selected totem
            mainView.getBoardView();
            selectedTotemPosition = clickedPosition;
            mainView.getBoardView().highlightCell(clickedPosition.x(), clickedPosition.y(), true);
        } else if (selectedTotemPosition != null) {
            System.out.println("Attempting to move to: " + clickedPosition);

            Symbol symbol = model.getBoard().getPiece(selectedTotemPosition).getSymbol();
            boolean success = model.moveTotem(symbol, clickedPosition);

            if (success) {
                System.out.println("Totem moved successfully to: " + clickedPosition);
                mainView.getBoardView();
                selectedTotemPosition = null;
                model.advancePhase();
                System.out.println("Current phase: " + model.getCurrentPhase());
            } else {
                System.out.println("Invalid move. Try another position.");
            }
        } else {
            System.out.println("No valid totem selected.");
        }
    }


    /**
     * Handles a click event on the board during the token placement phase.
     * A dialog box is displayed to select a token symbol, and the chosen symbol is placed on the board at the clicked position.
     *
     * @param clickedPosition The position of the clicked cell on the board.
     */
    private void handleTokenPlacement(Position clickedPosition) {
        System.out.println("Clicked position for token: " + clickedPosition);

        int remainingX = model.getRemainingTokens(Symbol.X);
        int remainingO = model.getRemainingTokens(Symbol.O);

        System.out.println("Remaining tokens - X: " + remainingX + ", O: " + remainingO);

        boolean success = model.placeToken(clickedPosition, model.getLastMovedSymbol());
        if (success) {
            System.out.println("Token placed successfully at: " + clickedPosition);

        } else {
            System.out.println("Invalid token placement.");
        }
    }


    public void undo() {
        commandManager.undo();
        updateView();
    }

    public void redo() {
        commandManager.redo();
        updateView();
    }

    private void updateView() {
        Player previousPlayer = model.getCurrentPlayer();

        if (mainView != null && mainView.getBoardView() != null) {
            mainView.getBoardView().updateBoard(model);
        }

        Player currentPlayer = model.getCurrentPlayer();
        if (!previousPlayer.equals(currentPlayer)) {
            model.notifyObservers("CurrentPlayerChanged", currentPlayer);
        }

        int remainingX = currentPlayer.getBag().countSymbol(Symbol.X);
        int remainingO = currentPlayer.getBag().countSymbol(Symbol.O);
        model.notifyObservers("TokenCountChanged", "X: " + remainingX + ", O: " + remainingO);
    }


    @Override
    public void update(String event, Object value) {
        System.out.println("Event received: " + event + ", Value: " + value);
        try {
            updateView();
        } catch (Exception e) {
            System.err.println("Error in GameController.update: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public GameModel getModel() {
        return model;
    }

    public void forfeitGame() {
        model.forfeitGame();
        updateView();
    }
}
