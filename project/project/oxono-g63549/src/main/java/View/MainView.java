// ===== MainView.java =====
package View;

import Controller.GameController;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;

/**
 * Modern MainView with improved layout and styling.
 */
public class MainView extends BorderPane {
    private MenuView menuView;
    private BoardView boardView;
    private InfoView infoView;

    public MainView(GameController controller) {
        this.menuView = new MenuView(controller);
        this.boardView = new BoardView(controller);
        this.infoView = new InfoView(controller);

        setupLayout();
        setupObservers(controller);
    }

    private void setupLayout() {
        setStyle("-fx-background-color: linear-gradient(to bottom, #edf2f7, #e2e8f0);");

        BorderPane.setMargin(menuView, new Insets(10));
        BorderPane.setMargin(boardView, new Insets(0, 10, 10, 10));
        BorderPane.setMargin(infoView, new Insets(0, 10, 10, 0));

        setTop(menuView);
        setCenter(boardView);
        setRight(infoView);
    }

    private void setupObservers(GameController controller) {
        controller.getModel().addObserver(boardView);
        controller.getModel().addObserver(infoView);
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public InfoView getInfoView() {
        return infoView;
    }
}