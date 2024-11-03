package g63549.dev.meteo.view;

import g63549.dev.meteo.model.WeatherObject;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

/**
 * WeatherView displays the weather information including temperature
 * and weather icon based on weather code.
 */
public class WeatherView extends VBox {
    private final Label resultLabel;
    private final ImageView weatherImageView;

    public WeatherView() {
        resultLabel = new Label("Results will appear here");
        weatherImageView = new ImageView();
        weatherImageView.setFitHeight(200);
        weatherImageView.setFitWidth(200);

        this.getChildren().addAll(weatherImageView, resultLabel);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
    }

    /**
     * Sets a message to be displayed in the result label.
     * Useful for displaying errors or notifications.
     *
     * @param text the text to display as a message
     */
    public void setResultText(String text) {
        resultLabel.setText(text);
    }

    /**
     * Displays the weather data in the view.
     *
     * @param weatherObject the weather data to display
     */
    public void displayWeather(WeatherObject weatherObject) {
        resultLabel.setText("Temperature Min: " + weatherObject.tempMin() +
                ", Max: " + weatherObject.tempMax());

        int weatherCode = weatherObject.weatherCode();
        Image weatherImage = getImageForWeatherCode(weatherCode);
        if (weatherImage == null) {
            System.out.println("Image non trouvée pour le code météo : " + weatherCode);
        } else {
            weatherImageView.setImage(weatherImage);
        }

        if (weatherImage != null) {
            weatherImageView.setImage(weatherImage);
        }
    }

    /**
     * Returns the appropriate weather image based on the weather code.
     *
     * @param weatherCode the weather code from the API
     * @return Image corresponding to the weather code
     */
    private Image getImageForWeatherCode(int weatherCode) {
        String imagePath;
        switch (weatherCode) {
            case 1:
                imagePath = "/sunny.png";
                break;
            case 2:
            case 3:
                imagePath = "/cloudy.png";
                break;
            case 45:
            case 48:
                imagePath = "/foggy.png";
                break;
            case 61:
            case 63:
            case 65:
                imagePath = "/rainy.png";
                break;
            case 71:
            case 73:
            case 75:
                imagePath = "/snowy.png";
                break;
            default:
                imagePath = null;
                break;
        }
        return (imagePath != null) ? new Image(getClass().getResource(imagePath).toExternalForm()) : null;
    }
}
