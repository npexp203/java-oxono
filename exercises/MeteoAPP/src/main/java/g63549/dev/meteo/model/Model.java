package gmodel;

import g63549.dev.meteo.model.WeatherException;
import g63549.dev.meteo.model.WeatherObject;
import g63549.dev.meteo.model.WeatherApi;

import java.time.LocalDate;

public class Model {

    private String lastAddress;
    private LocalDate lastDate;
    private WeatherObject lastWeatherData;

    public WeatherObject fetchWeather(String address, LocalDate date) {
        if (address.equals(lastAddress) && date.equals(lastDate)) {
            return lastWeatherData;
        }
        try {
            lastWeatherData = WeatherApi.fetch(address, date);
            lastAddress = address;
            lastDate = date;
            return lastWeatherData;
        } catch (Exception e) {
            throw new WeatherException("Error fetching weather data");
        }
    }
}
