package model;

import Util.Command;

public class PlaceTokenCommand implements Command {
    private final GameModel gameModel;
    private final Position position;
    private final Symbol tokenSymbol;
    private final Player originalPlayer;
    private final TurnPhase originalPhase;

    public PlaceTokenCommand(GameModel gameModel, Position position, Symbol tokenSymbol) {
        this.gameModel = gameModel;
        this.position = position;
        this.tokenSymbol = tokenSymbol;
        this.originalPlayer = gameModel.getCurrentPlayer();
        this.originalPhase = gameModel.getCurrentPhase();
    }

    @Override
    public void execute() {
        boolean success = gameModel.placeToken(position, tokenSymbol);
        if (!success) {
            throw new IllegalStateException("Invalid token placement. Try again.");
        }
    }

    @Override
    public void unexecute() {
        Piece piece = gameModel.getBoard().getPiece(position);
        gameModel.removeToken(position);

        // Utiliser la façade au lieu d'accès direct
        gameModel.restoreTokenToPlayer(piece, originalPlayer.getColor());
        gameModel.restoreGameState(originalPlayer, originalPhase);
    }

    @Override
    public boolean isReversible() {
        return true;
    }
}

