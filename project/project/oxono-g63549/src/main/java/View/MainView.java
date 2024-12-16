package View;
import Controller.*;

import javafx.scene.layout.BorderPane;

public class MainView extends BorderPane {

    private MenuView menuView;
    private BoardView boardView;
    private InfoView infoView;

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


    public BoardView getBoardView() {
        return boardView;
    }

    public InfoView getInfoView() {
        return infoView;
    }
}

