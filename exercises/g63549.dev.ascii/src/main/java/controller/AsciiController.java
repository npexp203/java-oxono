package controller;

import command.CommandManager;
import model.AsciiPaint;
import model.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Controller for the AsciiPaint application.
 * Handles user inputs and delegates actions to the model and CommandManager.
 */
public class AsciiController {
    private final AsciiPaint asciiPaint;
    private final CommandManager commandManager;

    /**
     * Constructs the AsciiController with the model and command manager.
     * @param asciiPaint the AsciiPaint model
     * @param commandManager the command manager
     */
    public AsciiController(AsciiPaint asciiPaint, CommandManager commandManager) {
        this.asciiPaint = asciiPaint;
        this.commandManager = commandManager;
    }

    /**
     * Starts the application and handles user input.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to AsciiPaint!");
        String input;

        while (true) {
            System.out.print("Enter command: ");
            input = scanner.nextLine().trim();

            if (input.equals("exit")) {
                System.out.println("Exiting AsciiPaint. Goodbye!");
                break;
            }

            try {
                handleCommand(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    /**
     * Parses and handles user commands.
     * @param input the user command
     */
    private void handleCommand(String input) {
        String[] parts = input.split("\\s+");
        String command = parts[0];

        switch (command) {
            case "group":
                handleGroupCommand(parts);
                break;
            case "ungroup":
                handleUngroupCommand(parts);
                break;
            case "undo":
                commandManager.undo();
                System.out.println("Undo successful.");
                break;
            case "redo":
                commandManager.redo();
                System.out.println("Redo successful.");
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + command);
        }
    }

    /**
     * Handles the group command.
     * @param parts the command parts
     */
    private void handleGroupCommand(String[] parts) {
        if (parts.length < 2) {
            throw new IllegalArgumentException("Usage: group <index1> <index2> ...");
        }

        List<Integer> indices = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            indices.add(Integer.parseInt(parts[i]));
        }

        Group newGroup = asciiPaint.group(indices);
        System.out.println("Grouped shapes into a new group with ID: " + asciiPaint.getShapes().indexOf(newGroup));
    }

    /**
     * Handles the ungroup command.
     * @param parts the command parts
     */
    private void handleUngroupCommand(String[] parts) {
        if (parts.length != 2) {
            throw new IllegalArgumentException("Usage: ungroup <groupIndex>");
        }

        int groupIndex = Integer.parseInt(parts[1]);
        if (groupIndex < 0 || groupIndex >= asciiPaint.getShapes().size() ||
                !(asciiPaint.getShapes().get(groupIndex) instanceof Group)) {
            throw new IllegalArgumentException("Invalid group index.");
        }

        Group group = (Group) asciiPaint.getShapes().get(groupIndex);
        asciiPaint.ungroup(group);
        System.out.println("Ungrouped shapes from group with ID: " + groupIndex);
    }
}
