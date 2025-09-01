package View;

import Controller.GameController;
import Util.Observer;
import javafx.scene.layout.GridPane;
import javafx.scene.effect.DropShadow;
import javafx.geometry.Insets;
import model.GameModel;


public class BoardView extends GridPane implements Observer {
    private final GameController controller;
    private final GameModel model;
    private static final double BOARD_PADDING = 20;

    /**
     * Creates a new BoardView with the specified controller.
     * 
     * @param controller the game controller to use
     */
    public BoardView(GameController controller) {
        this.controller = controller;
        this.model = controller.getModel();

        model.addObserver(this);

        setupBoardStyling();
    }

    private void setupBoardStyling() {
        setStyle(
                "-fx-background-color: linear-gradient(to bottom, #f0f4f8, #e2e8f0);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: #cbd5e0;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;"
        );

        setPadding(new Insets(BOARD_PADDING));
        setHgap(3);
        setVgap(3);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(15);
        shadow.setColor(javafx.scene.paint.Color.valueOf("#00000020"));
        setEffect(shadow);
    }

    /**
     * Initializes the board with the specified size.
     * Creates all CellView components that will also observe the model.
     * 
     * @param size the size of the board to initialize
     */
    public void initBoard(int size) {
        getChildren().clear();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                CellView cell = new CellView(i, j, controller);
                add(cell, j, i);
            }
        }

        updateBoard(model);

        System.out.println("Board initialized with size: " + size);
    }

    /**
     * Updates the entire board by delegating to individual CellView components.
     * Each CellView updates itself via the Observer pattern.
     * 
     * @param model the game model to update from
     */
    public void updateBoard(GameModel model) {
        for (javafx.scene.Node node : getChildren()) {
            if (node instanceof CellView cell) {
                cell.updateCell(model);
            }
        }
    }

    /**
     * Refreshes the entire display.
     */
    public void refreshDisplay() {
        updateBoard(model);
    }

    @Override
    public void update(String event, Object value) {
        switch (event) {
            case "GAME_STARTED":
                int boardSize = (Integer) value;
                if (getChildren().size() != boardSize * boardSize) {
                    initBoard(boardSize);
                } else {
                    refreshDisplay();
                }
                break;

            case "TOTEM_MOVED":
            case "TOKEN_PLACED":
            case "TOTEM_SELECTED":
            case "TOTEM_DESELECTED":
            case "PHASE_CHANGED":
            case "CURRENT_PLAYER_CHANGED":
                refreshDisplay();
                break;

            case "GAME_WON":
            case "GAME_DRAW":
            case "GAME_FORFEITED":
                refreshDisplay();
                System.out.println("Game ended - final board state displayed");
                break;

            default:
                refreshDisplay();
                break;
        }
    }

    public GameModel getModel() {
        return model;
    }
}