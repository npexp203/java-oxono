package View;

import Controller.GameController;
import javafx.animation.*;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.*;


public class CellView extends StackPane{
    private final int row;
    private final int col;
    private final GameController controller;
    private final GameModel model;
    private Rectangle rectangle;
    private Text label;
    private boolean isSelected;
    private boolean isHovering;
    private Timeline pulseAnimation;
    private static final double CELL_SIZE = 70;

    private static final String BASE_STYLE =
            "-fx-background-color: linear-gradient(to bottom, #f8fafc, #f1f5f9);" +
                    "-fx-border-color: #cbd5e1;" +
                    "-fx-border-width: 1.5;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;";

    private static final String VALID_HOVER_STYLE =
            "-fx-background-color: linear-gradient(to bottom, #86efac, #4ade80);" +
                    "-fx-border-color: #16a34a;" +
                    "-fx-border-width: 3;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;";

    private static final String SELECTED_TOTEM_STYLE =
            "-fx-background-color: linear-gradient(to bottom, #fbbf24, #f59e0b);" +
                    "-fx-border-color: #d97706;" +
                    "-fx-border-width: 4;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;";

    public CellView(int row, int col, GameController controller) {
        this.row = row;
        this.col = col;
        this.controller = controller;
        this.model = controller.getModel();
        this.isSelected = false;
        this.isHovering = false;

        setupCell();
        setupEventHandling();
    }

    private void setupCell() {
        rectangle = new Rectangle(CELL_SIZE, CELL_SIZE);
        rectangle.setArcWidth(12);
        rectangle.setArcHeight(12);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.valueOf("#e2e8f0"));
        rectangle.setStrokeWidth(1.5);

        DropShadow cellShadow = new DropShadow();
        cellShadow.setRadius(4);
        cellShadow.setOffsetY(3);
        cellShadow.setColor(Color.valueOf("#00000025"));
        rectangle.setEffect(cellShadow);

        label = new Text("");
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        label.setFill(Color.valueOf("#2d3748"));

        setStyle(BASE_STYLE);
        getChildren().addAll(rectangle, label);
    }

    private void setupEventHandling() {
        setOnMouseClicked(event -> {
            playClickAnimation();
            controller.handleCellClick(row, col);
        });

        setOnMouseEntered(event -> {
            isHovering = true;
            updateHoverEffects();
        });

        setOnMouseExited(event -> {
            isHovering = false;
            resetStyle();
        });
    }

    private void playClickAnimation() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(120), this);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(0.93);
        scale.setToY(0.93);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }


    private void updateHoverEffects() {
        Position currentPos = new Position(row, col);

        if (model.shouldShowHoverEffect(currentPos)) {
            if (model.isValidMoveTarget(currentPos)) {
                setStyle(VALID_HOVER_STYLE);
                playScaleAnimation(1.05);

                DropShadow greenGlow = new DropShadow();
                greenGlow.setColor(Color.valueOf("#10b98180"));
                greenGlow.setRadius(8);
                greenGlow.setOffsetY(3);
                setEffect(greenGlow);
            }
        }
    }

    private void playScaleAnimation(double targetScale) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), this);
        scale.setToX(targetScale);
        scale.setToY(targetScale);
        scale.play();
    }


    private void updateDisplay() {
        Position position = new Position(row, col);
        Piece piece = model.getPieceAt(position);

        updateCellAppearance(piece);
        updateSelectionState();

    }


    public void updateCell(GameModel model) {
        updateDisplay();
    }

    private void updateCellAppearance(Piece piece) {
        switch (piece) {
            case null -> {
                rectangle.setFill(Color.WHITE);
                label.setText("");
                label.setEffect(null);
            }
            case Totem totem -> {

                rectangle.setFill(Color.valueOf("#3b82f6"));
                label.setText(piece.getSymbol().toString());
                label.setFill(Color.WHITE);
                label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

                DropShadow blueGlow = new DropShadow();
                blueGlow.setColor(Color.valueOf("#3b82f680"));
                blueGlow.setRadius(10);
                blueGlow.setOffsetY(2);
                label.setEffect(blueGlow);
            }
            case Token token -> updateTokenAppearance(token);
            default -> {
            }
        }
    }

    private void updateTokenAppearance(Token token) {
        label.setText(token.getSymbol().toString());
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

        if (token.getColor() == Colors.PINK) {
            rectangle.setFill(Color.valueOf("#f472b6"));
            label.setFill(Color.valueOf("#be185d"));

            DropShadow pinkGlow = new DropShadow();
            pinkGlow.setColor(Color.valueOf("#ec489960"));
            pinkGlow.setRadius(6);
            pinkGlow.setOffsetY(2);
            label.setEffect(pinkGlow);

        } else {
            rectangle.setFill(Color.valueOf("#4b5563"));
            label.setFill(Color.WHITE);

            DropShadow darkGlow = new DropShadow();
            darkGlow.setColor(Color.valueOf("#37415160"));
            darkGlow.setRadius(6);
            darkGlow.setOffsetY(2);
            label.setEffect(darkGlow);
        }
    }

    private void updateSelectionState() {
        Position position = new Position(row, col);

        if (model.isPositionSelected(position)) {
            Piece piece = model.getPieceAt(position);

            if (piece instanceof Totem) {
                setStyle(SELECTED_TOTEM_STYLE);

                DropShadow goldGlow = new DropShadow();
                goldGlow.setColor(Color.valueOf("#f59e0b80"));
                goldGlow.setRadius(12);
                goldGlow.setOffsetY(3);
                setEffect(goldGlow);

                playPulseAnimation();
            }
        } else if (!isHovering) {
            resetStyle();
        }
    }

    private void playPulseAnimation() {
        if (pulseAnimation != null) {
            pulseAnimation.stop();
        }
        
        pulseAnimation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(scaleXProperty(), 1.0),
                        new KeyValue(scaleYProperty(), 1.0)),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(scaleXProperty(), 1.08),
                        new KeyValue(scaleYProperty(), 1.08)),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(scaleXProperty(), 1.0),
                        new KeyValue(scaleYProperty(), 1.0))
        );
        pulseAnimation.setCycleCount(Timeline.INDEFINITE);
        pulseAnimation.play();
    }

    public void resetStyle() {
        if (!isHovering) {
            setStyle(BASE_STYLE);
            playScaleAnimation(1.0);

            if (pulseAnimation != null) {
                pulseAnimation.stop();
                pulseAnimation = null;
            }

            DropShadow cellShadow = new DropShadow();
            cellShadow.setRadius(4);
            cellShadow.setOffsetY(3);
            cellShadow.setColor(Color.valueOf("#00000025"));
            setEffect(cellShadow);
        }
    }


    public Position getPosition() {
        return new Position(row, col);
    }


}