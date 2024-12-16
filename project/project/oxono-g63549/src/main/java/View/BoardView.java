package View;

import Controller.GameController;
import Util.Observer;
import javafx.scene.layout.GridPane;
import model.GameModel;

public class BoardView extends GridPane implements Observer {
    private GameController controller;

    public BoardView(GameController controller) {
        this.controller = controller;
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
        // Si un événement affecte l'état du plateau, actualisez la vue
        if ("TotemMoved".equals(event) || "TokenPlaced".equals(event)) {
            updateBoard(controller.getModel());
        }
    }
    public void clearSelection() {
        for (javafx.scene.Node node : getChildren()) {
            if (node instanceof CellView cell) {
                cell.setSelected(false); // Désélectionne toutes les cellules
            }
        }
    }
    public void highlightCell(int row, int col, boolean highlight) {
        for (javafx.scene.Node node : getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                if (node instanceof CellView cell) {
                    cell.setSelected(highlight); // Active ou désactive la sélection de la cellule
                }
                break;
            }
        }
    }


}
