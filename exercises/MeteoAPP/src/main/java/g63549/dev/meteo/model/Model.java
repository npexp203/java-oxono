package g63549.dev.meteo.model;

import java.io.IOException;
import java.time.LocalDate;

public class Model {

    private String address;
    private LocalDate date;
    private WeatherObject lastWeatherData;

    /**
     * Fetches the weather data for a given address and date.
     * If the requested address and date match the last request, cached data is returned.
     *
     * @param address the address for which to fetch weather data
     * @param date the date for which to fetch weather data
     * @return WeatherObject containing the weather data
     */
    public WeatherObject fetch(String address, LocalDate date) throws IOException, InterruptedException {
        if (address.equals(this.address) && date.equals(this.date)) {
            return lastWeatherData;
        }

        lastWeatherData = WeatherApi.fetch(address, date);
        this.address = address;
        this.date = date;
        return lastWeatherData;
    }
}
