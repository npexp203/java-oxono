package model;

import Util.MoveStrategy;

import java.util.List;


public class IntelligentMoveStrategy implements MoveStrategy {
    private final RandomMoveStrategy fallbackStrategy;

    public IntelligentMoveStrategy() {
        this.fallbackStrategy = new RandomMoveStrategy();
    }

    @Override
    public Move calculateMove(GameModel gameModel) {

        Move winningMove = findWinningMove(gameModel);
        if (winningMove != null) {
            return winningMove;
        }

        return fallbackStrategy.calculateMove(gameModel);
    }

    @Override
    public void play(GameModel gameModel) {
    }


    private Move findWinningMove(GameModel gameModel) {
        Board board = gameModel.getBoard();
        Player currentPlayer = gameModel.getCurrentPlayer();

        for (Symbol totemSymbol : new Symbol[]{Symbol.X, Symbol.O}) {
            Totem totem = (totemSymbol == Symbol.X) ? gameModel.getTotemX() : gameModel.getTotemO();

            List<Position> validTotemMoves = board.getValidTotemPositions(totem);

            for (Position newTotemPos : validTotemMoves) {
                Position currentTotemPos = board.findTotemPosition(totem);
                Piece tempPiece = board.getPiece(newTotemPos);
                board.removePiece(currentTotemPos);
                board.putPiece(newTotemPos, totem);

                List<Position> validTokenPlacements = GameRules.getValidTokenPositionsForPosition(board, newTotemPos);

                Position winningTokenPos = testTokenPlacements(gameModel, totemSymbol, validTokenPlacements, currentPlayer);

                board.removePiece(newTotemPos);
                board.putPiece(currentTotemPos, totem);
                if (tempPiece != null) {
                    board.putPiece(newTotemPos, tempPiece);
                }

                if (winningTokenPos != null) {
                    return new Move(newTotemPos, winningTokenPos, totemSymbol);
                }
            }
        }

        return null;
    }
    private Position testTokenPlacements(GameModel gameModel, Symbol totemSymbol, List<Position> validTokenPlacements, Player currentPlayer) {
        Board board = gameModel.getBoard();

        if (currentPlayer.getBag().countSymbol(totemSymbol) <= 0) {
            return null;
        }

        for (Position tokenPos : validTokenPlacements) {
            Token tempToken = new Token(currentPlayer.getColor(), totemSymbol);
            board.putPiece(tokenPos, tempToken);

            boolean isWinning = checkIfWinning(board, tokenPos);

            board.removePiece(tokenPos);

            if (isWinning) {
                return tokenPos;
            }
        }

        return null;
    }

    private boolean checkIfWinning(Board board, Position position) {
        Piece piece = board.getPiece(position);
        if (piece == null || piece instanceof Totem) {
            return false;
        }

        return checkAlignmentFromPosition(board, position, 1, 0) ||
               checkAlignmentFromPosition(board, position, 0, 1);
    }

    private boolean checkAlignmentFromPosition(Board board, Position startPos, int dx, int dy) {
        Piece startPiece = board.getPiece(startPos);
        if (startPiece instanceof Totem) return false;

        Colors targetColors = startPiece.getColor();
        Symbol targetSymbol = startPiece.getSymbol();

        for (int startOffset = -3; startOffset <= 0; startOffset++) {
            int consecutiveColor = 0;
            int consecutiveSymbol = 0;

            for (int step = 0; step < 4; step++) {
                int x = startPos.x() + (startOffset + step) * dx;
                int y = startPos.y() + (startOffset + step) * dy;
                Position pos = new Position(x, y);

                if (!board.isValidCoordinate(pos)) break;

                Piece piece = board.getPiece(pos);
                if (piece == null || piece instanceof Totem) break;

                if (piece.getColor() == targetColors) consecutiveColor++;
                if (piece.getSymbol() == targetSymbol) consecutiveSymbol++;
            }

            if (consecutiveColor >= 4 || consecutiveSymbol >= 4) {
                return true;
            }
        }

        return false;}
}
   