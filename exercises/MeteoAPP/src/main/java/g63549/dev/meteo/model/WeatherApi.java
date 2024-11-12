package g63549.dev.meteo.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class WeatherApi {

    static WeatherObject fetch(String address, LocalDate date) throws IOException, InterruptedException {
        double[] coordinates = fetchCoordinates(address);
        double latitude = coordinates[0];
        double longitude = coordinates[1];

        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                "&longitude=" + longitude +
                "&daily=weather_code,temperature_2m_max,temperature_2m_min" +
                "&timezone=Europe%2FBerlin&start_date=" + date + "&end_date=" + date;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body()).get("daily");

        int weatherCode = jsonNode.get("weather_code").get(0).asInt();
        double tempMin = jsonNode.get("temperature_2m_min").get(0).asDouble();
        double tempMax = jsonNode.get("temperature_2m_max").get(0).asDouble();

        return new WeatherObject(address, date, weatherCode, tempMin, tempMax);
    }

    private static double[] fetchCoordinates(String address) throws IOException, InterruptedException {
        String url = "https://nominatim.openstreetmap.org/search.php?q=" + URLEncoder.encode(address, StandardCharsets.UTF_8) + "&format=jsonv2";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());

        double latitude = jsonNode.get(0).get("lat").asDouble();
        double longitude = jsonNode.get(0).get("lon").asDouble();
        return new double[] { latitude, longitude };
    }
}
