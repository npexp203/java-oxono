package model;

import command.CommandManager;

import java.util.List;

public class AsciiPaint {
    private Drawing drawing;
    private CommandManager commandManager = new CommandManager();


    /**
     * Constructor for AsciiPaint.
     * Initializes a new Drawing object with specified width and height.
     *
     * @param width  The width of the drawing.
     * @param height The height of the drawing.
     */
    public AsciiPaint(int width, int height) {
        this.drawing = new Drawing(width, height);
    }

    /**
     * Adds a circle to the drawing.
     *
     * @param x      The x-coordinate of the circle's center.
     * @param y      The y-coordinate of the circle's center.
     * @param radius The radius of the circle.
     * @param color  The character representing the color of the circle.
     */
    public void addCircle(int x, int y, double radius, char color) {
        Circle circle = new Circle(new Point(x, y), radius, color);
        AddCommand addCommand = new AddCommand(drawing, circle);
        commandManager.doCommand(addCommand);
    }

    /**
     * Adds a rectangle to the drawing.
     *
     * @param x      The x-coordinate of the upper-left corner.
     * @param y      The y-coordinate of the upper-left corner.
     * @param width  The width of the rectangle.
     * @param height The height of the rectangle.
     * @param color  The character representing the color of the rectangle.
     */
    public void addRectangle(int x, int y, double width, double height, char color) {
        Rectangle rectangle = new Rectangle(new Point(x, y), width, height, color);
        drawing.addShape(rectangle);
    }

    /**
     * Adds a square to the drawing.
     *
     * @param x     The x-coordinate of the upper-left corner.
     * @param y     The y-coordinate of the upper-left corner.
     * @param side  The length of the side of the square.
     * @param color The character representing the color of the square.
     */
    public void addSquare(int x, int y, double side, char color) {
        Square square = new Square(new Point(x, y), side, color);
        drawing.addShape(square);
    }

    /**
     * Moves a shape located at a specified point.
     *
     * @param x  The x-coordinate of the point.
     * @param y  The y-coordinate of the point.
     * @param dx The horizontal distance to move the shape.
     * @param dy The vertical distance to move the shape.
     */
    public void moveShape(int x, int y, double dx, double dy) {
        Shape shape = drawing.getShapeAt(new Point(x, y));
        if (shape != null) {
            shape.move(dx, dy);
        }
    }

    public void removeShape(int x, int y) {
        Point point = new Point(x, y);

        Shape shapeToRemove = drawing.getShapeAt(point);

        if (shapeToRemove != null) {
            List<Shape> shapes = drawing.getShapes();
            int index = -1;
            for (int i = 0; i < shapes.size(); i++) {
                if (shapes.get(i) == shapeToRemove) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                DeleteCommand deleteCommand = new DeleteCommand(drawing, index);
                commandManager.doCommand(deleteCommand);
            }
        }
    }




    // Getters
    public Drawing getDrawing() {
        return drawing;
    }
}