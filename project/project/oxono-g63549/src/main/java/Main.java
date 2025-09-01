
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
        // Vérifier si l'utilisateur veut lancer la version console
        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
            System.out.println("=== OXONO - VERSION CONSOLE ===");
            System.out.println("Démarrage du jeu...\n");
            
            ControllerConsole consoleController = new ControllerConsole();
            consoleController.playGame();
            
            System.out.println("\nMerci d'avoir joué à Oxono !");
        } else {
            // Version JavaFX par défaut
            Application.launch(Main.class, args);
        }
    }
}
