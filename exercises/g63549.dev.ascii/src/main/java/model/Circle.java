package model;

public abstract class  Circle extends ColoredShape {
    private double radius; // Radius of the circle
    private Point center;  // Center point of the circle

    // Constructor for Circle
    public Circle(Point center, double radius, char color) {
        super(color); // Calls the constructor of the superclass ColoredShape
        if(radius <= 0)
            throw new IllegalArgumentException("Radius can not be negatif or null");
        this.center = new Point(center); // Uses copy constructor to protect original point
        this.radius = radius;
    }

    // Implementation of move method defined in Shape interface
    @Override
    public void move(double dx, double dy) {
        center.move(dx, dy); // Moves the center point
    }

    // Implementation of isInside method defined in Shape interface
    @Override
    public boolean isInside(Point p) {
        // Calculates the distance from the point to the center of the circle
        return center.distanceTo(p) <= radius; // Returns true if the point is inside the circle
    }

    // Getter for radius
    public double getRadius() {
        return radius;
    }

    // Setter for radius
    public void setRadius(double radius) {
        this.radius = radius;
    }

    // Getter for center
    public Point getCenter() {
        return new Point(center); // Returns a copy of the center point
    }

    // Setter for center using a new point
    public void setCenter(Point center) {
        this.center = new Point(center); // Uses the copy constructor
    }

}
