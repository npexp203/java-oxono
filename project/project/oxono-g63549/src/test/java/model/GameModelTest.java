package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    @Test
    void testStartGameInitialization() {
        GameModel gameModel = new GameModel();
        gameModel.startGame();

        Position posX = gameModel.getBoard().findTotemPosition(new Totem(Symbol.X));
        assertEquals(new Position(0, 0), posX, "Totem X should be at position (0, 0)");

        Position posO = gameModel.getBoard().findTotemPosition(new Totem(Symbol.O));
        assertEquals(new Position(5, 5), posO, "Totem O should be at position (5, 5)");


        assertEquals(gameModel.getPlayer1(), gameModel.getCurrentPlayer(), "Player 1 should be the current player.");
    }


    @Test
    void testCheckWin() {
        GameModel gameModel = new GameModel();
        gameModel.startGame();

        // Déplacement du totem pour permettre un alignement horizontal
        gameModel.moveTotem(Symbol.X, new Position(0, 1));

        // Place des tokens de manière adjacente au totem
        gameModel.placeToken(new Position(0, 2), Symbol.X);
        gameModel.placeToken(new Position(0, 3), Symbol.X);
        gameModel.placeToken(new Position(0, 4), Symbol.X);

        // Vérifie si le jeu détecte la victoire
        assertTrue(gameModel.checkWin(), "Player 1 should win with an aligned row.");
        assertEquals(gameModel.getPlayer1(), gameModel.getWinner(), "Player 1 should be the winner.");
    }



    @Test
    void getWinner() {
    }

    @Test
    void getCurrentPlayer() {
    }

    @Test
    void moveTotem() {
    }

    @Test
    void placeToken() {
    }

    @Test
    void isMovePossible() {
    }

    @Test
    void forfeitGame() {
    }

    @Test
    void testIsMovePossible() {
    }

    @Test
    void getBoard() {
    }

    @Test
    void removeToken() {
    }

    @Test
    void getTotemPosition() {
    }

    @Test
    void isGameOver() {
    }

    @Test
    void getCurrentPhase() {
    }

    @Test
    void advancePhase() {
    }

    @Test
    void endTurn() {
    }

    @Test
    void executeAutomaticMove() {
    }
}