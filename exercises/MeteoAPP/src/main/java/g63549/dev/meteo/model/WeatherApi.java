package g63549.dev.meteo.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class WeatherApi {

    private static final String OSM_API_URL = "https://nominatim.openstreetmap.org/search.php?q=";
    private static final String OSM_API_FORMAT = "&format=jsonv2";
    private static final String METEO_API_URL = "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&daily=temperature_2m_min,temperature_2m_max&timezone=Europe/Brussels";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static WeatherObject fetch(String address, LocalDate date) {
        try {
            var coordinates = fetchCoordinates(address);
            return fetchWeatherData(coordinates[0], coordinates[1], address, date);
        } catch (Exception e) {
            throw new WeatherException("Error fetching weather data: " + e.getMessage());
        }
    }

    private static double[] fetchCoordinates(String address) throws IOException, InterruptedException {
        String url = OSM_API_URL + address + OSM_API_FORMAT;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode jsonNode = objectMapper.readTree(response.body());
        if (!jsonNode.isArray() || jsonNode.isEmpty()) {
            throw new WeatherException("No location data found for address: " + address);
        }

        double latitude = jsonNode.get(0).get("lat").asDouble();
        double longitude = jsonNode.get(0).get("lon").asDouble();
        return new double[] { latitude, longitude };
    }

    private static WeatherObject fetchWeatherData(double latitude, double longitude, String address, LocalDate date) throws IOException, InterruptedException {
        String url = String.format(METEO_API_URL, latitude, longitude);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode jsonNode = objectMapper.readTree(response.body()).get("daily");

        if (jsonNode == null) {
            throw new WeatherException("Weather data not found for the specified date and location.");
        }

        double tempMin = jsonNode.get("temperature_2m_min").get(0).asDouble();
        double tempMax = jsonNode.get("temperature_2m_max").get(0).asDouble();
        // Assure-toi que le code météo correspond bien au format attendu dans ton API
        int weatherCode = 800; // Exemple par défaut, si l'API ne fournit pas un code spécifique

        return new WeatherObject(address, date, weatherCode, tempMin, tempMax);
    }
}
