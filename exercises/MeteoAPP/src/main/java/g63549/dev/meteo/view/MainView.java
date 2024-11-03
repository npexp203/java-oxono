package g63549.dev.meteo.view;

import g63549.dev.meteo.controller.Controller;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MainView extends BorderPane {
    private final InputView inputView;
    private final WeatherView weatherView;
    private final Controller controller;

    public MainView() {
        this.inputView = new InputView();
        this.weatherView = new WeatherView();
        this.controller = new Controller();

        VBox centerBox = new VBox(20, weatherView);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(15));

        setCenter(centerBox);
        setBottom(inputView);

        inputView.getFetchButton().setOnAction(e -> handleFetchWeather());
    }

    private void handleFetchWeather() {
        String city = inputView.getAddress();
        var date = inputView.getDate();

        if (city.isEmpty() || date == null) {
            weatherView.setResultText("Please enter a city and select a date.");
            return;
        }

        controller.fetchWeather(city, date, weatherView);
    }
}
