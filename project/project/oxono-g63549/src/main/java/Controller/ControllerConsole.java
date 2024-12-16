package Controller;

import Util.Command;
import model.*;

import java.util.Scanner;

public class ControllerConsole {

    private final GameModel gameModel;
    private final Scanner scanner;

    public ControllerConsole() {
        this.gameModel = new GameModel();
        this.scanner = new Scanner(System.in);
    }

    public void playGame() {
        CommandManager commandManager = new CommandManager();
        System.out.println("Welcome to Oxono!");
        gameModel.startGame();

        while (!gameModel.isGameOver()) {
            displayBoard();
            Player currentPlayer = gameModel.getCurrentPlayer();
            System.out.println("Current Player: " + currentPlayer.getColor());

            // Si le joueur est automatisé, on exécute directement son move.
            if (currentPlayer.isAutomated()) {
                gameModel.executeAutomaticMove();
                // Puis on termine le tour
                gameModel.endTurn();
            } else {
                // Joueur humain
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
                        return; // Game forfeited
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void concludeGame() {
        System.out.println("Game over!");
        if (gameModel.checkWin()) {
            System.out.println("Winner: " + gameModel.getWinner().getColor());
        } else {
            System.out.println("It's a draw!");
        }
    }

    private void displayBoard() {
        Board board = gameModel.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                Piece piece = board.getPiece(new Position(i, j));
                if (piece == null) {
                    System.out.print(". ");
                } else if (piece instanceof Totem) {
                    System.out.print(piece.getSymbol() + " ");
                } else {
                    System.out.print(piece.getSymbol() + " ");
                }
            }
            System.out.println();
        }
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
