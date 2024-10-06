package model;

import java.util.List;
import java.util.ArrayList;

/**
 * The Drawing class represents a collection of shapes.
 * It provides methods to add, remove, and retrieve shapes from the drawing.
 */
public class Drawing {
    private int width, height;
    private List<Shape> shapes;

    /**
     * Constructor for the Drawing class.
     *
     * @param width  The width of the drawing.
     * @param height The height of the drawing.
     */
    public Drawing(int width, int height) {
        this.width = width;
        this.height = height;
        this.shapes = new ArrayList<>();
    }

    /**
     * Adds a shape to the drawing.
     *
     * @param shape The shape to add.
     */
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    /**
     * Removes a shape from the drawing.
     *
     * @param shape The shape to remove.
     * @return True if the shape was removed, false otherwise.
     */
    public boolean removeShape(Shape shape) {
        return shapes.remove(shape);
    }

    /**
     * Removes a shape from the drawing by index.
     *
     * @param index The index of the shape to remove.
     * @return True if the shape was removed, false otherwise.
     */
    public boolean removeShape(int index) {
        if (index >= 0 && index < shapes.size()) {
            shapes.remove(index);
            return true;
        }
        return false;
    }

    /**
     * Gets the shape at a specific point.
     *
     * @param point The point to check.
     * @return The shape at the point, or null if no shape is found.
     */
    public Shape getShapeAt(Point point) {
        for (Shape shape : shapes) {
            if (shape.isInside(point)) {
                return shape;
            }
        }
        return null;
    }

    /**
     * Gets the shape at a specific index.
     *
     * @param index The index of the shape.
     * @return The shape at the specified index, or null if the index is out of bounds.
     */
    public Shape getShapeAt(int index) {
        if (index >= 0 && index < shapes.size()) {
            return shapes.get(index);
        }
        return null;
    }

    /**
     * Gets a list of all shapes in the drawing.
     *
     * @return A list of all shapes.
     */
    public List<Shape> getShapes() {
        return new ArrayList<>(shapes);
    }

    /**
     * Gets the width of the drawing.
     *
     * @return The width of the drawing.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the drawing.
     *
     * @return The height of the drawing.
     */
    public int getHeight() {
        return height;
    }
}
