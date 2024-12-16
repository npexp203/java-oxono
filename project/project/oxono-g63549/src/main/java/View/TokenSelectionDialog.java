package View;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import model.Symbol;

import java.util.Optional;

public class TokenSelectionDialog {

    /**
     * Affiche une boîte de dialogue pour sélectionner un token.
     *
     * @param remainingX Nombre de tokens restants pour le symbole X.
     * @param remainingO Nombre de tokens restants pour le symbole O.
     * @return Le symbole choisi par le joueur (X ou O).
     */
    public static Symbol showTokenSelectionDialog(int remainingX, int remainingO) {
        // Crée une liste des choix disponibles
        String choiceX = "X (restants : " + remainingX + ")";
        String choiceO = "O (restants : " + remainingO + ")";

        // Ajoute les choix dans un `ChoiceDialog`
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choiceX, choiceX, choiceO);
        dialog.setTitle("Choix du Token");
        dialog.setHeaderText("Sélectionnez le token que vous souhaitez jouer.");
        dialog.setContentText("Tokens disponibles :");

        // Affiche le dialogue et récupère le choix
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get().startsWith("X") ? Symbol.X : Symbol.O;
        } else {
            // Si l'utilisateur annule, on peut retourner une valeur par défaut ou lever une exception
            return null; // Ou Symbol.X par défaut
        }
    }
}
