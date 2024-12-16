package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testFindTotemPosition() {
        Board board = new Board();
        Totem totemX = new Totem(Symbol.X);
        board.putToken(new Position(2, 2), totemX);

        Position position = board.findTotemPosition(totemX);

        assertEquals(new Position(2, 2), position, "Totem X should be found at (2, 2)");
    }

}