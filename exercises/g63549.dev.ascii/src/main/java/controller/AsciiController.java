package controller;

import model.AsciiPaint;
import model.Shape;
import view.AsciiView;
import model.*;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The AsciiController class is responsible for handling user inputs and coordinating
 * interactions between the model (AsciiPaint) and the view (AsciiView).
 */
public class AsciiController {

    private AsciiPaint model;
    private AsciiView view;
    private Scanner scanner;

    /**
     * Constructor for the AsciiController class.
     *
     * @param model The AsciiPaint model that contains the drawing data.
     * @param view  The AsciiView view used to display the drawing.
     */
    public AsciiController(AsciiPaint model, AsciiView view) {
        this.model = model;
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the controller and listens for user commands.
     */
    public void start() {
        String command;
        System.out.println("Enter commands to manipulate the drawing or 'quit' to exit.");

        while (true) {
            System.out.print("> ");
            command = scanner.nextLine().trim();

            if ("quit".equalsIgnoreCase(command)) {
                break;
            }

            processCommand(command);
        }

        scanner.close();
        System.out.println("Thank you for using AsciiPaint!");
    }

    /**
     * Processes a given command entered by the user.
     *
     * @param command The command entered by the user.
     */
    private void processCommand(String command) {
        if (command.startsWith("add circle")) {
            Pattern pattern = Pattern.compile("add circle (\\d+) (\\d+) (\\d+) (\\w)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int radius = Integer.parseInt(matcher.group(3));
                char color = matcher.group(4).charAt(0);
                model.addCircle(x, y, radius, color);
                System.out.println("Circle added.");
            } else {
                System.out.println("Invalid command. Usage: add circle <x> <y> <radius> <color>");
            }
        } else if (command.startsWith("add rectangle")) {
            Pattern pattern = Pattern.compile("add rectangle (\\d+) (\\d+) (\\d+) (\\d+) (\\w)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int width = Integer.parseInt(matcher.group(3));
                int height = Integer.parseInt(matcher.group(4));
                char color = matcher.group(5).charAt(0);
                model.addRectangle(x, y, width, height, color);
                System.out.println("Rectangle added.");
            } else {
                System.out.println("Invalid command. Usage: add rectangle <x> <y> <width> <height> <color>");
            }
        } else if (command.startsWith("add square")) {
            Pattern pattern = Pattern.compile("add square (\\d+) (\\d+) (\\d+) (\\w)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int side = Integer.parseInt(matcher.group(3));
                char color = matcher.group(4).charAt(0);
                model.addSquare(x, y, side, color);
                System.out.println("Square added.");
            } else {
                System.out.println("Invalid command. Usage: add square <x> <y> <side> <color>");
            }
        } else if (command.equalsIgnoreCase("list")) {
            listShapes();
        } else if (command.startsWith("move ")) {
            Pattern pattern = Pattern.compile("move (\\d+) (\\d+) (\\d+)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                int index = Integer.parseInt(matcher.group(1));
                int dx = Integer.parseInt(matcher.group(2));
                int dy = Integer.parseInt(matcher.group(3));
                if (model.getDrawing().getShapeAt(index) != null) {
                    model.getDrawing().getShapeAt(index).move(dx, dy);
                    System.out.println("Shape moved.");
                } else {
                    System.out.println("No shape found at the specified index.");
                }
            } else {
                System.out.println("Invalid command. Usage: move <index> <dx> <dy>");
            }
        } else if (command.startsWith("color ")) {
            Pattern pattern = Pattern.compile("color (\\d+) (\\w)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                int index = Integer.parseInt(matcher.group(1));
                char color = matcher.group(2).charAt(0);
                if (model.getDrawing().getShapeAt(index) != null) {
                    model.getDrawing().getShapeAt(index).setColor(color);
                    System.out.println("Shape color updated.");
                } else {
                    System.out.println("No shape found at the specified index.");
                }
            } else {
                System.out.println("Invalid command. Usage: color <index> <color>");
            }
        } else if (command.startsWith("delete ")) {
            Pattern pattern = Pattern.compile("delete (\\d+)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.find()) {
                int index = Integer.parseInt(matcher.group(1));
                boolean removed = model.getDrawing().removeShape(index);
                if (removed) {
                    System.out.println("Shape deleted.");
                } else {
                    System.out.println("No shape found at the specified index.");
                }
            } else {
                System.out.println("Invalid command. Usage: delete <index>");
            }
        } else if ("show".equalsIgnoreCase(command)) {
            view.display(model.getDrawing());
        } else {
            System.out.println("Unknown command. Available commands: add circle, add rectangle, add square, move, color, delete, list, show, quit");
        }
    }

    /**
     * Lists all shapes in the drawing.
     */
    private void listShapes() {
        Drawing drawing = model.getDrawing();
        if (drawing.getShapes().isEmpty()) {
            System.out.println("No shapes in the drawing.");
        } else {
            for (int i = 0; i < drawing.getShapes().size(); i++) {
                Shape shape = drawing.getShapes().get(i);
                String shapeType = shape.getClass().getSimpleName();
                System.out.println(i + ": " + shapeType + " (Color: " + shape.getColor() + ")");
            }
        }
    }

    /**
     * Main method to start the AsciiPaint application with the controller.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        AsciiPaint paint = new AsciiPaint(50, 30); 
        AsciiView view = new AsciiView();
        AsciiController controller = new AsciiController(paint, view);
        controller.start();
    }
}
