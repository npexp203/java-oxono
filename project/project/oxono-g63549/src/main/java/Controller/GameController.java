package Controller;

import View.InfoView;
import View.MainView;
import model.*;

/**
 * Game controller that manages the game logic and coordinates between model and view.
 * Handles user input and translates it into model commands.
 */
public class GameController {
    private GameModel model;
    private MainView mainView;
    private final CommandManager commandManager;

    /**
     * Creates a new GameController with the specified model.
     * 
     * @param model the game model to control
     */
    public GameController(GameModel model) {
        this.model = model;
        this.commandManager = new CommandManager();
    }

    /**
     * Sets the main view for this controller.
     * 
     * @param mainView the main view to set
     */
    public void setView(MainView mainView) {
        this.mainView = mainView;
        model.addObserver(mainView);
    }

    /**
     * Starts a new game with the specified size and AI level.
     * 
     * @param size the board size for the new game
     * @param aiLevel the AI level to use
     */
    public void startGame(int size, String aiLevel) {
        commandManager.clear();

        try {
            model.startGame(size);
            model.setAILevel(aiLevel);

            if (mainView != null) {
                mainView.initBoard(model.getBoardSize());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid game parameters: " + e.getMessage());
        }
    }

    /**
     * Forfeits the current game.
     */
    public void forfeitGame() {
        try {
            model.forfeitGame();
            commandManager.clear();
        } catch (Exception e) {
            System.err.println("Error forfeiting game: " + e.getMessage());
        }
    }

    /**
     * Handles a cell click at the specified position.
     * Translates user input into appropriate game actions.
     * 
     * @param row the row of the clicked cell
     * @param col the column of the clicked cell
     */
    public void handleCellClick(int row, int col) {
        Position clickedPosition = new Position(row, col);

        try {
            if (model.isCurrentPlayerPink()) {
                if (model.isTotemMovementPhase()) {
                    handleTotemMovementInput(clickedPosition);
                } else if (model.isTokenPlacementPhase()) {
                    handleTokenPlacementInput(clickedPosition);

                    if (model.isCurrentPlayerBot()) {
                        executeAITurn();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error handling click: " + e.getMessage());
        }
    }

    private void handleTotemMovementInput(Position clickedPosition) {
        if (model.hasTotemAt(clickedPosition)) {
            model.selectTotem(clickedPosition);
            System.out.println("Totem selected at: " + clickedPosition);

        } else if (model.hasSelectedTotem()) {
            attemptTotemMove(clickedPosition);
        } else {
            System.out.println("No totem selected. Please select a totem first.");
        }
    }

    private void attemptTotemMove(Position targetPosition) {
        Position selectedPosition = model.getSelectedTotemPosition();
        Symbol symbol = model.getSymbolAt(selectedPosition);

        System.out.println("Attempting to move totem to: " + targetPosition);

        try {
            MoveTotemCommand command = new MoveTotemCommand(model, symbol, selectedPosition, targetPosition);
            commandManager.executeCommand(command);

            System.out.println("Move command executed. Current phase: " + model.getCurrentPhase());
        } catch (Exception e) {
            System.out.println("Move failed: " + e.getMessage());
        }
    }

    private void handleTokenPlacementInput(Position clickedPosition) {
        Symbol symbolToPlace = model.getLastMovedSymbol();
        if (symbolToPlace == null) {
            System.out.println("Error: No totem was moved in this turn.");
            return;
        }

        System.out.println("Placing token " + symbolToPlace + " at: " + clickedPosition);

        try {
            PlaceTokenCommand command = new PlaceTokenCommand(model, clickedPosition, symbolToPlace);
            commandManager.executeCommand(command);

            System.out.println("Token placement command executed.");
        } catch (Exception e) {
            System.out.println("Token placement failed: " + e.getMessage());
        }
    }

    private void executeAITurn() {
        try {
            model.executeAutomaticMove(commandManager);
        } catch (Exception e) {
            System.err.println("Error executing AI turn: " + e.getMessage());
        }
    }

    /**
     * Undoes the last command.
     */
    public void undo() {
        try {
            commandManager.undo();
        } catch (Exception e) {
            System.err.println("Error undoing command: " + e.getMessage());
        }
    }

    /**
     * Redoes the last undone command.
     */
    public void redo() {
        try {
            commandManager.redo();
        } catch (Exception e) {
            System.err.println("Error redoing command: " + e.getMessage());
        }
    }

    /**
     * Gets the game model.
     * 
     * @return the game model
     */
    public GameModel getModel() {
        return model;
    }
}