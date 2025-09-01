
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Controller.GameController;
import View.MainView;
import model.GameModel;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameModel model = new GameModel();
        GameController controller = new GameController(model);
        MainView mainView = new MainView(controller);
        controller.setView(mainView);

        Scene scene = new Scene(mainView, 800,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Oxono");
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
