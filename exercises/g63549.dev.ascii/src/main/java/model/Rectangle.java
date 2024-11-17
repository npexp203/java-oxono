package model;

/**
 * The Rectangle class represents a rectangle shape with a specified upper-left corner, width, height, and color.
 * It extends the Shape class to provide specific properties and methods for a rectangle.
 */
public class Rectangle extends ColoredShape {
    private Point upperLeft;
    double width;
    double height;

    /**
     * Constructor for the Rectangle class.
     *
     * @param upperLeft The upper-left corner of the rectangle.
     * @param width     The width of the rectangle.
     * @param height    The height of the rectangle.
     * @param color     The character representing the color of the rectangle.
     */
    public Rectangle(Point upperLeft, double width, double height, char color) {
        super(color);
        this.upperLeft = upperLeft;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the upper-left corner of the rectangle.
     *
     * @return The upper-left corner point.
     */
    public Point getUpperLeft() {
        return upperLeft;
    }

    /**
     * Gets the width of the rectangle.
     *
     * @return The width of the rectangle.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the rectangle.
     *
     * @return The height of the rectangle.
     */
    public double getHeight() {
        return height;
    }

    @Override
    public void move(double dx, double dy) {
        upperLeft.move(dx, dy);
    }

    @Override
    public boolean isInside(Point p) {
        double x = p.getX();
        double y = p.getY();
        return (x >= upperLeft.getX() && x <= upperLeft.getX() + width &&
                y >= upperLeft.getY() && y <= upperLeft.getY() + height);
    }

}
