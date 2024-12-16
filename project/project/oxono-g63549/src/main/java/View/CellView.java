package View;

import Controller.GameController;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.*;

public class CellView extends StackPane {
    private final int row;
    private final int col;
    private final GameController controller;
    private final Rectangle rectangle;
    private final Text label; // Pour afficher le symbole ou d'autres informations
    private boolean isSelected;

    public CellView(int row, int col, GameController controller) {
        this.row = row;
        this.col = col;
        this.controller = controller;
        this.isSelected = false; // Par défaut, la cellule n'est pas sélectionnée

        // Crée la cellule avec un rectangle
        rectangle = new Rectangle(50, 50); // Taille de la cellule
        rectangle.setFill(javafx.scene.paint.Color.WHITE); // Couleur par défaut
        rectangle.setStroke(javafx.scene.paint.Color.BLACK); // Bordure noire

        // Crée un label pour afficher du texte dans la cellule
        label = new Text("");
        label.setFont(new Font(20)); // Taille de la police
        label.setFill(javafx.scene.paint.Color.BLACK); // Couleur par défaut du texte

        getChildren().addAll(rectangle, label);

        // Gère les clics sur la cellule
        setOnMouseClicked(event -> controller.handleCellClick(row, col));
    }

    /**
     * Met à jour l'apparence de la cellule en fonction du modèle de jeu.
     */
    public void updateCell(GameModel model) {
        Board board = model.getBoard();
        Position position = new Position(row, col);
        Piece piece = board.getPiece(position);

        if (piece == null) {
            rectangle.setFill(javafx.scene.paint.Color.WHITE); // Case vide
            label.setText(""); // Pas de symbole
        } else if (piece instanceof Totem) {
            rectangle.setFill(javafx.scene.paint.Color.LIGHTBLUE); // Totem en bleu
            label.setText(piece.getSymbol().toString());
        } else if (piece instanceof Token token) {
            if (token.getColor() == Color.PINK) {
                rectangle.setFill(javafx.scene.paint.Color.PINK); // Token rose
            } else if (token.getColor() == Color.BLACK) {
                rectangle.setFill(javafx.scene.paint.Color.BLACK); // Token noir
            }
            label.setText(token.getSymbol().toString());
        }

        // Si la cellule est sélectionnée
        if (isSelected) {
            rectangle.setStroke(javafx.scene.paint.Color.YELLOW); // Bordure jaune pour la sélection
            rectangle.setStrokeWidth(3);
        } else {
            rectangle.setStroke(javafx.scene.paint.Color.BLACK); // Bordure par défaut
            rectangle.setStrokeWidth(1);
        }
    }

    /**
     * Définit l'état de sélection de la cellule.
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected; // Mettez à jour l'état interne
        if (selected) {
            rectangle.setStroke(javafx.scene.paint.Color.YELLOW); // Bordure jaune pour la sélection
            rectangle.setStrokeWidth(3); // Épaissir la bordure
        } else {
            rectangle.setStroke(javafx.scene.paint.Color.BLACK); // Bordure par défaut
            rectangle.setStrokeWidth(1); // Taille normale
        }
    }

    /**
     * Retourne l'état de sélection de la cellule.
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Renvoie la position de la cellule.
     */
    public Position getPosition() {
        return new Position(row, col);
    }
}
