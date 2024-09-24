package model;

public abstract class ColoredShape implements Shape {

    private char color;
    public ColoredShape(char color) {
        this.color = color;
    }

    // Implementation of getColor method that we defined in Shape interface
    @Override
    public char getColor() {
        return color; // Returns the color character
    }

    // Implementation of setColor method that we defined in Shape interface
    @Override
    public void setColor(char color) {
        this.color = color; // Sets the color character
    }
}
