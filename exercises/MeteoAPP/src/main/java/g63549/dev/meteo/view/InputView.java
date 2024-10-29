package g63549.dev.meteo.view;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class InputView extends VBox {

    private final TextField addressField = new TextField();
    private final TextField dateField = new TextField();

    public InputView() {
        addressField.setPromptText("Enter address");
        dateField.setPromptText("Enter date (YYYY-MM-DD)");
        this.getChildren().addAll(addressField, dateField);
    }

    public String getAddress() {
        return addressField.getText();
    }

    public LocalDate getDate() {
        return LocalDate.parse(dateField.getText());
    }
}
