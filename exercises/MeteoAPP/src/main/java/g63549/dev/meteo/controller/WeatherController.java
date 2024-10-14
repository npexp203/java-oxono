package g63549.dev.meteo.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import g63549.dev.meteo.model.WeatherData;

import java.time.LocalDate;

public class WeatherController {

    private static final String OSM_API_URL = "https://nominatim.openstreetmap.org/search.php?q=";
    private static final String OSM_API_FORMAT = "&format=jsonv2";
    private static final String METEO_API_URL = "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&daily=temperature_2m_min,temperature_2m_max&timezone=Europe/Brussels";

    public double[] getCoordinates(String city) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String url = OSM_API_URL + city + OSM_API_FORMAT;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))   // Spécifie l'URL de la requête
                    .GET()  // Méthode GET
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Utiliser ObjectMapper pour lire le JSON
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());

                // Extraire la latitude et la longitude du premier résultat
                if (root.isArray() && root.size() > 0) {
                    double latitude = root.get(0).get("lat").asDouble();
                    double longitude = root.get(0).get("lon").asDouble();
                    return new double[]{latitude, longitude};
                } else {
                    System.out.println("Ville non trouvée !");
                }
            } else {
                System.out.println("Erreur lors de la requête : " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public WeatherData getWeatherData(double latitude, double longitude, LocalDate date) {
        try {
            String url = String.format(METEO_API_URL, latitude, longitude);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());

                if (root.has("daily") && root.get("daily").has("temperature_2m_min") && root.get("daily").has("temperature_2m_max")) {
                    double temperatureMin = root.get("daily").get("temperature_2m_min").get(0).asDouble();
                    double temperatureMax = root.get("daily").get("temperature_2m_max").get(0).asDouble();
                    // Remplir les données météo
                    WeatherData weatherData = new WeatherData();
                    weatherData.setMinTemp(temperatureMin);
                    weatherData.setMaxTemp(temperatureMax);
                    weatherData.setWeatherDescription("Prévisions météo obtenues avec succès !");
                    return weatherData;
                } else {
                    System.out.println("Erreur lors de la récupération des données météo");
                }
            } else {
                System.out.println("Erreur lors de la requête : " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si erreur ou données non trouvées
    }
}
