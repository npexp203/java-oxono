package model;

import Util.Command;

public class CompositeMoveCommand implements Command {
    private final MoveTotemCommand moveTotemCommand;
    private final PlaceTokenCommand placeTokenCommand;
    private final boolean hasTokenPlacement;

    public CompositeMoveCommand(GameModel gameModel, Move move) {
        Position currentTotemPos = gameModel.getTotemPosition(move.getTokenSymbol());

        this.moveTotemCommand = new MoveTotemCommand(
                gameModel,
                move.getTokenSymbol(),
                currentTotemPos,
                move.getTotemTarget()
        );

        this.hasTokenPlacement = (move.getTokenTarget() != null);
        this.placeTokenCommand = hasTokenPlacement ?
                new PlaceTokenCommand(gameModel, move.getTokenTarget(), move.getTokenSymbol()) :
                null;
    }

    @Override
    public void execute() {
        moveTotemCommand.execute();

        if (hasTokenPlacement && placeTokenCommand != null) {
            try {
                placeTokenCommand.execute();
            } catch (IllegalStateException e) {
                moveTotemCommand.unexecute();
                throw e;
            }
        }
    }

    @Override
    public void unexecute() {
        if (hasTokenPlacement && placeTokenCommand != null) {
            placeTokenCommand.unexecute();
        }

        moveTotemCommand.unexecute();
    }

    @Override
    public boolean isReversible() {
        return moveTotemCommand.isReversible() &&
                (!hasTokenPlacement || placeTokenCommand.isReversible());
    }
}