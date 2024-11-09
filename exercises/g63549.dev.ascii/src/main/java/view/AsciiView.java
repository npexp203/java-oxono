package view;

import model.AsciiPaint;
import model.Drawing;
import model.Circle;
import model.Rectangle;
import model.Shape;

import java.util.Arrays;

/**
 * The AsciiView class is responsible for displaying the current state of the AsciiPaint drawing.
 * It provides a method to visualize the drawing using ASCII characters.
 */
public class    AsciiView {

    /**
     * Displays the current state of the drawing on the console.
     * The canvas is represented by a 2D array of characters.
     *
     * @param drawing The Drawing object that contains all shapes to be visualized.
     */
    public void display(Drawing drawing) {
        int width = drawing.getWidth();
        int height = drawing.getHeight();
        char[][] canvas = new char[height][width];

        // Initialize canvas with ' ' (space)
        for (char[] row : canvas) {
            Arrays.fill(row, ' ');
        }

        // Place shapes on the canvas
        for (Shape shape : drawing.getShapes()) {
            if (shape instanceof Circle) {
                Circle circle = (Circle) shape;
                int centerX = (int) circle.getCenter().getX();
                int centerY = (int) circle.getCenter().getY();
                int radius = (int) circle.getRadius();
                char color = circle.getColor();

                // Draw the filled circle on the canvas
                for (int y = -radius; y <= radius; y++) {
                    for (int x = -radius; x <= radius; x++) {
                        // Check if the point (x, y) is inside the circle using an approximation
                        if (x * x + y * y <= radius * radius) {
                            int drawX = centerX + x;
                            int drawY = centerY + y;
                            if (drawX >= 0 && drawX < width && drawY >= 0 && drawY < height) {
                                canvas[drawY][drawX] = color;
                            }
                        }
                    }
                }
            } else if (shape instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) shape;
                int upperLeftX = (int) rectangle.getUpperLeft().getX();
                int upperLeftY = (int) rectangle.getUpperLeft().getY();
                int rectWidth = (int) rectangle.getWidth();
                int rectHeight = (int) rectangle.getHeight();
                char color = rectangle.getColor();

                // Draw the rectangle on the canvas, with bounds checking
                for (int y = 0; y < rectHeight; y++) {
                    for (int x = 0; x < rectWidth; x++) {
                        int drawX = upperLeftX + x;
                        int drawY = upperLeftY + y;

                        // Ensure the coordinates are within the canvas bounds
                        if (drawX >= 0 && drawX < width && drawY >= 0 && drawY < height) {
                            canvas[drawY][drawX] = color;
                        }
                    }
                }
            }
        }

        // Print the canvas
        for (char[] row : canvas) {
            System.out.println(new String(row));
        }
    }

    /**
     * Main method to generate and display an example drawing.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        AsciiPaint paint = new AsciiPaint(50, 30);
        AsciiView view = new AsciiView();

        // Add shapes to test
        paint.addCircle(5, 3, 1, 'c');        // Test circle
        paint.addRectangle(10, 10, 5, 20, 'r'); // Test rectangle

        // Display the drawing
        view.display(paint.getDrawing());
    }
}
