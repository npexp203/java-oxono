package model;

/**
 * The Shape interface defines the common behaviors of all shapes in the drawing.
 */
public interface Shape {

    /**
     * Checks if a point is inside the shape.
     *
     * @param p The point to check.
     * @return True if the point is inside the shape, false otherwise.
     */
    boolean isInside(Point p);

    /**
     * Moves the shape by the specified distances.
     *
     * @param dx The horizontal distance to move.
     * @param dy The vertical distance to move.
     */
    void move(double dx, double dy);

    /**
     * Sets the color of the shape.
     *
     * @param color The character representing the color.
     */
    void setColor(char color);

    /**
     * Gets the color of the shape.
     *
     * @return The character representing the color of the shape.
     */
    char getColor();
}
