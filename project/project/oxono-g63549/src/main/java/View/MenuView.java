package View;
import Controller.*;


import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;


public class MenuView extends HBox {
    private TextField boardSizeField;
    private ComboBox<String> aiLevelCombo;
    private Button startButton;
    private Button forfeitButton;
    private Button UndoButton;
    private Button RedoButton;

    public MenuView(GameController controller) {
        boardSizeField = new TextField("6");
        aiLevelCombo = new ComboBox<>();
        aiLevelCombo.getItems().addAll("Random (Level 0)");
        aiLevelCombo.setValue("Random (Level 0)");

        startButton = new Button("Start");
        startButton.setOnAction(e -> {
            int size = Integer.parseInt(boardSizeField.getText());
            String level = aiLevelCombo.getValue();
            controller.startGame(size, level);
        });

        forfeitButton = new Button("Forfeit");
        forfeitButton.setOnAction(e -> controller.forfeitGame());
        UndoButton = new Button("Undo");
        UndoButton.setOnAction(e -> controller.undo());
        RedoButton = new Button("Redo");
        RedoButton.setOnAction(e -> controller.redo());

        getChildren().addAll(new Label("Taille:"), boardSizeField, new Label("Niveau IA:"), aiLevelCombo, startButton, UndoButton, RedoButton, forfeitButton);
    }
}

