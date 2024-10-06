package model;

/**
 * The Square class represents a square shape with a specified upper-left corner, side length, and color.
 * It extends the Rectangle class since a square is a specific type of rectangle.
 */
public class Square extends Rectangle {

    /**
     * Constructor for the Square class.
     *
     * @param upperLeft The upper-left corner of the square.
     * @param side      The length of the side of the square.
     * @param color     The character representing the color of the square.
     */
    public Square(Point upperLeft, double side, char color) {
        super(upperLeft, side, side, color);
    }

    /**
     * Sets the side length of the square.
     * This method sets both the width and the height to the given value.
     *
     * @param side The length of the side of the square.
     */
    public void setSide(double side) {
        super.width = side;  // Directly set the width in the parent class
        super.height = side; // Directly set the height in the parent class
    }

    /**
     * Gets the side length of the square.
     *
     * @return The length of the side of the square.
     */
    public double getSide() {
        return getWidth(); // Since width and height are equal, just return one of them
    }
}
