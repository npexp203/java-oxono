package model;

import command.Command;

public class AddCommand implements Command {
    private Drawing drawing;
    private Shape shape;

    public AddCommand(Drawing drawing, Shape shape) {
        this.drawing = drawing;
        this.shape = shape;
    }

    @Override
    public void execute() {
        drawing.addShape(shape);
    }

    @Override
    public void unexecute() {
        drawing.removeShape(shape);
    }
}

