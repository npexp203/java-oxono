package View;

import Controller.GameController;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.*;
import Util.Observer;

/**
 * The InfoView class displays information about the current state of the game,
 * including the current player and the number of tokens remaining.
 * It observes changes in the game model and updates the display accordingly.
 */
public class InfoView extends VBox implements Observer {
    private Label currentPlayerLabel;
    private Label tokensRemainingLabel;

    /**
     * Constructs an InfoView object with the specified game controller.
     *
     * @param gameController the game controller used to control the game
     */
    public InfoView(GameController gameController) {
        currentPlayerLabel = new Label();
        tokensRemainingLabel = new Label();

        getChildren().addAll(currentPlayerLabel, tokensRemainingLabel);
    }

    /**
     * Updates the view based on the specified event and value.
     *
     * @param event the event that triggered the update
     * @param value the new value associated with the event
     */
    @Override
    public void update(String event, Object value) {
        if (event.equals("CurrentPlayerChanged")) {
            Player currentPlayer = (Player) value;
            currentPlayerLabel.setText("Current Player: " + currentPlayer.getColor());
        } else if (event.equals("TokenCountChanged")) {
            String tokenInfo = (String) value; // Example: "X: 5, O: 4"
            tokensRemainingLabel.setText("Tokens Remaining: " + tokenInfo);
        }
    }
}