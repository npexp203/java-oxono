package View;

import Controller.GameController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.DropShadow;
import model.*;
import Util.Observer;

/**
 * Enhanced info view component that displays game information.
 * Shows winner display, detailed token counts, and game statistics.
 */
public class InfoView extends VBox implements Observer {
    private Label gameStatusLabel;

    private Label winnerLabel;
    private Label player1TokensLabel;
    private Label player2TokensLabel;
    private Label emptyCellsLabel;
    private final GameController gameController;

    /**
     * Creates a new InfoView with the specified controller.
     * 
     * @param gameController the game controller to use
     */
    public InfoView(GameController gameController) {
        this.gameController = gameController;
        setupInfoPanel();
        createLabels();
    }

    private void setupInfoPanel() {
        setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f8fafc);" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: #e2e8f0;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 15;");

        setPadding(new Insets(20));
        setSpacing(12);
        setMinWidth(280);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setColor(javafx.scene.paint.Color.valueOf("#00000010"));
        setEffect(shadow);
    }

    private void createLabels() {
        Label titleLabel = new Label(" État du Jeu");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #2d3748; -fx-padding: 0 0 10 0;");

        gameStatusLabel = createStyledLabel("Statut: En attente", "#48bb78");

        winnerLabel = createStyledLabel("", "#e53e3e");
        winnerLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        winnerLabel.setVisible(false);

        player1TokensLabel = createStyledLabel(" Rose - X: 0, O: 0", "#e91e63");
        player2TokensLabel = createStyledLabel(" Noir - X: 0, O: 0", "#424242");
        emptyCellsLabel = createStyledLabel(" Cases vides: 0", "#6b46c1");

        getChildren().addAll(
                titleLabel,
                gameStatusLabel,
                winnerLabel,
                createSeparator(),
                player1TokensLabel,
                player2TokensLabel,
                emptyCellsLabel
        );
    }

    private Label createSeparator() {
        Label separator = new Label("────────────────");
        separator.setStyle("-fx-text-fill: #e2e8f0;");
        return separator;
    }

    private Label createStyledLabel(String text, String color) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.SEMI_BOLD, 13));
        label.setStyle("-fx-text-fill: " + color + ";" +
                "-fx-background-color: " + color + "20;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 8 12;");
        return label;
    }

    @Override
    public void update(String event, Object value) {
        Platform.runLater(() -> {
            System.out.println("InfoView received event: " + event + " with value: " + value);

            switch (event) {
                case "GAME_STARTED" -> handleGameStarted();
                case "GameOver" -> handleGameOver();
                case "GameWon" -> displayWinner((Player) value);
                case "GameDraw" -> displayDraw();
                default -> {
                    updateGameStats();
                }
            }
        });
    }


    void handleGameOver() {
        gameStatusLabel.setText(" Partie terminée!");
        gameStatusLabel.setStyle("-fx-text-fill: #e53e3e;" +
                "-fx-background-color: #e53e3e20;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 8 12;");
    }


    void displayWinner(Player winner) {
        String winnerText = winner.getColor() == Colors.PINK ?
                " VICTOIRE - Joueur Rose!" : " VICTOIRE - Joueur Noir!";

        winnerLabel.setText(winnerText);
        winnerLabel.setStyle("-fx-text-fill: #ffffff;" +
                "-fx-background-color: #48bb78;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 12 16;" +
                "-fx-font-size: 16px;");
        winnerLabel.setVisible(true);

        gameStatusLabel.setText("🎉 Partie terminée!");
        updateGameStats();
    }


    void displayDraw() {
        winnerLabel.setText("MATCH NUL!");
        winnerLabel.setStyle("-fx-text-fill: #ffffff;" +
                "-fx-background-color: #ed8936;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 12 16;" +
                "-fx-font-size: 16px;");
        winnerLabel.setVisible(true);

        gameStatusLabel.setText(" Égalité!");
        updateGameStats();
    }


    public void handleGameStarted() {
        winnerLabel.setVisible(false);
        gameStatusLabel.setText(" Partie en cours");
        gameStatusLabel.setStyle("-fx-text-fill: #48bb78;" +
                "-fx-background-color: #48bb7820;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 8 12;");
        updateGameStats();
    }


    private void updateGameStats() {
        try {
            GameModel model = gameController.getModel();

            Player player1 = model.getPlayer1();
            Player player2 = model.getPlayer2();

            int p1X = model.getRemainingTokensForPlayer(player1, Symbol.X);
            int p1O = model.getRemainingTokensForPlayer(player1, Symbol.O);
            int p2X = model.getRemainingTokensForPlayer(player2, Symbol.X);
            int p2O = model.getRemainingTokensForPlayer(player2, Symbol.O);

            player1TokensLabel.setText(String.format(" Rose - X: %d, O: %d", p1X, p1O));
            player2TokensLabel.setText(String.format(" Noir - X: %d, O: %d", p2X, p2O));

            int emptyCells = model.getEmptyCellsCount();
            emptyCellsLabel.setText(" Cases vides: " + emptyCells);

        } catch (Exception e) {
            System.err.println("Error updating game stats in InfoView: " + e.getMessage());
        }
    }
}