package model;

import Util.Command;

public class PlaceTokenCommand implements Command {
    private final GameModel gameModel;
    private final Position position;
    private final Symbol tokenSymbol;
    private final Player originalPlayer;
    private final TurnPhase originalPhase;
    private final Symbol originalLastMovedSymbol;

    private Piece placedPiece;

    public PlaceTokenCommand(GameModel gameModel, Position position, Symbol tokenSymbol) {
        this.gameModel = gameModel;
        this.position = position;
        this.tokenSymbol = tokenSymbol;
        this.originalPlayer = gameModel.getCurrentPlayer();
        this.originalPhase = gameModel.getCurrentPhase();
        this.originalLastMovedSymbol = gameModel.getLastMovedSymbol();

    }

    @Override
    public void execute() {
        boolean success = gameModel.placeToken(position, tokenSymbol);
        if (!success) {
            throw new IllegalStateException("Invalid token placement.");
        }
        this.placedPiece = gameModel.getBoard().getPiece(position);
    }

    @Override
    public void unexecute() {
        gameModel.removeToken(position);

        if (placedPiece != null) {
            gameModel.restoreTokenToPlayer(placedPiece, originalPlayer.getColor());
        }

        gameModel.restoreGameState(originalPlayer, originalPhase);
        gameModel.setLastMovedSymbol(originalLastMovedSymbol);

        System.out.println("Token placement undone: " + tokenSymbol + " removed from " + position);
    }

    @Override
    public boolean isReversible() {
        return true;
    }
}

