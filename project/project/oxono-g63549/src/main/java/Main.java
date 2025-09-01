
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Controller.GameController;
import Controller.ControllerConsole;
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
        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {

            ControllerConsole consoleController = new ControllerConsole();
            consoleController.playGame();
            
            System.out.println("\nMerci d'avoir joué à Oxono !");
        } else {
            Application.launch(Main.class, args);
        }
    }
}
