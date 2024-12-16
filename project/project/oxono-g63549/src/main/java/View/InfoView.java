package View;

import Controller.GameController;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.*;
import Util.Observer;

public class InfoView extends VBox implements Observer {
    private Label currentPlayerLabel;
    private Label tokensRemainingLabel;

    public InfoView(GameController gameController) {
        currentPlayerLabel = new Label();
        tokensRemainingLabel = new Label();

        getChildren().addAll(currentPlayerLabel, tokensRemainingLabel);
    }

    @Override
    public void update(String event, Object value) {
        if (event.equals("CurrentPlayerChanged")) {
            Player currentPlayer = (Player) value;
            currentPlayerLabel.setText("Current Player: " + currentPlayer.getColor());
        } else if (event.equals("TokenCountChanged")) {
            String tokenInfo = (String) value; // Exemple : "X: 5, O: 4"
            tokensRemainingLabel.setText("Tokens Remaining: " + tokenInfo);
        }
    }
}
