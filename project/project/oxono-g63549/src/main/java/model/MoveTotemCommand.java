/**
 * Command to move a totem to a new position.
 */
package model;

import Util.Command;

public class MoveTotemCommand implements Command {
    private final GameModel gameModel;
    private final Symbol totemSymbol;
    private final Position oldPosition;
    private final Position newPosition;

    /**
     * Creates a new MoveTotemCommand.
     * @param gameModel the game model
     * @param totemSymbol the symbol of the totem to move
     * @param oldPosition the old position of the totem
     * @param newPosition the new position of the totem
     */
    public MoveTotemCommand(GameModel gameModel, Symbol totemSymbol, Position oldPosition, Position newPosition) {
        this.gameModel = gameModel;
        this.totemSymbol = totemSymbol;
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }

    @Override
    public void execute() {
        gameModel.moveTotem(totemSymbol, newPosition);
    }

    @Override
    public void unexecute() {
        gameModel.moveTotem(totemSymbol, oldPosition);
    }

    @Override
    public boolean isReversible() {
        return true;
    }
}