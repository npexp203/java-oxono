// ===== InfoView.java =====
package View;

import Controller.GameController;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.DropShadow;
import model.*;
import Util.Observer;

/**
 * Modern InfoView with attractive styling and clear information display.
 */
public class InfoView extends VBox implements Observer {
    private Label currentPlayerLabel;
    private Label tokensRemainingLabel;
    private Label phaseLabel;
    private Label gameStatusLabel;

    public InfoView(GameController gameController) {
        setupInfoPanel();
        createLabels();
        gameController.getModel().addObserver(this);
    }

    private void setupInfoPanel() {
        setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #f8fafc);" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: #e2e8f0;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 15;");

        setPadding(new Insets(20));
        setSpacing(15);
        setMinWidth(250);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setColor(javafx.scene.paint.Color.valueOf("#00000010"));
        setEffect(shadow);
    }

    private void createLabels() {
        Label titleLabel = new Label("🎮 État du Jeu");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #2d3748; -fx-padding: 0 0 10 0;");

        currentPlayerLabel = createStyledLabel("Joueur actuel: Aucun", "#4299e1");
        tokensRemainingLabel = createStyledLabel("Jetons restants: -", "#38b2ac");
        phaseLabel = createStyledLabel("Phase: Initialisation", "#9f7aea");
        gameStatusLabel = createStyledLabel("Statut: En attente", "#48bb78");

        getChildren().addAll(titleLabel, currentPlayerLabel, phaseLabel,
                tokensRemainingLabel, gameStatusLabel);
    }

    private Label createStyledLabel(String text, String color) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        label.setStyle("-fx-text-fill: " + color + ";" +
                "-fx-background-color: " + color + "20;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 8 12;");
        return label;
    }

    @Override
    public void update(String event, Object value) {
        switch (event) {
            case "CurrentPlayerChanged", "Next turn :" -> {
                Player currentPlayer = (Player) value;
                String playerText = "🔴 Joueur Rose";
                if (currentPlayer.getColor() == Color.BLACK) {
                    playerText = "⚫ Joueur Noir (IA)";
                }
                currentPlayerLabel.setText(playerText);
            }
            case "TokenCountChanged" -> {
                String tokenInfo = (String) value;
                tokensRemainingLabel.setText("💎 Jetons: " + tokenInfo);
            }
            case "PhaseChanged" -> {
                TurnPhase phase = (TurnPhase) value;
                String phaseText = phase == TurnPhase.MOVE_TOTEM ?
                        "📍 Phase: Déplacer Totem" : "🎯 Phase: Placer Jeton";
                phaseLabel.setText(phaseText);
            }
            case "GameOver" -> {
                gameStatusLabel.setText("🏆 Partie terminée!");
                gameStatusLabel.setStyle("-fx-text-fill: #e53e3e;" +
                        "-fx-background-color: #e53e3e20;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 12;");
            }
        }
    }
}