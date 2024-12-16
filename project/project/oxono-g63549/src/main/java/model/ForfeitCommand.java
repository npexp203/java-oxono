package model;

import Util.Command;

public class ForfeitCommand implements Command {
    private final GameModel gameModel;

    public ForfeitCommand(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public void execute() {
        gameModel.forfeitGame();
    }

    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Forfeit cannot be undone.");
    }

    @Override
    public boolean isReversible() {
        return false;
    }
}
