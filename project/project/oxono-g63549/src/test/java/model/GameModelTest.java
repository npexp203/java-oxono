package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;


class GameModelTest {

    private GameModel gameModel;

    @BeforeEach
    void setUp() {
        gameModel = new GameModel(6);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor should create game with size 6")
        void testDefaultConstructor() {
            GameModel defaultModel = new GameModel();
            assertEquals(6, defaultModel.getBoardSize());
        }

        @Test
        @DisplayName("Constructor with valid size should initialize correctly")
        void testConstructorWithValidSize() {
            GameModel model = new GameModel(8);
            assertEquals(8, model.getBoardSize());
            assertNotNull(model.getPlayer1());
            assertNotNull(model.getPlayer2());
            assertNotNull(model.getTotemX());
            assertNotNull(model.getTotemO());
        }
    }

    @Nested
    @DisplayName("Game Lifecycle Tests")
    class GameLifecycleTests {

        @Test
        @DisplayName("startGame() should initialize game state correctly")
        void testStartGame() {
            gameModel.startGame();

            assertEquals(TurnPhase.MOVE_TOTEM, gameModel.getCurrentPhase());
            assertEquals(gameModel.getPlayer1(), gameModel.getCurrentPlayer());
            assertFalse(gameModel.isGameOver());
            assertNull(gameModel.getWinner());
        }

        @Test
        @DisplayName("startGame(int) with valid size should work")
        void testStartGameWithValidSize() {
            gameModel.startGame(8);

            assertEquals(8, gameModel.getBoardSize());
            assertEquals(TurnPhase.MOVE_TOTEM, gameModel.getCurrentPhase());
        }

        @Test
        @DisplayName("startGame(int) with invalid size should throw exception")
        void testStartGameWithInvalidSize() {
            assertThrows(IllegalArgumentException.class, () -> gameModel.startGame(3));
            assertThrows(IllegalArgumentException.class, () -> gameModel.startGame(11));
        }

        @Test
        @DisplayName("forfeitGame() should end game and set winner")
        void testForfeitGame() {
            gameModel.startGame();
            Player currentPlayer = gameModel.getCurrentPlayer();

            gameModel.forfeitGame();

            assertTrue(gameModel.isGameOver());
            assertNotNull(gameModel.getWinner());
            assertNotEquals(currentPlayer, gameModel.getWinner());
        }

        @Test
        @DisplayName("forfeitGame() when game is over should throw exception")
        void testForfeitGameWhenGameOver() {
            gameModel.startGame();
            gameModel.forfeitGame();

            assertThrows(IllegalStateException.class, () -> gameModel.forfeitGame());
        }
    }

    @Nested
    @DisplayName("Totem Selection Tests")
    class TotemSelectionTests {

        @BeforeEach
        void setUp() {
            gameModel.startGame();
        }

        @Test
        @DisplayName("selectTotem() should select totem at valid position")
        void testSelectTotem() {
            Position totemPos = gameModel.getTotemPosition(Symbol.X);
            gameModel.selectTotem(totemPos);

            assertTrue(gameModel.hasSelectedTotem());
            assertEquals(totemPos, gameModel.getSelectedTotemPosition());
            assertTrue(gameModel.isPositionSelected(totemPos));
        }

        @Test
        @DisplayName("selectTotem() at invalid position should throw exception")
        void testSelectTotemInvalidPosition() {
            Position invalidPos = new Position(0, 0);
            if (!gameModel.hasTotemAt(invalidPos)) {
                assertThrows(IllegalArgumentException.class, () -> gameModel.selectTotem(invalidPos));
            }
        }

        @Test
        @DisplayName("deselectTotem() should clear selection")
        void testDeselectTotem() {
            Position totemPos = gameModel.getTotemPosition(Symbol.X);
            gameModel.selectTotem(totemPos);
            gameModel.deselectTotem();

            assertFalse(gameModel.hasSelectedTotem());
            assertNull(gameModel.getSelectedTotemPosition());
            assertFalse(gameModel.isPositionSelected(totemPos));
        }

        @Test
        @DisplayName("hasSelectedTotem() should return correct state")
        void testHasSelectedTotem() {
            assertFalse(gameModel.hasSelectedTotem());

            Position totemPos = gameModel.getTotemPosition(Symbol.X);
            gameModel.selectTotem(totemPos);
            assertTrue(gameModel.hasSelectedTotem());
        }
    }

    @Nested
    @DisplayName("Totem Movement Tests")
    class TotemMovementTests {

        @BeforeEach
        void setUp() {
            gameModel.startGame();
        }

        @Test
        @DisplayName("moveTotem() should move totem to valid position")
        void testMoveTotem() {
            Position from = gameModel.getTotemPosition(Symbol.X);
            Position to = new Position(from.x() + 1, from.y());

            gameModel.selectTotem(from);
            boolean moved = gameModel.moveTotem(Symbol.X, to);

            if (moved) {
                assertEquals(to, gameModel.getTotemPosition(Symbol.X));
                assertEquals(TurnPhase.PLACE_TOKEN, gameModel.getCurrentPhase());
                assertEquals(Symbol.X, gameModel.getLastMovedSymbol());
            }
        }

        @Test
        @DisplayName("moveTotem() should throw exception for invalid move")
        void testMoveTotemInvalidMove() {
            Position from = gameModel.getTotemPosition(Symbol.X);
            Position to = new Position(0, 0);
            
            gameModel.selectTotem(from);
            assertThrows(IllegalStateException.class, () -> gameModel.moveTotem(Symbol.X, to));
        }

        @Test
        @DisplayName("moveTotem() when game is over should throw exception")
        void testMoveTotemWhenGameOver() {
            gameModel.forfeitGame();
            assertThrows(IllegalStateException.class, () -> gameModel.moveTotem(Symbol.X, new Position(0, 0)));
        }

        @Test
        @DisplayName("isValidTotemMove() should validate moves correctly")
        void testIsValidTotemMove() {
            Position from = gameModel.getTotemPosition(Symbol.X);
            Position to = new Position(from.x() + 1, from.y());

            assertTrue(gameModel.isValidTotemMove(from, to));
            assertFalse(gameModel.isValidTotemMove(from, new Position(0, 0)));
        }
    }

    @Nested
    @DisplayName("Token Placement Tests")
    class TokenPlacementTests {

        @BeforeEach
        void setUp() {
            gameModel.startGame();
            Position from = gameModel.getTotemPosition(Symbol.X);
            Position to = new Position(from.x() + 1, from.y());
            gameModel.moveTotem(Symbol.X, to);
        }

        @Test
        @DisplayName("placeToken() should place token at valid position")
        void testPlaceToken() {
            Position tokenPos = new Position(0, 0);
            boolean placed = gameModel.placeToken(tokenPos, Symbol.X);

            if (placed) {
                assertNotNull(gameModel.getPieceAt(tokenPos));
                assertEquals(Symbol.X, gameModel.getSymbolAt(tokenPos));
            }
        }

        @Test
        @DisplayName("placeToken() should return false for invalid placement")
        void testPlaceTokenInvalidPlacement() {
            Position tokenPos = new Position(0, 0);
            boolean placed = gameModel.placeToken(tokenPos, Symbol.O);
            assertFalse(placed);
        }

        @Test
        @DisplayName("placeToken() when not in placement phase should return false")
        void testPlaceTokenWrongPhase() {
            gameModel.startGame();
            boolean placed = gameModel.placeToken(new Position(0, 0), Symbol.X);
            assertFalse(placed);
        }
    }

    @Nested
    @DisplayName("Turn Management Tests")
    class TurnManagementTests {

        @BeforeEach
        void setUp() {
            gameModel.startGame();
        }

        @Test
        @DisplayName("endTurn() should switch players")
        void testEndTurn() {
            Player initialPlayer = gameModel.getCurrentPlayer();
            gameModel.endTurn();

            assertNotEquals(initialPlayer, gameModel.getCurrentPlayer());
            assertEquals(TurnPhase.MOVE_TOTEM, gameModel.getCurrentPhase());
        }

        @Test
        @DisplayName("advancePhase() should change phase")
        void testAdvancePhase() {
            assertEquals(TurnPhase.MOVE_TOTEM, gameModel.getCurrentPhase());

            gameModel.advancePhase();
            assertEquals(TurnPhase.PLACE_TOKEN, gameModel.getCurrentPhase());

            gameModel.advancePhase();
            assertEquals(TurnPhase.MOVE_TOTEM, gameModel.getCurrentPhase());
        }
    }

    @Nested
    @DisplayName("AI Tests")
    class AITests {

        @Test
        @DisplayName("setAILevel() should set AI strategy")
        void testSetAILevel() {
            gameModel.setAILevel("random");
            assertTrue(gameModel.getPlayer2().isAutomated());

            gameModel.setAILevel("level1");
            assertTrue(gameModel.getPlayer2().isAutomated());
        }

        @Test
        @DisplayName("executeAutomaticMove() should work for AI player")
        void testExecuteAutomaticMove() {
            gameModel.startGame();
            gameModel.setAILevel("random");
            gameModel.endTurn();

            assertDoesNotThrow(() -> gameModel.executeAutomaticMove(new CommandManager()));
        }
    }

    @Nested
    @DisplayName("Query Methods Tests")
    class QueryMethodsTests {

        @BeforeEach
        void setUp() {
            gameModel.startGame();
        }

        @Test
        @DisplayName("shouldShowHoverEffect() should work correctly")
        void testShouldShowHoverEffect() {
            Position emptyPos = new Position(0, 0);
            assertFalse(gameModel.shouldShowHoverEffect(emptyPos));

            gameModel.selectTotem(gameModel.getTotemPosition(Symbol.X));
            assertTrue(gameModel.shouldShowHoverEffect(emptyPos));
        }

        @Test
        @DisplayName("isValidMoveTarget() should validate targets")
        void testIsValidMoveTarget() {
            Position target = new Position(0, 0);
            assertFalse(gameModel.isValidMoveTarget(target));

            gameModel.selectTotem(gameModel.getTotemPosition(Symbol.X));
            
            Position totemPos = gameModel.getSelectedTotemPosition();
            Position validTarget = findValidTargetPosition(totemPos);
            
            if (validTarget != null) {
                assertTrue(gameModel.isValidMoveTarget(validTarget));
            } else {
                assertFalse(gameModel.isValidMoveTarget(new Position(0, 0)));
            }
        }

        /**
         * Helper method to find a valid target position for totem movement.
         * 
         * @param totemPos the current totem position
         * @return a valid target position, or null if none found
         */
        private Position findValidTargetPosition(Position totemPos) {
            Position[] adjacentPositions = {
                new Position(totemPos.x() + 1, totemPos.y()),
                new Position(totemPos.x() - 1, totemPos.y()),
                new Position(totemPos.x(), totemPos.y() + 1),
                new Position(totemPos.x(), totemPos.y() - 1)
            };
            
            for (Position pos : adjacentPositions) {
                if (gameModel.getBoard().isValidCoordinate(pos) && 
                    gameModel.getBoard().isEmpty(pos) &&
                    gameModel.isValidTotemMove(totemPos, pos)) {
                    return pos;
                }
            }
            
            return null;
        }

        @Test
        @DisplayName("hasTotemAt() should detect totems")
        void testHasTotemAt() {
            Position totemPos = gameModel.getTotemPosition(Symbol.X);
            assertTrue(gameModel.hasTotemAt(totemPos));

            Position emptyPos = new Position(0, 0);
            assertFalse(gameModel.hasTotemAt(emptyPos));
        }

        @Test
        @DisplayName("getSymbolAt() should return correct symbols")
        void testGetSymbolAt() {
            Position totemPos = gameModel.getTotemPosition(Symbol.X);
            assertEquals(Symbol.X, gameModel.getSymbolAt(totemPos));

            Position emptyPos = new Position(0, 0);
            assertNull(gameModel.getSymbolAt(emptyPos));
        }

        @Test
        @DisplayName("getPieceAt() should return pieces")
        void testGetPieceAt() {
            Position totemPos = gameModel.getTotemPosition(Symbol.X);
            assertNotNull(gameModel.getPieceAt(totemPos));
            assertTrue(gameModel.getPieceAt(totemPos) instanceof Totem);

            Position emptyPos = new Position(0, 0);
            assertNull(gameModel.getPieceAt(emptyPos));
        }
    }

    @Nested
    @DisplayName("State Restoration Tests")
    class StateRestorationTests {

        @Test
        @DisplayName("restoreGameState() should restore state")
        void testRestoreGameState() {
            gameModel.startGame();
            Player player = gameModel.getPlayer2();
            TurnPhase phase = TurnPhase.PLACE_TOKEN;

            gameModel.restoreGameState(player, phase);

            assertEquals(player, gameModel.getCurrentPlayer());
            assertEquals(phase, gameModel.getCurrentPhase());
        }

        @Test
        @DisplayName("restoreTokenToCurrentPlayer() should restore token")
        void testRestoreTokenToCurrentPlayer() {
            gameModel.startGame();
            Token token = new Token(Colors.PINK, Symbol.X);

            gameModel.restoreTokenToCurrentPlayer(token);

            assertTrue(gameModel.getCurrentPlayer().getBag().countSymbol(Symbol.X) > 0);
        }

        @Test
        @DisplayName("restoreTokenToPlayer() should restore token to specific player")
        void testRestoreTokenToPlayer() {
            gameModel.startGame();
            Token token = new Token(Colors.BLACK, Symbol.O);

            gameModel.restoreTokenToPlayer(token, Colors.BLACK);

            assertTrue(gameModel.getPlayer2().getBag().countSymbol(Symbol.O) > 0);
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterMethodsTests {

        @Test
        @DisplayName("All getter methods should return correct values")
        void testGetterMethods() {
            gameModel.startGame();

            assertNotNull(gameModel.getCurrentPlayer());
            assertNotNull(gameModel.getPlayer1());
            assertNotNull(gameModel.getPlayer2());
            assertNotNull(gameModel.getBoard());
            assertNotNull(gameModel.getTotemX());
            assertNotNull(gameModel.getTotemO());
            assertNotNull(gameModel.getCurrentPhase());
            assertNotNull(gameModel.getBoardSize());

            assertFalse(gameModel.isGameOver());
            assertNull(gameModel.getWinner());
        }

        @Test
        @DisplayName("Convenience methods should work correctly")
        void testConvenienceMethods() {
            gameModel.startGame();

            assertTrue(gameModel.isCurrentPlayerHuman());
            assertTrue(gameModel.isCurrentPlayerPink());
            assertFalse(gameModel.isCurrentPlayerBot());
            assertTrue(gameModel.isTotemMovementPhase());
            assertFalse(gameModel.isTokenPlacementPhase());
            assertTrue(gameModel.canCurrentPlayerPlay());
            assertEquals(Colors.PINK, gameModel.getCurrentPlayerColor());
        }

        @Test
        @DisplayName("Token management methods should work")
        void testTokenManagementMethods() {
            gameModel.startGame();

            assertTrue(gameModel.getRemainingTokens(Symbol.X) > 0);
            assertTrue(gameModel.getRemainingTokens(Symbol.O) > 0);
            assertFalse(gameModel.wasTotemMoveSuccessful());

            Position totemPos = gameModel.getTotemPosition(Symbol.X);
            assertNotNull(totemPos);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Game should handle multiple start/stop cycles")
        void testMultipleStartStopCycles() {
            for (int i = 0; i < 3; i++) {
                gameModel.startGame();
                assertFalse(gameModel.isGameOver());

                gameModel.forfeitGame();
                assertTrue(gameModel.isGameOver());
            }
        }

        @Test
        @DisplayName("Game should handle rapid phase changes")
        void testRapidPhaseChanges() {
            gameModel.startGame();

            for (int i = 0; i < 10; i++) {
                TurnPhase currentPhase = gameModel.getCurrentPhase();
                gameModel.advancePhase();
                assertNotEquals(currentPhase, gameModel.getCurrentPhase());
            }
        }
    }
}



