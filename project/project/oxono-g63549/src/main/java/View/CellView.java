/**
 * A CellView is a graphical representation of a cell of the game's board.
 * It displays the color and the symbol of the piece on the cell.
 * It is also responsible of handling the click events on the cell.
 */
package View;

import Controller.GameController;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.*;

public class CellView extends StackPane {
    private final int row;
    private final int col;
    private final GameController controller;
    private final Rectangle rectangle;
    private final Text label;
    private boolean isSelected;

    /**
     * Creates a new CellView.
     * @param row the row of the cell in the board.
     * @param col the column of the cell in the board.
     * @param controller the game controller.
     */
    public CellView(int row, int col, GameController controller) {
        this.row = row;
        this.col = col;
        this.controller = controller;
        this.isSelected = false;

        rectangle = new Rectangle(50, 50);
        rectangle.setFill(javafx.scene.paint.Color.WHITE);
        rectangle.setStroke(javafx.scene.paint.Color.BLACK);

        label = new Text("");
        label.setFont(new Font(20));
        label.setFill(javafx.scene.paint.Color.BLACK);

        getChildren().addAll(rectangle, label);

        setOnMouseClicked(event -> controller.handleCellClick(row, col));
    }

    /**
     * Updates the CellView with the given GameModel.
     * @param model the game model.
     */
    public void updateCell(GameModel model) {
        Board board = model.getBoard();
        Position position = new Position(row, col);
        Piece piece = board.getPiece(position);

        if (piece == null) {
            rectangle.setFill(javafx.scene.paint.Color.WHITE);
            label.setText("");
        } else if (piece instanceof Totem) {
            rectangle.setFill(javafx.scene.paint.Color.LIGHTBLUE);
            label.setText(piece.getSymbol().toString());
        } else if (piece instanceof Token token) {
            if (token.getColor() == Color.PINK) {
                rectangle.setFill(javafx.scene.paint.Color.PINK);
            } else if (token.getColor() == Color.BLACK) {
                rectangle.setFill(javafx.scene.paint.Color.BLACK);
            }
            label.setText(token.getSymbol().toString());
        }

        if (isSelected) {
            rectangle.setStroke(javafx.scene.paint.Color.YELLOW);
            rectangle.setStrokeWidth(3);
        } else {
            rectangle.setStroke(javafx.scene.paint.Color.BLACK);
            rectangle.setStrokeWidth(1);
        }
    }

    /**
     * Sets the selected state of the CellView.
     * @param selected the selected state.
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        if (selected) {
            rectangle.setStroke(javafx.scene.paint.Color.YELLOW);
            rectangle.setStrokeWidth(3);
        } else {
            rectangle.setStroke(javafx.scene.paint.Color.BLACK);
            rectangle.setStrokeWidth(1);
        }
    }

    /**
     * Returns the selected state of the CellView.
     * @return the selected state.
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Returns the position of the CellView in the board.
     * @return the position of the CellView.
     */
    public Position getPosition() {
        return new Position(row, col);
    }
}