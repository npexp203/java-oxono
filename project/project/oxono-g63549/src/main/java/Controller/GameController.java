package Controller;

import Util.Observer;
import View.MainView;
import View.TokenSelectionDialog;
import model.*;

public class GameController implements Observer {
    private final GameModel model;
    private MainView mainView;
    private Position selectedTotemPosition; // Position du totem actuellement sélectionné
    private final CommandManager commandManager;


    public GameController(GameModel model) {
        this.model = model;
        this.commandManager = new CommandManager();
        this.model.addObserver(this); // S'enregistre comme observateur du modèle
    }

    public void setView(MainView mainView) {
        this.mainView = mainView;

        // Mise à jour initiale des vues
        updateView();
    }

    public void startGame(int size, String aiLevel) {
        model.startGame();
        mainView.getBoardView().initBoard(size);
        updateView();
        System.out.println("Game started with board size: " + size + " and AI level: " + aiLevel);
    }

    public void forfeitGame() {
        model.forfeitGame();
        updateView();
    }

    public void handleCellClick(int row, int col) {
        Position clickedPosition = new Position(row, col);

        if (model.getCurrentPhase() == TurnPhase.MOVE_TOTEM) {
            handleTotemMovement(clickedPosition);
        } else if (model.getCurrentPhase() == TurnPhase.PLACE_TOKEN) {
            handleTokenPlacement(clickedPosition);
        }

        updateView(); // Mettre à jour la vue après chaque action
    }

    private void handleTotemMovement(Position clickedPosition) {
        Piece piece = model.getBoard().getPiece(clickedPosition);

        if (piece instanceof Totem && piece.getColor() == model.getCurrentPlayer().getColor()) {
            // Sélection d'un totem appartenant au joueur actuel
            mainView.getBoardView().clearSelection();
            selectedTotemPosition = clickedPosition;
            mainView.getBoardView().highlightCell(clickedPosition.x(), clickedPosition.y(),true);
            System.out.println("Totem sélectionné : " + selectedTotemPosition);
        } else if (selectedTotemPosition != null) {
            // Tentative de déplacement du totem
            Symbol symbol = model.getBoard().getPiece(selectedTotemPosition).getSymbol();
            boolean success = model.moveTotem(symbol, clickedPosition);

            if (success) {
                mainView.getBoardView().clearSelection();
                selectedTotemPosition = null; // Réinitialise la sélection
                System.out.println("Déplacement réussi du totem à " + clickedPosition);
            } else {
                System.out.println("Déplacement invalide. Essayez une autre case.");
            }
        } else {
            System.out.println("Veuillez sélectionner un totem valide.");
        }
    }

    private void handleTokenPlacement(Position clickedPosition) {
        // Affiche une boîte de dialogue pour choisir un symbole de token
        int remainingX = model.getCurrentPlayer().getBag().countSymbol(Symbol.X);
        int remainingO = model.getCurrentPlayer().getBag().countSymbol(Symbol.O);

        Symbol chosenSymbol = TokenSelectionDialog.showTokenSelectionDialog(remainingX, remainingO);

        if (chosenSymbol != null) {
            boolean success = model.placeToken(clickedPosition, chosenSymbol);
            if (success) {
                System.out.println("Token " + chosenSymbol + " placé à " + clickedPosition);
            } else {
                System.out.println("Placement invalide. Essayez une autre case.");
            }
        } else {
            System.out.println("Aucun token sélectionné.");
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

    @Override
    public void update(String event, Object value) {
        switch (event) {
            case "TotemMoved":
                System.out.println("Totem déplacé : " + value);
                break;
            case "TokenPlaced":
                System.out.println("Token placé : " + value);
                break;
            case "GameOver":
                System.out.println("Fin de partie. Gagnant : " + value);
                break;
            default:
                System.out.println("Événement inconnu : " + event);
                break;
        }
        updateView();
    }

    private void updateView() {
        mainView.getBoardView().updateBoard(model); // Met à jour le plateau
        // L'InfoView se met déjà à jour en fonction des événements générés par le modèle.
        model.notifyObservers("CurrentPlayerChanged", model.getCurrentPlayer());
        int remainingX = model.getCurrentPlayer().getBag().countSymbol(Symbol.X);
        int remainingO = model.getCurrentPlayer().getBag().countSymbol(Symbol.O);
        model.notifyObservers("TokenCountChanged", "X: " + remainingX + ", O: " + remainingO);
    }

    public GameModel getModel(){
        return model;
    }
}
