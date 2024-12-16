package View;

import Controller.GameController;
import javafx.scene.layout.BorderPane;

/**
 * MainView is the primary view that integrates MenuView, BoardView, and InfoView.
 * It extends BorderPane to arrange the components in a structured manner.
 */
public class MainView extends BorderPane {

    private MenuView menuView;
    private BoardView boardView;
    private InfoView infoView;

    /**
     * Constructs the MainView with the given GameController.
     * Initializes MenuView, BoardView, and InfoView and sets their positions.
     * 
     * @param controller The GameController used to manage the game state.
     */
    public MainView(GameController controller) {
        this.menuView = new MenuView(controller);
        this.boardView = new BoardView(controller);
        this.infoView = new InfoView(controller);

        setTop(menuView);
        setCenter(boardView);
        setRight(infoView);

        controller.getModel().addObserver(boardView);
        controller.getModel().addObserver(infoView);
    }

    /**
     * Returns the BoardView component.
     * 
     * @return The BoardView used in the MainView.
     */
    public BoardView getBoardView() {
        return boardView;
    }

    /**
     * Returns the InfoView component.
     * 
     * @return The InfoView used in the MainView.
     */
    public InfoView getInfoView() {
        return infoView;
    }
}