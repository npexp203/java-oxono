package model;

import java.util.ArrayList;
import java.util.List;

public class Group implements Shape {
    private List<Shape> shapes;
    private char color;

    /**
     * Constructor for the Group class.
     * Initializes the group with a specified color.
     *
     * @param color The character representing the color of the group.
     */
    public Group(char color) {
        this.shapes = new ArrayList<>();
        this.color = color;
    }

    /**
     * Checks if a point is inside any shape in the group.
     *
     * @param p The point to check.
     * @return True if the point is inside any shape in th  e group, false otherwise.
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

    /**
     * Moves all shapes in the group by the specified distances.
     *
     * @param dx The horizontal distance to move.
     * @param dy The vertical distance to move.
     */
    @Override
    public void move(double dx, double dy) {
        for (Shape shape : shapes) {
            shape.move(dx, dy);
        }
    }

    /**
     * Adds a shape to the group.
     *
     * @param shape The shape to add.
     */
    void addShape(Shape shape) {
        shapes.add(shape);
    }

    /**
     * Gets the list of shapes in the group.
     *
     * @return A list of shapes in the group.
     */
    List<Shape> getShapes() {
        return new ArrayList<>(shapes);
    }

    /**
     * Sets the color for the group and all contained shapes.
     *
     * @param color The character representing the color to set.
     */
    @Override
    public void setColor(char color) {
        this.color = color;
        for (Shape shape : shapes) {
            shape.setColor(color);
        }
    }

    /**
     * Gets the color of the group.
     *
     * @return The character representing the color of the group.
     */
    @Override
    public char getColor() {
        return this.color;
    }
}
