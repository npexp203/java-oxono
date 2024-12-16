package model;

import Util.Command;

public class PlaceTokenCommand implements Command {
    private final GameModel gameModel;
    private final Position position;
    private final Symbol tokenSymbol;

    public PlaceTokenCommand(GameModel gameModel, Position position, Symbol tokenSymbol) {
        this.gameModel = gameModel;
        this.position = position;
        this.tokenSymbol = tokenSymbol;
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
        gameModel.removeToken(position);
    }

    @Override
    public boolean isReversible() {
        return true;
    }
}

