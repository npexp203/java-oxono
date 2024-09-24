package model;

public interface Shape {
        // Moves the shape by the specified amounts along the x and y axes
        void move(double dx, double dy);

        // Checks if a given point is inside the shape
        boolean isInside(Point p);

        // Returns the display character representing the shape's color
        char getColor();

        // Sets the display character representing the shape's color
        void setColor(char color);
    }


