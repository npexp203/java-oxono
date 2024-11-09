package model;

import java.util.ArrayList;
import java.util.List;

public class Pieces {

    private char totemsSymbol;
    private List<Pieces> piecesList;

    public Pieces() {
        piecesList = new ArrayList<>(16);
        for (int i = 0; i < 8; i++) {
            piecesList.add(new Pieces(Symbol.X,Color.PINK));


        }

    }

    public char getTotems() {
        return totemsSymbol;
    }
}
