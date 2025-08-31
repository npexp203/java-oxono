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

    /**
     * Handles the AI turn if the current player is automated.
     */
    private void handleBot() {
        if (model.getCurrentPlayer().isAutomated()) {
            System.out.println("Executing automatic move for AI.");
            model.executeAutomaticMove();
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

        // Vérifier que c'est le tour du joueur humain
        if (model.getCurrentPlayer().getColor() == Color.PINK) {
            if (model.getCurrentPhase() == TurnPhase.MOVE_TOTEM) {
                handleTotemMovement(clickedPosition);
            } else if (model.getCurrentPhase() == TurnPhase.PLACE_TOKEN) {
                handleTokenPlacement(clickedPosition);
                // Le bot joue après que le joueur humain ait terminé son tour
                handleBot();
            }
        }
        updateView();
    }

    /**
     * Handles a click event on the board during the totem movement phase.
     * If a totem is clicked, it gets selected.
     * If an empty cell is clicked while a totem is selected, attempts to move the totem.
     *
     * @param clickedPosition The position of the clicked cell.
     */
    private void handleTotemMovement(Position clickedPosition) {
        System.out.println("Clicked position: " + clickedPosition);

        Piece piece = model.getBoard().getPiece(clickedPosition);

        if (piece instanceof Totem) {
            System.out.println("Valid totem selected at: " + clickedPosition);

            // Clear previous selection
            if (selectedTotemPosition != null) {
                mainView.getBoardView().highlightCell(selectedTotemPosition.x(), selectedTotemPosition.y(), false);
            }

            // Highlight the selected totem
            selectedTotemPosition = clickedPosition;
            mainView.getBoardView().highlightCell(clickedPosition.x(), clickedPosition.y(), true);

        } else if (selectedTotemPosition != null) {
            System.out.println("Attempting to move totem to: " + clickedPosition);

            Symbol symbol = model.getBoard().getPiece(selectedTotemPosition).getSymbol();
            Position oldPosition = selectedTotemPosition;

            // Utiliser le pattern Command au lieu d'appel direct
            MoveTotemCommand command = new MoveTotemCommand(model, symbol, oldPosition, clickedPosition);
            commandManager.executeCommand(command);

            // Vérifier si la commande a réussi en consultant l'état du modèle
            if (model.getCurrentPhase() == TurnPhase.PLACE_TOKEN) {
                System.out.println("Totem moved successfully to: " + clickedPosition);

                // Clear selection and highlighting
                mainView.getBoardView().highlightCell(selectedTotemPosition.x(), selectedTotemPosition.y(), false);
                selectedTotemPosition = null;

                System.out.println("Current phase: " + model.getCurrentPhase());
            } else {
                System.out.println("Invalid move. Try another position.");
            }
        } else {
            System.out.println("No totem selected. Please select a totem first.");
        }
    }

    /**
     * Handles a click event on the board during the token placement phase.
     * Places a token with the symbol of the last moved totem at the clicked position.
     *
     * @param clickedPosition The position of the clicked cell on the board.
     */
    /**
     * Handles a click event on the board during the token placement phase.
     * Places a token with the symbol of the last moved totem at the clicked position using Command pattern.
     */
    private void handleTokenPlacement(Position clickedPosition) {
        System.out.println("Clicked position for token: " + clickedPosition);

        // Utiliser le symbole du totem qui vient d'être déplacé
        Symbol symbolToPlace = model.getLastMovedSymbol();
        if (symbolToPlace == null) {
            System.out.println("Error: No totem was moved in this turn.");
            return;
        }

        int remainingTokens = model.getRemainingTokens(symbolToPlace);
        System.out.println("Remaining " + symbolToPlace + " tokens: " + remainingTokens);

        if (remainingTokens <= 0) {
            System.out.println("No more " + symbolToPlace + " tokens available.");
            return;
        }

        // Utiliser le pattern Command au lieu d'appel direct
        PlaceTokenCommand command = new PlaceTokenCommand(model, clickedPosition, symbolToPlace);
        commandManager.executeCommand(command);

        // Vérifier si la commande a réussi en consultant l'état du modèle
        // Si le jeu n'est pas terminé et qu'on est passé au joueur suivant, c'est que ça a marché
        if (model.getCurrentPhase() == TurnPhase.MOVE_TOTEM || model.isGameOver()) {
            System.out.println("Token " + symbolToPlace + " placed successfully at: " + clickedPosition);
        } else {
            System.out.println("Invalid token placement. Position must be adjacent to the moved totem.");
        }
    }

    /**
     * Undoes the last command.
     */
    public void undo() {
        commandManager.undo();

        // Réinitialiser la sélection après un undo
        if (selectedTotemPosition != null) {
            mainView.getBoardView().highlightCell(selectedTotemPosition.x(), selectedTotemPosition.y(), false);
            selectedTotemPosition = null;
        }

        updateView();
        System.out.println("Undo executed. Current phase: " + model.getCurrentPhase());
    }
    /**
     * Redoes the last undone command.
     */
    public void redo() {
        commandManager.redo();
        updateView();
        System.out.println("Redo executed. Current phase: " + model.getCurrentPhase());
    }

    /**
     * Updates all views with the current game state.
     */
    private void updateView() {
        if (mainView != null && mainView.getBoardView() != null) {
            mainView.getBoardView().updateBoard(model);
        }

        Player currentPlayer = model.getCurrentPlayer();
        int remainingX = currentPlayer.getBag().countSymbol(Symbol.X);
        int remainingO = currentPlayer.getBag().countSymbol(Symbol.O);
        model.notifyObservers("TokenCountChanged", "X: " + remainingX + ", O: " + remainingO);

        // AJOUTER CES NOTIFICATIONS :
        model.notifyObservers("CurrentPlayerChanged", currentPlayer);
        model.notifyObservers("PhaseChanged", model.getCurrentPhase());
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

    /**
     * Forfeits the current game.
     */
    public void forfeitGame() {
        model.forfeitGame();
        updateView();
    }
}