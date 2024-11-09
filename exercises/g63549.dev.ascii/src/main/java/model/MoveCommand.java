package model;

import command.Command;
import model.Drawing;
import model.Shape;
import model.Point;

public class MoveCommand implements Command {
    private Drawing drawing;
    private Shape shape;
    private double dx, dy;
    private double previousDx, previousDy;

    public MoveCommand(Drawing drawing, Shape shape, double dx, double dy) {
        this.drawing = drawing;
        this.shape = shape;
        this.dx = dx;
        this.dy = dy;

        this.previousDx = dx;
        this.previousDy = dy;
    }

    @Override
    public void execute() {
        shape.move(dx, dy);
    }

    @Override
    public void unexecute() {
        shape.move(-dx, -dy);
    }
}
