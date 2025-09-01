package model;

import java.util.ArrayList;
import java.util.List;

public class GameRules {

    private static final int[][] DIRECTIONS = {{0,1},{1,0},{0,-1},{-1,0}};


    public static boolean isEnclaved(Board board, Position pos) {
        for (int[] dir : DIRECTIONS) {
            Position adjacent = new Position(pos.x() + dir[0], pos.y() + dir[1]);
            if (board.isValidCoordinate(adjacent) && board.isEmpty(adjacent)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isRectilinearMove(Position start, Position target) {
        return start.x() == target.x() || start.y() == target.y();
    }


    public static boolean isPathClear(Board board, Position start, Position target) {
        int stepX = Integer.compare(target.x(), start.x());
        int stepY = Integer.compare(target.y(), start.y());

        int currentX = start.x() + stepX;
        int currentY = start.y() + stepY;

        while (currentX != target.x() || currentY != target.y()) {
            if (board.getPiece(new Position(currentX, currentY)) != null) {
                return false;
            }
            currentX += stepX;
            currentY += stepY;
        }
        return true;
    }


    public static Position handleEnclavedJump(Board board, Totem totem, Position start, int[] direction) {
        int dx = direction[0];
        int dy = direction[1];
        int x = start.x() + dx;
        int y = start.y() + dy;

        while (board.isValidCoordinate(new Position(x, y))) {
            Position current = new Position(x, y);
            Piece piece = board.getPiece(current);

            if (piece == null) {
                return current;
            }

            if (!(piece.getColor() == totem.getColor() || piece.getSymbol() == totem.getSymbol())) {
                break;
            }

            x += dx;
            y += dy;
        }

        return null;
    }


    public static boolean moveTotem(Board board, Totem totem, Position target) {
        Position start = board.findTotemPosition(totem);
        if (start == null) {
            throw new IllegalStateException("The totem is not on the board.");
        }

        if (isEnclaved(board, start)) {
            for (int[] dir : DIRECTIONS) {
                Position finalPos = handleEnclavedJump(board, totem, start, dir);
                if (finalPos != null && finalPos.equals(target)) {
                    board.removePiece(start);
                    board.putPiece(target, totem);
                    return true;
                }
            }
            throw new IllegalStateException("The target position is not reachable for an enclaved totem.");
        }

        if (!board.isValidCoordinate(target)) {
            throw new IllegalArgumentException("Invalid target position.");
        }
        if (!board.isEmpty(target)) {
            throw new IllegalStateException("The target cell is already occupied.");
        }
        if (!isRectilinearMove(start, target)) {
            throw new IllegalStateException("The move must be rectilinear.");
        }
        if (!isPathClear(board, start, target)) {
            throw new IllegalStateException("The path to the target is blocked.");
        }
        board.removePiece(start);
        board.putPiece(target, totem);
        return true;
    }


    public static List<Position> getValidTotemPositions(Board board, Totem totem) {
        List<Position> validMoves = new ArrayList<>();
        Position start = board.findTotemPosition(totem);

        if (isEnclaved(board, start)) {
            for (int[] dir : DIRECTIONS) {
                Position finalPos = handleEnclavedJump(board, totem, start, dir);
                if (finalPos != null) {
                    validMoves.add(finalPos);
                }
            }
            return validMoves;
        }

        for (int[] dir : DIRECTIONS) {
            int x = start.x() + dir[0];
            int y = start.y() + dir[1];
            while (board.isValidCoordinate(new Position(x,y))) {
                Position current = new Position(x,y);
                if (board.getPiece(current) != null) {
                    break;
                }
                validMoves.add(current);
                x += dir[0];
                y += dir[1];
            }
        }
        return validMoves;
    }


    public static List<Position> getValidTokenPositionsForPosition(Board board, Position totemPos) {
        List<Position> validPositions = new ArrayList<>();

        for (int[] dir : DIRECTIONS) {
            Position adj = new Position(totemPos.x() + dir[0], totemPos.y() + dir[1]);
            if (board.isValidCoordinate(adj) && board.isEmpty(adj)) {
                validPositions.add(adj);
            }
        }

        if (validPositions.isEmpty() && isEnclaved(board, totemPos)) {
            int boardSize = board.getSize();
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    Position pos = new Position(i, j);
                    if (board.isEmpty(pos)) {
                        validPositions.add(pos);
                    }
                }
            }
        }

        return validPositions;
    }


    public static boolean isValidTokenPlacement(Board board, Position totemPos, Position tokenPos) {
        if (!board.isValidCoordinate(tokenPos) || !board.isEmpty(tokenPos)) {
            return false;
        }

        int dx = Math.abs(totemPos.x() - tokenPos.x());
        int dy = Math.abs(totemPos.y() - tokenPos.y());
        boolean isAdjacent = (dx == 1 && dy == 0) || (dx == 0 && dy == 1);

        if (isAdjacent) {
            return true;
        }

        if (isEnclaved(board, totemPos)) {
            return true;
        }

        return false;
    }



    public static boolean checkWin(Board board) {
        int boardSize = board.getSize();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Position pos = new Position(i, j);
                if (board.isEmpty(pos)) continue;

                Piece piece = board.getPiece(pos);
                if (piece instanceof Totem) continue;

                if (checkColorAlignment(board, pos, 1, 0) ||
                        checkColorAlignment(board, pos, 0, 1) ||
                        checkSymbolAlignment(board, pos, 1, 0) ||
                        checkSymbolAlignment(board, pos, 0, 1)) {
                    return true;
                }
            }
        }
        return false;
    }


    private static boolean checkColorAlignment(Board board, Position start, int dx, int dy) {
        Piece startPiece = board.getPiece(start);
        if (startPiece instanceof Totem) {
            return false;
        }

        Colors targetColors = startPiece.getColor();

        for (int step = 0; step < 4; step++) {
            int x = start.x() + step * dx;
            int y = start.y() + step * dy;
            Position pos = new Position(x, y);

            if (!board.isValidCoordinate(pos)) {
                return false;
            }

            Piece piece = board.getPiece(pos);
            if (piece == null || piece instanceof Totem || piece.getColor() != targetColors) {
                return false;
            }
        }

        return true;
    }

    private static boolean checkSymbolAlignment(Board board, Position start, int dx, int dy) {
        Piece startPiece = board.getPiece(start);
        if (startPiece instanceof Totem) {
            return false;
        }

        Symbol targetSymbol = startPiece.getSymbol();

        for (int step = 0; step < 4; step++) {
            int x = start.x() + step * dx;
            int y = start.y() + step * dy;
            Position pos = new Position(x, y);

            if (!board.isValidCoordinate(pos)) return false;

            Piece piece = board.getPiece(pos);
            if (piece == null || piece instanceof Totem || piece.getSymbol() != targetSymbol) {
                return false;
            }
        }

        return true;
    }


    public static boolean isMovePossible(Board board) {
        int boardSize = board.getSize();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Position pos = new Position(i, j);
                if (board.isEmpty(pos)) {
                    return true;
                }
            }
        }
        return false;
    }
}