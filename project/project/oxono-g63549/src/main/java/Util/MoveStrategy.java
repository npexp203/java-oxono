package Util;

import model.GameModel;
import model.Move;

public interface MoveStrategy {
    /**
     * Calcule le prochain mouvement de l'adversaire.
     *
     * @param gameModel Le modèle du jeu.
     * @return Une action décrite par un objet ou une liste de positions (totem + token).
     */
    Move calculateMove(GameModel gameModel);
}

