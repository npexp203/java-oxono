package model;

/**
 * The Circle class represents a circle shape with a center point, radius, and color.
 * It extends the Shape class to provide specific properties and methods for a circle.
 */
public class Circle extends ColoredShape {
    private Point center;
    private double radius;

    /**
     * Constructor for the Circle class.
     *
     * @param center The center point of the circle.
     * @param radius The radius of the circle.
     * @param color  The character representing the color of the circle.
     */
    public Circle(Point center, double radius, char color) {
        super(color);
        this.center = center;
        this.radius = radius;
    }

    /**
     * Gets the center of the circle.
     *
     * @return The center point of the circle.
     */
    public Point getCenter() {
        return center;
    }

    /**
     * Gets the radius of the circle.
     *
     * @return The radius of the circle.
     */
    public double getRadius() {
        return radius;
    }

    @Override
    public void move(double dx, double dy) {
        center.move(dx, dy);
    }

    @Override
    public boolean isInside(Point p) {
        double distance = Math.sqrt(Math.pow(center.getX() - p.getX(), 2) + Math.pow(center.getY() - p.getY(), 2));
        return distance <= radius;
    }
}
