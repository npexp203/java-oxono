package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of shapes.
 * Implements the Composite Pattern to manage multiple shapes as one.
 */
public class        Group extends ColoredShape {
    private List<Shape> shapes = new ArrayList<>();

    /**
     * Constructs a group with a specific color.
     * @param color the color of the group
     */
    public Group(char color) {
        super(color);
    }

    /**
     * Adds a shape to the group.
     * @param shape the shape to add
     */
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    /**
     * Retrieves the shapes in the group.
     * @return a list of shapes in the group
     */
    public List<Shape> getShapes() {
        return shapes;
    }

    /**
     * Moves all shapes in the group by the given offsets.
     * @param dx the offset along the x-axis
     * @param dy the offset along the y-axis
     */
    @Override
    public void move(double dx, double dy) {
        for (Shape shape : shapes) {
            shape.move(dx, dy);
        }
    }

    /**
     * Checks if the group contains the specified point.
     * @param p the point to check
     * @return true if any shape in the group contains the point, false otherwise
     */
    @Override
    public boolean isInside(Point p) {
        for (Shape shape : shapes) {
            if (shape.isInside(p)) {
                return true;
            }
        }
        return false;
    }
}
