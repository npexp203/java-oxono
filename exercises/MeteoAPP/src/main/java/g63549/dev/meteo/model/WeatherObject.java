package g63549.dev.meteo.model;

import java.time.LocalDate;

/**
 * Record WeatherObject represents weather data for a specific location and date.
 *
 * @param locality   the location's name
 * @param date       the date for the weather data
 * @param weatherCode a code representing weather conditions
 * @param tempMin    the minimum temperature
 * @param tempMax    the maximum temperature
 */
public record WeatherObject(String locality, LocalDate date, Integer weatherCode, double tempMin, double tempMax) {}
