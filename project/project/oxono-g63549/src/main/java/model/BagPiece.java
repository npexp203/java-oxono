package model;

import java.util.ArrayList;
import java.util.List;

public class BagPiece {
    private List<Piece> pieces;
    private final Color color;

    public BagPiece(Color color) {
        this.color = color;
        pieces = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            pieces.add(new Piece(color, Symbol.O));
            pieces.add(new Piece(color, Symbol.X));
        }

    }

    public Piece removePieceFromBag(Symbol symbol){
        for (Piece p : pieces){
            if(p.getSymbol() == symbol){
                pieces.remove(p);
                return p;
            }
        }
        throw new IllegalStateException("No piece with symbol " + symbol + " available.");

    }
    public void reset() {
        pieces.clear();
        for (int i = 0; i < 8; i++) {
            pieces.add(new Piece(color, Symbol.O));
            pieces.add(new Piece(color, Symbol.X));
        }
    }
}
