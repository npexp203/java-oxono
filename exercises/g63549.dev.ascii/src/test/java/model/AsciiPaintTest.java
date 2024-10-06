import model.AsciiPaint;
import model.Drawing;
import model.Circle;
import model.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AsciiPaintTest {

    private AsciiPaint asciiPaint;

    @BeforeEach
    public void setUp() {
        // Initialiser un objet AsciiPaint avant chaque test
        asciiPaint = new AsciiPaint(50, 30);
    }

    @Test
    public void testAddCircle() {
        // Ajouter un cercle au dessin
        asciiPaint.addCircle(10, 10, 5, 'c');
        Drawing drawing = asciiPaint.getDrawing();

        // Vérifier que le dessin contient une forme
        assertEquals(1, drawing.getShapes().size(), "The drawing should contain one shape.");

        // Vérifier les propriétés de la forme ajoutée
        Shape shape = drawing.getShapes().get(0);
        assertTrue(shape instanceof Circle, "The shape should be an instance of Circle.");

        if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            assertEquals(10, circle.getCenter().getX(), "The circle's x-coordinate should be 10.");
            assertEquals(10, circle.getCenter().getY(), "The circle's y-coordinate should be 10.");
            assertEquals(5, circle.getRadius(), "The circle's radius should be 5.");
            assertEquals('c', circle.getColor(), "The circle's color should be 'c'.");
        }
    }

    @Test
    public void testAddRectangle() {
        // Ajouter un rectangle au dessin
        asciiPaint.addRectangle(5, 5, 10, 15, 'r');
        Drawing drawing = asciiPaint.getDrawing();

        // Vérifier que le dessin contient une forme
        assertEquals(1, drawing.getShapes().size(), "The drawing should contain one shape.");

        // Vérifier les propriétés de la forme ajoutée
        Shape shape = drawing.getShapes().get(0);
        assertTrue(shape instanceof model.Rectangle, "The shape should be an instance of Rectangle.");

        if (shape instanceof model.Rectangle) {
            model.Rectangle rectangle = (model.Rectangle) shape;
            assertEquals(5, rectangle.getUpperLeft().getX(), "The rectangle's upper-left x-coordinate should be 5.");
            assertEquals(5, rectangle.getUpperLeft().getY(), "The rectangle's upper-left y-coordinate should be 5.");
            assertEquals(10, rectangle.getWidth(), "The rectangle's width should be 10.");
            assertEquals(15, rectangle.getHeight(), "The rectangle's height should be 15.");
            assertEquals('r', rectangle.getColor(), "The rectangle's color should be 'r'.");
        }
    }

    @Test
    public void testAddSquare() {
        // Ajouter un carré au dessin
        asciiPaint.addSquare(20, 20, 10, 's');
        Drawing drawing = asciiPaint.getDrawing();

        // Vérifier que le dessin contient une forme
        assertEquals(1, drawing.getShapes().size(), "The drawing should contain one shape.");

        // Vérifier les propriétés de la forme ajoutée
        Shape shape = drawing.getShapes().get(0);
        assertTrue(shape instanceof model.Square, "The shape should be an instance of Square.");

        if (shape instanceof model.Square) {
            model.Square square = (model.Square) shape;
            assertEquals(20, square.getUpperLeft().getX(), "The square's upper-left x-coordinate should be 20.");
            assertEquals(20, square.getUpperLeft().getY(), "The square's upper-left y-coordinate should be 20.");
            assertEquals(10, square.getSide(), "The square's side length should be 10.");
            assertEquals('s', square.getColor(), "The square's color should be 's'.");
        }
    }

    @Test
    public void testMoveShape() {
        // Ajouter une forme puis la déplacer
        asciiPaint.addCircle(10, 10, 5, 'c');
        asciiPaint.moveShape(10, 10, 5, 5);

        Drawing drawing = asciiPaint.getDrawing();
        Shape shape = drawing.getShapes().get(0);

        assertTrue(shape instanceof Circle, "The shape should be an instance of Circle.");

        if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            assertEquals(15, circle.getCenter().getX(), "The circle's x-coordinate should have been updated to 15.");
            assertEquals(15, circle.getCenter().getY(), "The circle's y-coordinate should have been updated to 15.");
        }
    }

    @Test
    public void testRemoveShape() {
        // Ajouter une forme puis la supprimer
        asciiPaint.addCircle(10, 10, 5, 'c');
        asciiPaint.removeShape(10, 10);

        // Vérifier que la forme a été supprimée
        Drawing drawing = asciiPaint.getDrawing();
        assertEquals(0, drawing.getShapes().size(), "The drawing should not contain any shapes.");
    }
}
