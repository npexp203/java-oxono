package model;

import java.util.ArrayList;
import java.util.List;

public class Pieces {

    private char totemsSymbol;
    private List<Piece> piecesList;

    public Pieces(Symbol symbol,Color color) {
        piecesList = new ArrayList<Piece>(16);
        for (int i = 0; i < 8; i++) {
            piecesList.add(new Piece(Symbol.X,color));
            piecesList.add(new Piece(Symbol.O,color));
        }

    }

    public char getTotems() {
        return totemsSymbol;
    }
}
