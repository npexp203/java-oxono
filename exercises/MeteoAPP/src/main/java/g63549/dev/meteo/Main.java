package g63549.dev.meteo;

import g63549.dev.meteo.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class to launch the Weather application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();

        Scene scene = new Scene(mainView, 500, 600);  // Définir la taille de la fenêtre
        primaryStage.setScene(scene);
        primaryStage.setTitle("Weather App");
        primaryStage.show();
    }

    /**
     * Main entry point of the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);  // Lance l'application JavaFX
    }
}
