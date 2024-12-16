package Util;

import model.GameModel;
import model.Move;

public interface MoveStrategy {
    /**
     * Computes the next move for the opponent.
     *
     * @param gameModel The game model.
     * @return An action described by an object or a list of positions (totem + token).
     */
    Move calculateMove(GameModel gameModel);
    void play(GameModel gameModel);
}