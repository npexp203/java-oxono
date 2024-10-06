package model;

/**
 * The Point class represents a point in a 2D space with x and y coordinates.
 */
public class Point {
    private double x, y;

    /**
     * Constructor for the Point class.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate of the point.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the point.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Moves the point by specified distances along the x and y axes.
     *
     * @param dx The distance to move along the x-axis.
     * @param dy The distance to move along the y-axis.
     */
    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }
}
