package g63549.dev.meteo.model;

import java.time.LocalDate;

public record WeatherObject(String locality, LocalDate date, Integer weatherCode, double tempMin, double tempMax) {
}
