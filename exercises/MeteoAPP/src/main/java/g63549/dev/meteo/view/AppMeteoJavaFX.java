package g63549.dev.meteo.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

        // Créer l'ImageView pour afficher les images météo
        ImageView weatherImageView = new ImageView();
        weatherImageView.setFitHeight(200);  // Augmenter la taille de l'image
        weatherImageView.setFitWidth(200);   // Augmenter la taille de l'image

        // Créer le HBox pour les entrées utilisateur
        HBox inputBox = new HBox(10);
        inputBox.setPadding(new Insets(15));
        inputBox.setAlignment(Pos.CENTER);  // Centrer le HBox
        inputBox.getChildren().addAll(textfield, datePicker, actionButton);

        // Créer un VBox pour centrer l'image et le label au centre de la fenêtre
        VBox vBox = new VBox(20);  // Espace de 20px entre les éléments
        vBox.setAlignment(Pos.CENTER);  // Centrer le contenu verticalement
        Label resultLabel = new Label("Results will appear here");
        vBox.getChildren().addAll(weatherImageView, resultLabel);

        // Créer la structure BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);  // Mettre l'image et le label au centre
        borderPane.setBottom(inputBox);  // Mettre les entrées utilisateur en bas

        // Créer la scène
        Scene scene = new Scene(borderPane, 500, 600);  // Augmenter la taille de la fenêtre
        stage.setScene(scene);
        stage.setTitle("Meteo APP");
        stage.show();

        // Instancier le contrôleur
        WeatherController weatherController = new WeatherController();

        actionButton.setOnAction(e -> {
            String city = textfield.getText();
            LocalDate date = datePicker.getValue();

            if (city.isEmpty() || date == null) {
                resultLabel.setText("Please enter a city and select a date.");
                return;
            }

            double[] coordinates = weatherController.getCoordinates(city);
            if (coordinates == null) {
                resultLabel.setText("City not found.");
                return;
            }

            WeatherData weatherData = weatherController.getWeatherData(coordinates[0], coordinates[1], date);
            if (weatherData == null) {
                resultLabel.setText("Unable to fetch weather data.");
                return;
            }

            // Afficher les résultats dans le label
            resultLabel.setText("Temperature Min: " + weatherData.getMinTemp() +
                    ", Max: " + weatherData.getMaxTemp() +
                    ". " + weatherData.getWeatherDescription());

            // Étape de débogage : afficher la description météo dans la console
            System.out.println("Description météo (raw) : " + weatherData.getWeatherDescription());

            // Tester la logique pour changer l'image
            String description = weatherData.getWeatherDescription().toLowerCase();
            System.out.println("Description météo (lowercase) : " + description);

            Image weatherImage = null;

            if (description.contains("rain")) {
                System.out.println("Condition : rain");
                weatherImage = new Image(getClass().getResource("/rainy.png").toExternalForm());
            } else if (description.contains("sunny") || description.contains("clear")) {
                System.out.println("Condition : sunny or clear");   
                weatherImage = new Image(getClass().getResource("/sunny.png").toExternalForm());
            } else if (description.contains("cloud") || description.contains("overcast")) {
                System.out.println("Condition : cloud or overcast");
                weatherImage = new Image(getClass().getResource("/cloudy.png").toExternalForm());
            } else {
                System.out.println("Aucune condition trouvée.");
                weatherImageView.setImage(null);
            }

            if (weatherImage != null) {
                weatherImageView.setImage(weatherImage);
            }
        });



    }

}
