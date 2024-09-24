package model;
import java.util.ArrayList;
import java.util.List;

public class Drawing {
    private int width, height; // Dimensions of the drawing
    private List<Shape> shapes; // List to store shapes

    // Constructor for Drawing
    public Drawing(int width, int height) {
        this.width = width;
        this.height = height;
        this.shapes = new ArrayList<>(); // Initialize the list of shapes
    }

    //  Method to add a shape to the drawing
    protected void addShape(Shape shape) {
        shapes.add(shape);
    }

    // Method to remove a shape from the drawing
    protected boolean removeShape(Shape shape) {
        return shapes.remove(shape); // Returns true if the shape was in the list and was removed
    }
    //Remove a shape by index
    protected boolean removeShape(int index) {
        if (index >= 0 && index < shapes.size()) {
            shapes.remove(index);
            return true;
        }
        return false;
    }

    // Method to get a shape at a specific point
    public Shape getShapeAt(Point point) {
        for (Shape shape : shapes) {
            if (shape.isInside(point)) {
                return shape; // Returns the first shape for which the point is inside
            }
        }
        return null; // Returns null if no shape contains the point
    }
    // Method to get a shape by index
    public Shape getShapeAt(int index) {
        if (index >= 0 && index < shapes.size()) {
            return shapes.get(index);
        }
        return null;
    }

    // Getters for width and height
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Method to list all shapes in the drawing
    public List<Shape> getShapes() {
        return new ArrayList<>(shapes); // Returns a copy of the list of shapes
    }
}
