package g63549.dev.meteo.model;

import g63549.dev.meteo.model.WeatherException;
import g63549.dev.meteo.model.WeatherObject;
import g63549.dev.meteo.model.WeatherApi;

import java.time.LocalDate;

public class Model {

    private String lastAddress;
    private LocalDate lastDate;
    private WeatherObject lastWeatherData;
    /**
     * Fetches the weather data for a given address and date.
     * If the requested address and date match the last request, cached data is returned.
     *
     * @param address the address for which to fetch weather data
     * @param date the date for which to fetch weather data
     * @return WeatherObject containing the weather data
     * @throws WeatherException if there is an error during the data fetch
     */

    public WeatherObject fetchWeather(String address, LocalDate date) {
        if (address.equals(lastAddress) && date.equals(lastDate)) {
            return lastWeatherData;
        }
            lastWeatherData = WeatherApi.fetch(address, date);
            lastAddress = address;
            lastDate = date;
            return lastWeatherData;
    }
}
