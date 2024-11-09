package model;


import command.Command;
import model.Drawing;
import model.Shape;


import model.Drawing;
import model.Shape;

public class DeleteCommand implements Command {
    private Drawing drawing;
    private int index;
    private Shape removedShape;

    public DeleteCommand(Drawing drawing, int index) {
        this.drawing = drawing;
        this.index = index;
    }

    @Override
    public void execute() {
        // Avant de supprimer, on sauvegarde la forme pour pouvoir la restaurer en cas d'unexecute
        removedShape = drawing.getShapeAt(index);
        if (removedShape != null) {
            drawing.removeShape(index);
        }
    }

    @Override
    public void unexecute() {
        // On remet la forme à son emplacement original
        if (removedShape != null) {
            drawing.getShapes().add(index, removedShape);
        }
    }
}
