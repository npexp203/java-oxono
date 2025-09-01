package View;

import Controller.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.DropShadow;

public class MenuView extends HBox {
    private TextField boardSizeField;
    private ComboBox<String> aiLevelCombo;
    private Button startButton;
    private Button forfeitButton;
    private Button undoButton;
    private Button redoButton;

    /**
     * Creates a new MenuView with the specified controller.
     * 
     * @param controller the game controller to use
     */
    public MenuView(GameController controller) {
        setupMenuStyling();
        createControls();
        setupEventHandlers(controller);
        layoutControls();
    }

    private void setupMenuStyling() {
        setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 15;");

        setSpacing(15);
        setPadding(new Insets(15));

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setColor(javafx.scene.paint.Color.valueOf("#00000020"));
        setEffect(shadow);
    }

    private void createControls() {
        boardSizeField = new TextField("6");
        boardSizeField.setPrefWidth(60);
        boardSizeField.setStyle(getInputStyle());

        aiLevelCombo = new ComboBox<>();
        aiLevelCombo.getItems().addAll("Aléatoire (Niveau 0)", "Intelligent (Niveau 1)");
        aiLevelCombo.setValue("Aléatoire (Niveau 0)");
        aiLevelCombo.setStyle(getInputStyle());

        startButton = createStyledButton(" Nouvelle Partie", "#48bb78");
        forfeitButton = createStyledButton("️ Abandonner", "#e53e3e");
        undoButton = createStyledButton(" Annuler", "#4299e1");
        redoButton = createStyledButton(" Refaire", "#4299e1");
    }

    private String getInputStyle() {
        return "-fx-background-color: white;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #cbd5e0;" +
                "-fx-border-radius: 8;" +
                "-fx-padding: 8;" +
                "-fx-font-size: 13px;";
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 10 20;" +
                "-fx-font-size: 13px;" +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e ->
                button.setStyle(button.getStyle().replace(color, adjustBrightness(color, 0.1))));
        button.setOnMouseExited(e ->
                button.setStyle(button.getStyle().replace(adjustBrightness(color, 0.1), color)));

        return button;
    }

    private String adjustBrightness(String color, double factor) {
        return color.equals("#48bb78") ? "#38a169" :
                color.equals("#e53e3e") ? "#c53030" :
                        color.equals("#4299e1") ? "#3182ce" : color;
    }


    private void setupEventHandlers(GameController controller) {
        startButton.setOnAction(e -> {
            try {
                int size = Integer.parseInt(boardSizeField.getText().trim());
                String selectedText = aiLevelCombo.getValue();
                String aiLevel = convertToAILevel(selectedText);

                controller.startGame(size, aiLevel);

            } catch (NumberFormatException ex) {
                showErrorDialog("Erreur", "Veuillez entrer un nombre valide pour la taille du plateau.");
                boardSizeField.setText("6");
            }
        });

        forfeitButton.setOnAction(e -> controller.forfeitGame());
        undoButton.setOnAction(e -> controller.undo());
        redoButton.setOnAction(e -> controller.redo());
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void layoutControls() {
        Label sizeLabel = createLabel(" Taille:");
        Label aiLabel = createLabel(" Niveau IA:");

        getChildren().addAll(
                sizeLabel, boardSizeField,
                aiLabel, aiLevelCombo,
                startButton, undoButton, redoButton, forfeitButton
        );
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 14));
        label.setStyle("-fx-text-fill: white;");
        return label;
    }


    private String convertToAILevel(String displayText) {
        if (displayText.contains("Intelligent") || displayText.contains("Niveau 1")) {
            return "level1";
        } else {
            return "random";
        }
    }
}