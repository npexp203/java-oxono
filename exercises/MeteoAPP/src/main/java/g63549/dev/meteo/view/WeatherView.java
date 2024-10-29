package g63549.dev.meteo.view;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class WeatherView extends VBox {

    private final Label localityLabel = new Label();
    private final Label tempMinLabel = new Label();
    private final Label tempMaxLabel = new Label();
    private final ImageView weatherImageView = new ImageView();

    public WeatherView() {
        this.getChildren().addAll(localityLabel, tempMinLabel, tempMaxLabel, weatherImageView);
    }

    public void setLocality(String locality) {
        localityLabel.setText("Locality: " + locality);
    }

    public void setTempMin(double tempMin) {
        tempMinLabel.setText("Min Temp: " + tempMin + "°C");
    }

    public void setTempMax(double tempMax) {
        tempMaxLabel.setText("Max Temp: " + tempMax + "°C");
    }

    public void setImage(int weatherCode) {
        String imagePath = "/images/weather_" + weatherCode + ".png";
        weatherImageView.setImage(new Image(imagePath));
    }
}
