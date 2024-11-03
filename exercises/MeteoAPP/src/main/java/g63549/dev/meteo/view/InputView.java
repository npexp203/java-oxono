package g63549.dev.meteo.view;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.time.LocalDate;

/**
 * InputView provides the input fields and button for user interaction.
 */
public class InputView extends HBox {
    private final TextField cityField;
    private final DatePicker datePicker;
    private final Button fetchButton;

    public InputView() {
        cityField = new TextField();
        cityField.setPromptText("Enter city name");

        datePicker = new DatePicker(LocalDate.now());
        fetchButton = new Button("Get Weather");

        this.getChildren().addAll(cityField, datePicker, fetchButton);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(15));
    }

    /**
     * @return the city entered by the user
     */
    public String getAddress() {
        return cityField.getText();
    }

    /**
     * @return the selected date
     */
    public LocalDate getDate() {
        return datePicker.getValue();
    }

    /**
     * @return the fetch button
     */
    public Button getFetchButton() {
        return fetchButton;
    }
}
