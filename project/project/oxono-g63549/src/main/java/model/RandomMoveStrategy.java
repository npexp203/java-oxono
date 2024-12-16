/**
 * This class implements a random move strategy for the Gomoku game.
 * It extends the abstract class MoveStrategy and implements the method calculateMove,
 * which returns a random valid move for the current state of the game.
 * The method play is not implemented.
 * @author Florian
 */

package model;

import Util.MoveStrategy;

import java.util.List;
import java.util.Random;

public class RandomMoveStrategy implements MoveStrategy {
    private Random random = new Random();

    /**
     * Calculates a random valid move for the current state of the game.
     * @param gameModel The current state of the game.
     * @return A random valid move for the current state of the game.
     */
    @Override
    public Move calculateMove(GameModel gameModel) {
        Symbol chosenSymbol = random.nextBoolean() ? Symbol.X : Symbol.O;
        Totem chosenTotem = (chosenSymbol == Symbol.X) ? gameModel.getTotemX() : gameModel.getTotemO();

        List<Position> validTotemMoves = gameModel.getBoard().getValidTotemPositions(chosenTotem);
        System.out.println(validTotemMoves);
        Position startPos = gameModel.getBoard().findTotemPosition(chosenTotem);

        Position totemTarget;
        if (validTotemMoves.isEmpty()) {
            totemTarget = startPos;
        } else {
            totemTarget = validTotemMoves.get(random.nextInt(validTotemMoves.size()));
        }

        List<Position> validTokenPlacements = GameRules.getValidTokenPositionsForPosition(gameModel.getBoard(), totemTarget);
        System.out.println(validTokenPlacements);

        Position tokenTarget = null;
        if (!validTokenPlacements.isEmpty()) {
            tokenTarget = validTokenPlacements.get(random.nextInt(validTokenPlacements.size()));
        }
        System.out.println(totemTarget);
        System.out.println(tokenTarget);
        System.out.println(chosenSymbol);
        return new Move(totemTarget, tokenTarget, chosenSymbol);
    }

    /**
     * Does nothing, as this class is only used for testing purposes.
     * @param gameModel The current state of the game.
     */
    @Override
    public void play(GameModel gameModel) {

    }
}