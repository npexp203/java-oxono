package Controller;

import Util.Command;
import Util.Observer;
import model.*;
import java.util.Scanner;

public class ControllerConsole implements Observer {

    private final GameModel gameModel;
    private final Scanner scanner;
    private final CommandManager commandManager;

    public ControllerConsole() {
        this.gameModel = new GameModel();
        this.scanner = new Scanner(System.in);
        this.commandManager = new CommandManager();

        gameModel.addObserver(this);
    }


    public void playGame() {
        System.out.println("Welcome to Oxono!");

        try {
            gameModel.startGame();

            while (!gameModel.isGameOver()) {
                displayBoard();
                handlePlayerTurn();
            }

        } catch (Exception e) {
            System.err.println("Game error: " + e.getMessage());
        } finally {
            concludeGame();
        }
    }


    private void handlePlayerTurn() {
        Player currentPlayer = gameModel.getCurrentPlayer();
        System.out.println("\nCurrent Player: " + currentPlayer.getColor());
        System.out.println("Phase: " + gameModel.getCurrentPhase());

        if (gameModel.isCurrentPlayerBot()) {
            handleAITurn();
        } else {
            handleHumanTurn();
        }
    }

    private void handleAITurn() {
        System.out.println("AI is thinking...");
        gameModel.executeAutomaticMove(commandManager);
    }

    private void handleHumanTurn() {
        if (gameModel.isTotemMovementPhase()) {
            if (!handleTotemMovementOrForfeit()) {
                return;
            }
        }

        if (gameModel.isTokenPlacementPhase()) {
            handleTokenPlacement();
        }

        if (!gameModel.isGameOver()) {
            handleEndTurnOptions();
        }
    }


    private boolean handleTotemMovementOrForfeit() {
        while (gameModel.isTotemMovementPhase() && !gameModel.isGameOver()) {
            System.out.println("\nChoose an action:");
            System.out.println("1. Move Totem");
            System.out.println("2. Forfeit Game");

            int choice = getUserInput("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> {
                        return attemptTotemMove();
                    }
                    case 2 -> {
                        executeCommand(new ForfeitCommand(gameModel));
                        return false;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return true;
    }

    private boolean attemptTotemMove() {
        Symbol totemSymbol = getUserSymbol();
        Position targetPosition = getUserPosition("Enter the target position for the totem:");
        Position currentPosition = gameModel.getTotemPosition(totemSymbol);

        try {
            Command moveCommand = new MoveTotemCommand(gameModel, totemSymbol, currentPosition, targetPosition);
            executeCommand(moveCommand);
            return true;
        } catch (Exception e) {
            System.out.println("Move failed: " + e.getMessage());
            return false;
        }
    }

    private void handleTokenPlacement() {
        while (gameModel.isTokenPlacementPhase() && !gameModel.isGameOver()) {
            System.out.println("\nPlace a token:");

            Symbol tokenSymbol = gameModel.getLastMovedSymbol();
            if (tokenSymbol == null) {
                tokenSymbol = getUserSymbol();
            } else {
                System.out.println("Placing " + tokenSymbol + " token (from moved totem)");
            }

            Position targetPosition = getUserPosition("Enter the target position for the token:");

            try {
                Command placeCommand = new PlaceTokenCommand(gameModel, targetPosition, tokenSymbol);
                executeCommand(placeCommand);
                break;
            } catch (Exception e) {
                System.out.println("Token placement failed: " + e.getMessage());
            }
        }
    }

    private void handleEndTurnOptions() {
        boolean endTurn = false;

        while (!endTurn && !gameModel.isGameOver()) {
            System.out.println("\nChoose an action:");
            System.out.println("0. End Turn");
            System.out.println("1. Undo");
            System.out.println("2. Redo");
            System.out.println("3. Forfeit Game");

            int choice = getUserInput("Enter your choice: ");

            try {
                switch (choice) {
                    case 0 -> {
                        System.out.println(gameModel.getCurrentPlayer().getColor() + " ended their turn.");
                        gameModel.endTurn();
                        endTurn = true;
                    }
                    case 1 -> {
                        undoAction();
                    }
                    case 2 -> {
                        redoAction();
                    }
                    case 3 -> {
                        executeCommand(new ForfeitCommand(gameModel));
                        endTurn = true;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    private void executeCommand(Command command) {
        commandManager.executeCommand(command);
    }

    private void undoAction() {
        if (commandManager.canUndo()) {
            commandManager.undo();
            System.out.println("Action undone.");
            displayBoard();
        } else {
            System.out.println("Nothing to undo.");
        }
    }

    private void redoAction() {
        if (commandManager.canRedo()) {
            commandManager.redo();
            System.out.println("Action redone.");
            displayBoard();
        } else {
            System.out.println("Nothing to redo.");
        }
    }


    private void displayBoard() {
        Board board = gameModel.getBoard();
        int boardSize = gameModel.getBoardSize();

        System.out.println("\nCurrent Board:");
        System.out.println("   " + "0 1 2 3 4 5".substring(0, Math.min(11, boardSize * 2 - 1)));

        for (int i = 0; i < boardSize; i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < boardSize; j++) {
                Piece piece = gameModel.getPieceAt(new Position(i, j));
                if (piece == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(piece.getSymbol() + " ");
                }
            }
            System.out.println();
        }

        displayGameInfo();
    }

    private void displayGameInfo() {
        System.out.println("\nGame Info:");
        System.out.println("Current Player: " + gameModel.getCurrentPlayerColor());
        System.out.println("Phase: " + gameModel.getCurrentPhase());

        if (gameModel.hasSelectedTotem()) {
            System.out.println("Selected Totem at: " + gameModel.getSelectedTotemPosition());
        }

        int xTokens = gameModel.getRemainingTokens(Symbol.X);
        int oTokens = gameModel.getRemainingTokens(Symbol.O);
        System.out.println("Remaining tokens - X: " + xTokens + ", O: " + oTokens);
    }

    private void concludeGame() {
        System.out.println("\nGame Over!");

        if (gameModel.isGameOver()) {
            Player winner = gameModel.getWinner();
            if (winner != null) {
                System.out.println("Winner: " + winner.getColor());
            } else {
                System.out.println("It's a draw!");
            }
        }

        displayBoard();
    }


    private int getUserInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private Symbol getUserSymbol() {
        System.out.print("Enter the symbol (X or O): ");
        while (true) {
            String input = scanner.next().toUpperCase();
            if (input.equals("X")) {
                return Symbol.X;
            } else if (input.equals("O")) {
                return Symbol.O;
            } else {
                System.out.println("Invalid symbol. Please enter X or O.");
            }
        }
    }

    private Position getUserPosition(String prompt) {
        int x, y;

        while (true) {
            try {
                System.out.println(prompt);
                System.out.print("Enter row (x): ");
                x = scanner.nextInt();

                System.out.print("Enter column (y): ");
                y = scanner.nextInt();

                Position pos = new Position(x, y);

                if (!gameModel.getBoard().isValidCoordinate(pos)) {
                    System.out.println("Invalid position. Try again.");
                    continue;
                }

                return pos;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter integers for x and y.");
                scanner.nextLine();
            }
        }
    }


    @Override
    public void update(String event, Object value) {
        switch (event) {
            case "GAME_WON":
                System.out.println("\n*** GAME WON by " + ((Player) value).getColor() + " ***");
                break;
            case "GAME_DRAW":
                System.out.println("\n*** GAME DRAW ***");
                break;
            case "GAME_FORFEITED":
                System.out.println("\n*** GAME FORFEITED - Winner: " + ((Player) value).getColor() + " ***");
                break;
            case "TOTEM_MOVED":
                System.out.println("Totem moved to " + value);
                break;
            case "TOKEN_PLACED":
                System.out.println("Token placed at " + value);
                break;
        }
    }
}