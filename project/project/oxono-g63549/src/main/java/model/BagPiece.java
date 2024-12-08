package model;

import java.util.ArrayList;
import java.util.List;

public class BagPiece {
    private List<Piece> bagPiece;
    private final Color color;

    public BagPiece(Color color) {
        this.color = color;
        bagPiece = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            bagPiece.add(new Piece(color, Symbol.O));
            bagPiece.add(new Piece(color, Symbol.X));
        }

    }

    public Piece removePieceFromBag(Symbol symbol){
        for (Piece p : bagPiece){
            if(p.getSymbol() == symbol){
                bagPiece.remove(p);
                return p;
            }
        }
        throw new IllegalStateException("No piece with symbol " + symbol + " available.");

    }
}
