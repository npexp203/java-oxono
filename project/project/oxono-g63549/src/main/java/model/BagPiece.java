package model;

import java.util.ArrayList;
import java.util.List;

public class BagPiece {
    private List<Piece> bagPiece;

    public BagPiece(Color color) {
        bagPiece = new ArrayList<>(16);
        for (int i = 0; i < 8; i++) {
            bagPiece.add(new Token(color, Symbol.O));
            bagPiece.add(new Token(color, Symbol.X));
        }
    }

    public Piece removePieceFromBag(Symbol symbol) {
        for (Piece p : bagPiece) {
            if (p.getSymbol() == symbol) {
                bagPiece.remove(p);
                return p;
            }
        }
        throw new IllegalStateException("No piece with symbol " + symbol + " available.");
    }
    // Dans BagPiece
    public int countSymbol(Symbol symbol) {
        int count = 0;
        for (Piece p : bagPiece) {
            if (p.getSymbol() == symbol) {
                count++;
            }
        }
        return count;
    }


}
