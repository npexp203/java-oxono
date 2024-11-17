package main;

import controller.AsciiController;
import command.CommandManager;
import model.AsciiPaint;

/**
 * Entry point for the AsciiPaint application.
 */
public class Main {
    public static void main(String[] args) {
        // Initialize the model and command manager
        AsciiPaint asciiPaint = new AsciiPaint();
        CommandManager commandManager = new CommandManager();

        // Initialize the controller and start the application
        AsciiController controller = new AsciiController(asciiPaint, commandManager);
        controller.start();
    }
}
