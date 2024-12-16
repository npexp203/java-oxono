package model;

import Util.MoveStrategy;

import java.util.List;
import java.util.Random;

public class RandomMoveStrategy implements MoveStrategy {
    private Random random = new Random();

    @Override
    public Move calculateMove(GameModel gameModel) {
        // Choix aléatoire entre Symbol.X et Symbol.O
        Symbol chosenSymbol = random.nextBoolean() ? Symbol.X : Symbol.O;
        Totem chosenTotem = (chosenSymbol == Symbol.X) ? gameModel.getTotemX() : gameModel.getTotemO();

        // Positions valides pour le totem
        List<Position> validTotemMoves = gameModel.getBoard().getValidTotemPositions(chosenTotem);
        Position startPos = gameModel.getBoard().findTotemPosition(chosenTotem);

        Position totemTarget;
        if (validTotemMoves.isEmpty()) {
            // Pas de déplacement possible, le totem reste sur place
            totemTarget = startPos;
        } else {
            // Choisir une position au hasard
            totemTarget = validTotemMoves.get(random.nextInt(validTotemMoves.size()));
        }

        // Maintenant, on calcule les positions valides pour le token autour de la position cible du totem (totemTarget)
        List<Position> validTokenPlacements = GameRules.getValidTokenPositionsForPosition(gameModel.getBoard(), totemTarget);

        Position tokenTarget = null;
        if (!validTokenPlacements.isEmpty()) {
            tokenTarget = validTokenPlacements.get(random.nextInt(validTokenPlacements.size()));
        }

        return new Move(totemTarget, tokenTarget, chosenSymbol);
    }
}

