package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class PieceTest {


    @Test
    public void testPieceCreation(){
        Piece piece = new Piece(Color.BLACK,Symbol.X);
        assertEquals(Color.BLACK, piece.getColor());
        assertEquals(Symbol.X, piece.getSymbol());
    }


    @org.junit.jupiter.api.Test
    void comparePieceSymbol() {
    }

    @org.junit.jupiter.api.Test
    void matches() {
    }
}