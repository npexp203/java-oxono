package View;

import Controller.GameController;
import javafx.animation.*;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.util.Duration;
import model.*;

/**
 * A modern CellView with smooth animations and attractive styling.
 */
public class CellView extends StackPane {
    private final int row;
    private final int col;
    private final GameController controller;
    private Rectangle rectangle;
    private Text label;
    private boolean isSelected;
    private static final double CELL_SIZE = 60;

    public CellView(int row, int col, GameController controller) {
        this.row = row;
        this.col = col;
        this.controller = controller;
        this.isSelected = false;

        setupCell();
        setupEventHandling();
    }

    private void setupCell() {
        rectangle = new Rectangle(CELL_SIZE, CELL_SIZE);
        rectangle.setArcWidth(12);
        rectangle.setArcHeight(12);
        rectangle.setFill(javafx.scene.paint.Color.WHITE);
        rectangle.setStroke(javafx.scene.paint.Color.valueOf("#e2e8f0"));
        rectangle.setStrokeWidth(1.5);

        DropShadow cellShadow = new DropShadow();
        cellShadow.setRadius(3);
        cellShadow.setOffsetY(2);
        cellShadow.setColor(javafx.scene.paint.Color.valueOf("#00000015"));
        rectangle.setEffect(cellShadow);

        label = new Text("");
        label.setFont(Font.font("System", FontWeight.BOLD, 24));
        label.setFill(javafx.scene.paint.Color.valueOf("#2d3748"));

        getChildren().addAll(rectangle, label);
    }

    private void setupEventHandling() {
        setOnMouseClicked(event -> {
            playClickAnimation();
            controller.handleCellClick(row, col);
        });

        setOnMouseEntered(event -> playHoverAnimation(true));
        setOnMouseExited(event -> playHoverAnimation(false));
    }

    private void playClickAnimation() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), this);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(0.95);
        scale.setToY(0.95);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    private void playHoverAnimation(boolean entering) {
        TranslateTransition translate = new TranslateTransition(Duration.millis(150), this);
        if (entering) {
            translate.setToY(-2);
            rectangle.setFill(javafx.scene.paint.Color.valueOf("#f7fafc"));
        } else {
            translate.setToY(0);
            updateCellColor();
        }
        translate.play();
    }

    public void updateCell(GameModel model) {
        Board board = model.getBoard();
        Position position = new Position(row, col);
        Piece piece = board.getPiece(position);

        updateCellAppearance(piece);
        updateSelectionState();
    }

    private void updateCellAppearance(Piece piece) {
        if (piece == null) {
            rectangle.setFill(javafx.scene.paint.Color.WHITE);
            label.setText("");
        } else if (piece instanceof Totem) {
            rectangle.setFill(javafx.scene.paint.Color.valueOf("#63b3ed"));
            label.setText(piece.getSymbol().toString());
            label.setFill(javafx.scene.paint.Color.WHITE);

            DropShadow glowEffect = new DropShadow();
            glowEffect.setColor(javafx.scene.paint.Color.valueOf("#3182ce"));
            glowEffect.setRadius(8);
            label.setEffect(glowEffect);
        } else if (piece instanceof Token token) {
            updateTokenAppearance(token);
        }
    }

    private void updateTokenAppearance(Token token) {
        if (token.getColor() == Color.PINK) {
            rectangle.setFill(javafx.scene.paint.Color.valueOf("#f687b3"));
            label.setFill(javafx.scene.paint.Color.valueOf("#97266d"));
        } else if (token.getColor() == Color.BLACK) {
            rectangle.setFill(javafx.scene.paint.Color.valueOf("#4a5568"));
            label.setFill(javafx.scene.paint.Color.WHITE);
        }

        label.setText(token.getSymbol().toString());

        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setRadius(3);
        innerShadow.setColor(javafx.scene.paint.Color.valueOf("#00000030"));
        rectangle.setEffect(innerShadow);
    }

    private void updateCellColor() {
        Board board = controller.getModel().getBoard();
        Position position = new Position(row, col);
        Piece piece = board.getPiece(position);
        updateCellAppearance(piece);
    }

    private void updateSelectionState() {
        if (isSelected) {
            rectangle.setStroke(javafx.scene.paint.Color.valueOf("#f6e05e"));
            rectangle.setStrokeWidth(3);

            DropShadow selectionGlow = new DropShadow();
            selectionGlow.setColor(javafx.scene.paint.Color.valueOf("#f6e05e"));
            selectionGlow.setRadius(8);
            rectangle.setEffect(selectionGlow);
        } else {
            rectangle.setStroke(javafx.scene.paint.Color.valueOf("#e2e8f0"));
            rectangle.setStrokeWidth(1.5);
            updateCellColor();
        }
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        updateSelectionState();

        if (selected) {
            playPulseAnimation();
        }
    }

    private void playPulseAnimation() {
        Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(scaleXProperty(), 1.0),
                        new KeyValue(scaleYProperty(), 1.0)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(scaleXProperty(), 1.05),
                        new KeyValue(scaleYProperty(), 1.05)),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(scaleXProperty(), 1.0),
                        new KeyValue(scaleYProperty(), 1.0))
        );
        pulse.setCycleCount(1);
        pulse.play();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Position getPosition() {
        return new Position(row, col);
    }
}