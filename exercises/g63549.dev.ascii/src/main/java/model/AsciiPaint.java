package model;

public class AsciiPaint {
    private Drawing drawing;

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
        drawing.addShape(circle);
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

    // Method to remove a shape at a specified point
    public void removeShape(int x, int y) {
        Shape shape = drawing.getShapeAt(new Point(x, y));
        if (shape != null) {
            drawing.removeShape(shape);
        }
    }

    // Getters
    public Drawing getDrawing() {
        return drawing;
    }
}