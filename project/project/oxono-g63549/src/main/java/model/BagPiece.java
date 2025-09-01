package model;

import java.util.ArrayList;
import java.util.List;

public class BagPiece {
    private List<Piece> bagPiece;

    public BagPiece(Colors colors) {
        bagPiece = new ArrayList<>(16);
        for (int i = 0; i < 8; i++) {
            bagPiece.add(new Token(colors, Symbol.O));
            bagPiece.add(new Token(colors, Symbol.X));
        }
    }

    
    public int countSymbol(Symbol symbol) {
        int count = 0;
        for (Piece p : bagPiece) {
            if (p.getSymbol() == symbol) {
                count++;
            }
        }
        return count;
    }
    /**
     * Removes and returns a piece with the specified symbol.
     * @param symbol the symbol of the piece to remove
     * @return the removed piece, or null if no piece with that symbol exists
     */
    public Piece removePiece(Symbol symbol) {
        for (int i = 0; i < bagPiece.size(); i++) {
            Piece piece = bagPiece.get(i);
            if (piece.getSymbol() == symbol) {
                return bagPiece.remove(i);
            }
        }
        return null;
    }



    /**
     * Returns the total number of pieces remaining.
     */
    public int size() {
        return bagPiece.size();
    }

    /**
     * Checks if the bag is empty.
     */
    public boolean isEmpty() {
        return bagPiece.isEmpty();
    }
    public void addPiece(Piece piece) {
        bagPiece.add(piece);
    }


}
