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
    private final Player originalPlayer;
    private final TurnPhase originalPhase;
    private final Symbol originalLastMovedSymbol;

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

        this.originalPlayer = gameModel.getCurrentPlayer();
        this.originalPhase = gameModel.getCurrentPhase();
        this.originalLastMovedSymbol = gameModel.getLastMovedSymbol();
    }

    @Override
    public void execute() {
        gameModel.moveTotem(totemSymbol, newPosition);
    }

    @Override
    public void unexecute() {
        Totem totem = (totemSymbol == Symbol.X) ? gameModel.getTotemX() : gameModel.getTotemO();
        gameModel.getBoard().removePiece(newPosition);
        gameModel.getBoard().putPiece(oldPosition, totem);

        gameModel.restoreGameState(originalPlayer, originalPhase);
        gameModel.setLastMovedSymbol(originalLastMovedSymbol);

        System.out.println("Totem movement undone: " + totemSymbol + " from " + newPosition + " back to " + oldPosition);
    }

    @Override
    public boolean isReversible() {
        return true;
    }
}