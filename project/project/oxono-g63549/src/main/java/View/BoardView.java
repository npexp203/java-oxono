/**
 * The BoardView class is a GridPane that displays the game board.
 * It displays the Totems and Tokens on the board.
 * It implements the Observer interface to receive events from the GameModel.
 * It also contains methods to highlight a cell on the board.
 */
package View;

import Controller.GameController;
import Util.Observer;
import javafx.scene.layout.GridPane;
import model.GameModel;

public class BoardView extends GridPane implements Observer {
    private GameController controller;

    /**
     * Creates a new BoardView with the given GameController.
     * @param controller The GameController that this BoardView is associated with.
     */
    public BoardView(GameController controller) {
        this.controller = controller;
    }

    /**
     * Initializes the board with the given size.
     * It clears the GridPane and adds new CellViews to it according to the given size.
     * @param size The size of the board.
     */
    public void initBoard(int size) {
        getChildren().clear();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                CellView cell = new CellView(i, j, controller);
                add(cell, j, i);
            }
        }
    }

    /**
     * Updates the board with the given GameModel.
     * It updates all the CellViews on the board with the given GameModel.
     * @param model The GameModel that this BoardView is associated with.
     */
    public void updateBoard(GameModel model) {
        for (javafx.scene.Node node : getChildren()) {
            if (node != null && node instanceof CellView) {
                CellView cell = (CellView) node;
                cell.updateCell(model);
            }else {
                System.err.println("GameModel is null in BoardView.updateBoard");
            }
        }
    }

    @Override
    public void update(String event, Object value) {
        if ("TotemMoved".equals(event) || "TokenPlaced".equals(event)) {
            updateBoard(controller.getModel());
        }
    }
    /**
     * Highlights a cell on the board.
     * @param row The row index of the cell to be highlighted.
     * @param col The column index of the cell to be highlighted.
     * @param highlight If true, the cell is highlighted. If false, the cell is not highlighted.
     */
    public void highlightCell(int row, int col, boolean highlight) {
        for (javafx.scene.Node node : getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                if (node instanceof CellView cell) {
                    cell.setSelected(highlight);
                }
                break;
            }
        }
    }


}