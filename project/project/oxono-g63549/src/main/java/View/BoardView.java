// ===== BoardView.java =====
package View;

import Controller.GameController;
import Util.Observer;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.effect.DropShadow;
import javafx.geometry.Insets;
import model.GameModel;

/**
 * The BoardView class is a modern GridPane that displays the game board.
 * It displays the Totems and Tokens with enhanced visual styling.
 */
public class BoardView extends GridPane implements Observer {
    private GameController controller;
    private static final double CELL_SIZE = 70;
    private static final double BOARD_PADDING = 20;

    public BoardView(GameController controller) {
        this.controller = controller;
        setupBoardStyling();
    }

    private void setupBoardStyling() {
        setStyle("-fx-background-color: linear-gradient(to bottom, #f0f4f8, #e2e8f0);" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: #cbd5e0;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 15;");

        setPadding(new Insets(BOARD_PADDING));
        setHgap(3);
        setVgap(3);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(15);
        shadow.setColor(javafx.scene.paint.Color.valueOf("#00000020"));
        setEffect(shadow);
    }

    public void initBoard(int size) {
        getChildren().clear();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                CellView cell = new CellView(i, j, controller);
                add(cell, j, i);
            }
        }
    }

    public void updateBoard(GameModel model) {
        for (javafx.scene.Node node : getChildren()) {
            if (node instanceof CellView cell) {
                cell.updateCell(model);
            }
        }
    }

    @Override
    public void update(String event, Object value) {
        if ("TotemMoved".equals(event) || "TokenPlaced".equals(event)) {
            updateBoard(controller.getModel());
        }
    }

    public void highlightCell(int row, int col, boolean highlight) {
        for (javafx.scene.Node node : getChildren()) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeCol = GridPane.getColumnIndex(node);

            if (nodeRow != null && nodeCol != null &&
                    nodeRow == row && nodeCol == col && node instanceof CellView cell) {
                cell.setSelected(highlight);
                break;
            }
        }
    }
}