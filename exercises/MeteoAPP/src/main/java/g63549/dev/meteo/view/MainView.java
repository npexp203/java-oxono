package g63549.dev.meteo.view;

import g63549.dev.meteo.controller.Controller;
import g63549.dev.meteo.model.WeatherObject;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MainView extends VBox {

    private final InputView inputView = new InputView();
    private final WeatherView weatherView = new WeatherView();
    private Controller controller;

    public MainView(Stage stage) {
        Button fetchButton = new Button("Fetch Weather");
        fetchButton.setOnAction(e -> fetchWeather());

        this.getChildren().addAll(inputView, fetchButton, weatherView);
        Scene scene = new Scene(this, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void fetchWeather() {
        String address = inputView.getAddress();
        LocalDate date = inputView.getDate();
        controller.actionFetch(address, date);
    }

    public void update(WeatherObject weatherData) {
        weatherView.setLocality(weatherData.locality());
        weatherView.setTempMin(weatherData.tempMin());
        weatherView.setTempMax(weatherData.tempMax());
        weatherView.setImage(weatherData.weatherCode());
    }


}
