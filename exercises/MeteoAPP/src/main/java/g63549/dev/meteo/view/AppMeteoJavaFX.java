package g63549.dev.meteo.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.time.LocalDate;
import g63549.dev.meteo.controller.WeatherController;
import g63549.dev.meteo.model.WeatherData;

public class AppMeteoJavaFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Créer les composants
        Button actionButton = new Button("Get Weather");
        TextField textfield = new TextField();
        textfield.setPromptText("Enter city name");
        DatePicker datePicker = new DatePicker();

        // Layout horizontal pour les éléments
        HBox root = new HBox(10);
        root.setPadding(new Insets(15));

        // Ajouter les éléments à la boîte horizontale
        root.getChildren().addAll(textfield, datePicker, actionButton);

        // Label pour afficher les résultats
        Label resultLabel = new Label("Results will appear here");

        // BorderPane pour organiser les éléments
        BorderPane borderPane = new BorderPane();
        borderPane.setBottom(root);
        borderPane.setCenter(resultLabel);

        // Créer la scène et l'ajouter au stage
        Scene scene = new Scene(borderPane, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Meteo APP");
        stage.show();

        // Instancier le contrôleur
        WeatherController weatherController = new WeatherController();

        // Gérer l'action du bouton
        actionButton.setOnAction(e -> {
            // Obtenir la ville et la date depuis l'interface
            String city = textfield.getText();
            LocalDate date = datePicker.getValue();

            // Vérifier que la ville et la date sont valides
            if (city.isEmpty() || date == null) {
                resultLabel.setText("Please enter a city and select a date.");
                return;
            }

            // Appeler le contrôleur pour obtenir les coordonnées
            double[] coordinates = weatherController.getCoordinates(city);
            if (coordinates == null) {
                resultLabel.setText("City not found.");
                return;
            }

            // Appeler le contrôleur pour obtenir les données météo
            WeatherData weatherData = weatherController.getWeatherData(coordinates[0], coordinates[1], date);
            if (weatherData == null) {
                resultLabel.setText("Unable to fetch weather data.");
                return;
            }

            // Afficher les résultats dans le label
            resultLabel.setText("Temperature Min: " + weatherData.getMinTemp() +
                    ", Max: " + weatherData.getMaxTemp() +
                    ". " + weatherData.getWeatherDescription());
        });
    }

    public static void main(String[] args) {
        launch(args);  // Démarre l'application JavaFX
    }
}
