package g63549.dev.meteo.controller;

import g63549.dev.meteo.model.Model;
import g63549.dev.meteo.model.WeatherObject;
import g63549.dev.meteo.view.WeatherView;
import java.time.LocalDate;

/**
 * Controller handles requests from the view, fetches data from the model,
 * and updates the view with the retrieved weather information.
 */
public class Controller {

    private final Model model;

    /**
     * Constructs a Controller with a given model.
     */
    public Controller() {
        this.model = new Model();
    }

    /**
     * Fetches the weather data for a given city and date, and updates the view.
     *
     * @param city the city for which to fetch weather data
     * @param date the date for which to fetch weather data
     * @param weatherView the view to update with the weather data
     */
    public void fetchWeather(String city, LocalDate date, WeatherView weatherView) {
        try {
            WeatherObject weatherData = model.fetchWeather(city, date);

            weatherView.displayWeather(weatherData);
        } catch (Exception e) {
            weatherView.setResultText("Error fetching weather data: " + e.getMessage());
        }
    }
}
