package Controller;

import Util.Command;
import model.*;

import java.util.Scanner;

/**
 * Console controller for the game.
 */
public class ControllerConsole {

    private final GameModel gameModel;
    private final Scanner scanner;

    /**
     * Initializes the console controller.
     */
    public ControllerConsole() {
        this.gameModel = new GameModel();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts and manages the game loop.
     */
    public void playGame() {
        CommandManager commandManager = new CommandManager();
        System.out.println("Welcome to Oxono!");
        gameModel.startGame();

        while (!gameModel.isGameOver()) {
            displayBoard();
            Player currentPlayer = gameModel.getCurrentPlayer();
            System.out.println("Current Player: " + currentPlayer.getColor());

            if (currentPlayer.isAutomated()) {
                gameModel.executeAutomaticMove();
                gameModel.endTurn();
            } else {
                if (!handleTotemMovementOrForfeit(commandManager)) {
                    return;
                }

                displayBoard();
                handleTokenPlacement(commandManager);
                displayBoard();
                handleEndTurnOptions(commandManager);
            }
        }

        concludeGame();
    }

    /**
     * Handles the movement of a totem or the forfeit action.
     * Allows the player to choose between moving a totem or forfeiting the game.
     *
     * @param commandManager The command manager to execute commands.
     * @return true if the totem was moved, false if the game was forfeited.
     */
    private boolean handleTotemMovementOrForfeit(CommandManager commandManager) {
        boolean totemMoved = false;
        while (!totemMoved && !gameModel.isGameOver()) {
            System.out.println("Choose an action:");
            System.out.println("1. Move Totem");
            System.out.println("2. Forfeit Game");

            int choice = getUserInput("Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> {
                        Symbol totemSymbol = getUserSymbol();
                        Position targetTotem = getUserPosition("Enter the target position for the totem:");
                        Position oldPosition = gameModel.getTotemPosition(totemSymbol);
                        Command moveCommand = new MoveTotemCommand(gameModel, totemSymbol, oldPosition, targetTotem);
                        commandManager.executeCommand(moveCommand);
                        totemMoved = true;
                    }
                    case 2 -> {
                        Command forfeitCommand = new ForfeitCommand(gameModel);
                        commandManager.executeCommand(forfeitCommand);
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

    /**
     * Handles the token placement process.
     *
     * @param commandManager The command manager to execute commands.
     */
    private void handleTokenPlacement(CommandManager commandManager) {
        boolean tokenPlaced = false;
        while (!tokenPlaced && !gameModel.isGameOver()) {
            System.out.println("Place a token:");
            Symbol tokenSymbol = getUserSymbol();
            Position targetToken = getUserPosition("Enter the target position for the token:");

            try {
                Command placeCommand = new PlaceTokenCommand(gameModel, targetToken, tokenSymbol);
                commandManager.executeCommand(placeCommand);
                tokenPlaced = true;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Handles options at the end of the player's turn.
     *
     * @param commandManager The command manager to execute commands.
     */
    private void handleEndTurnOptions(CommandManager commandManager) {
        boolean endTurn = false;
        while (!endTurn && !gameModel.isGameOver()) {
            System.out.println("Choose an action:");
            System.out.println("0. End Turn");
            System.out.println("1. Undo");
            System.out.println("2. Redo");
            System.out.println("3. Forfeit Game");

            int choice = getUserInput("Enter your choice: ");
            try {
                switch (choice) {
                    case 0 -> {
                        System.out.println(gameModel.getCurrentPlayer().getColor() + " has ended their turn.");
                        endTurn = true;
                        gameModel.endTurn();
                    }
                    case 1 -> {
                        commandManager.undo();
                        System.out.println("Action undone.");
                        displayBoard();
                    }
                    case 2 -> {
                        commandManager.redo();
                        System.out.println("Action redone.");
                        displayBoard();
                    }
                    case 3 -> {
                        Command forfeitCommand = new ForfeitCommand(gameModel);
                        commandManager.executeCommand(forfeitCommand);
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Concludes the game and announces the result.
     */
    private void concludeGame() {
        System.out.println("Game over!");
        if (gameModel.checkWin()) {
            System.out.println("Winner: " + gameModel.getWinner().getColor());
        } else {
            System.out.println("It's a draw!");
        }
    }

    /**
     * Displays the current state of the board.
     */
    private void displayBoard() {
        Board board = gameModel.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Piece piece = board.getPiece(new Position(i, j));
                if (piece == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(piece.getSymbol() + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Gets user input as an integer.
     *
     * @param prompt The prompt message to display.
     * @return The integer input from the user.
     */
    private int getUserInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    /**
     * Gets the symbol input from the user.
     *
     * @return The symbol chosen by the user.
     */
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

    /**
     * Gets the position input from the user.
     *
     * @param prompt The prompt message to display.
     * @return The position entered by the user.
     */
    public Position getUserPosition(String prompt) {
        Scanner scannerLocal = new Scanner(System.in);
        int x, y;

        while (true) {
            try {
                System.out.println(prompt);
                System.out.print("Enter row (x): ");
                x = scannerLocal.nextInt();

                System.out.print("Enter column (y): ");
                y = scannerLocal.nextInt();

                return new Position(x, y);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter integers for x and y.");
                scannerLocal.nextLine();
            }
        }
    }
}