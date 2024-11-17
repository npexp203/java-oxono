package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the main drawing, containing a list of shapes.
 * Provides methods to group, ungroup, add, and remove shapes.
 */
public class AsciiPaint {
    private List<Shape> shapes = new ArrayList<>();

    /**
     * Groups the shapes at the specified indices.
     * @param indices the indices of shapes to group
     * @return the created group
     */
    public Group group(List<Integer> indices) {
        Group newGroup = new Group('G'); // Default group color
        List<Shape> shapesToRemove = new ArrayList<>();

        for (int index : indices) {
            if (index >= 0 && index < shapes.size()) {
                Shape shape = shapes.get(index);
                newGroup.addShape(shape);
                shapesToRemove.add(shape);
            }
        }

        shapes.removeAll(shapesToRemove);
        shapes.add(newGroup);
        return newGroup;
    }

    /**
     * Ungroups a group and adds its shapes back to the drawing.
     * @param group the group to ungroup
     */
    public void ungroup(Group group) {
        if (shapes.contains(group)) {
            shapes.remove(group);
            shapes.addAll(group.getShapes());
        }
    }

    /**
     * Retrieves all shapes in the drawing.
     * @return a list of shapes
     */
    public List<Shape> getShapes() {
        return shapes;
    }

    /**
     * Adds a shape to the drawing.
     * @param shape the shape to add
     */
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    /**
     * Removes a shape from the drawing.
     * @param shape the shape to remove
     */
    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }
}
