package View;

import Controller.GameController;
import Util.Observer;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import model.Position;
import model.Player;


public class MainView extends BorderPane implements Observer {
    private MenuView menuView;
    private BoardView boardView;
    private InfoView infoView;
    private GameController controller;

    /**
     * Creates a new MainView with the specified controller.
     * 
     * @param controller the game controller to use
     * @throws IllegalArgumentException if controller is null
     */
    public MainView(GameController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("Controller cannot be null");
        }
        this.controller = controller;
        this.menuView = new MenuView(controller);
        this.boardView = new BoardView(controller);
        this.infoView = new InfoView(controller);

        setupLayout();
        setupObservers(controller);
    }

    /**
     * Initializes the game board with the specified size.
     * 
     * @param size the size of the board to initialize
     */
    public void initBoard(int size) {
        boardView.initBoard(size);
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

    /**
     * Gets the board view component.
     * 
     * @return the board view
     */
    public BoardView getBoardView() {
        return boardView;
    }

    /**
     * Gets the info view component.
     * 
     * @return the info view
     */
    public InfoView getInfoView() {
        return infoView;
    }

    @Override
    public void update(String event, Object value) {
        if (shouldUpdateBoard(event)) {
            boardView.updateBoard(controller.getModel());
        }
        
        if (isGameEndEvent(event)) {
            handleGameEnd(event, value);
        }
        
        if (event.equals("GAME_STARTED")) {
            infoView.handleGameStarted();
        }
    }

    private boolean shouldUpdateBoard(String event) {
        return switch (event) {
            case "GAME_STARTED", "TOTEM_SELECTED", "TOTEM_DESELECTED", 
                 "TOTEM_MOVED", "TOKEN_PLACED", "CURRENT_PLAYER_CHANGED", 
                 "PHASE_CHANGED" -> true;
            default -> false;
        };
    }

    private boolean isGameEndEvent(String event) {
        return switch (event) {
            case "GAME_WON", "GAME_DRAW", "GAME_FORFEITED" -> true;
            default -> false;
        };
    }

    private void handleGameEnd(String event, Object value) {
        switch (event) {
            case "GAME_WON" -> {
                Player winner = (Player) value;
                infoView.displayWinner(winner);
            }
            case "GAME_DRAW" -> infoView.displayDraw();
            case "GAME_FORFEITED" -> {
                Player forfeiter = (Player) value;
                infoView.handleGameOver();
            }
        }
    }
}

