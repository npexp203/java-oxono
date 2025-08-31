package model;


import java.util.ArrayList;
import java.util.List;

public class GameRules {

    /**
     * Checks if a totem is surrounded (all adjacent cells occupied).
     */
    public static boolean isEnclaved(Board board, Position pos) {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (int[] dir : directions) {
            Position adjacent = new Position(pos.x() + dir[0], pos.y() + dir[1]);
            if (board.isValidCoordinate(adjacent) && board.isEmpty(adjacent)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the move is rectilinear (same row or same column).
     */
    public static boolean isRectilinearMove(Position start, Position target) {
        return start.x() == target.x() || start.y() == target.y();
    }

    /**
     * Checks if the path between start and target is clear.
     */
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

    /**
     * Handles the jump in case of enclosure.
     * Returns the final position if a jump is possible, otherwise null.
     */
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

    /**
     * Moves the totem on the board, checking the rules.
     */
    public static boolean moveTotem(Board board, Totem totem, Position target) {
        Position start = board.findTotemPosition(totem);
        if (start == null) {
            throw new IllegalStateException("The totem is not on the board.");
        }

        if (isEnclaved(board, start)) {
            int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            for (int[] dir : directions) {
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
    private static final int[][] DIRECTIONS = {{0,1},{1,0},{0,-1},{-1,0}};



        /**
         * Returns the list of valid positions for the totem from its current position on the board.
         */
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

            // Normal movement (horizontal/vertical)
            for (int[] dir : DIRECTIONS) {
                int x = start.x() + dir[0];
                int y = start.y() + dir[1];
                while (board.isValidCoordinate(new Position(x,y))) {
                    Position current = new Position(x,y);
                    if (board.getPiece(current) != null) {
                        break; // We meet a piece, we stop
                    }
                    validMoves.add(current);
                    x += dir[0];
                    y += dir[1];
                }
            }
            return validMoves;
        }

        /**
         * Returns the list of valid positions for placing a token around the totem,
         * from its current position on the board.
         * (Not used by the strategy, but kept in case of future needs.)
         */

    /**
     * Returns the list of valid positions for placing a token around a given position (totemPos).
     * This allows to calculate the positions of tokens based on the final position where the totem will be moved.
     */
    public static List<Position> getValidTokenPositionsForPosition(Board board, Position totemPos) {
        List<Position> validPositions = new ArrayList<>();

        // Vérifier les positions adjacentes normales
        for (int[] dir : DIRECTIONS) {
            Position adj = new Position(totemPos.x() + dir[0], totemPos.y() + dir[1]);
            if (board.isValidCoordinate(adj) && board.isEmpty(adj)) {
                validPositions.add(adj);
            }
        }

        // Si aucune position adjacente valide ET que le totem est enclavé
        if (validPositions.isEmpty() && isEnclaved(board, totemPos)) {
            // Règle B : placer sur n'importe quelle case libre du plateau
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    Position pos = new Position(i, j);
                    if (board.isEmpty(pos)) {
                        validPositions.add(pos);
                    }
                }
            }
        }

        return validPositions;
    }

    /**
     * Checks if the placement of a token is valid with respect to the current position of the totem on the board.
     */
    public static boolean isValidTokenPlacement(Board board, Position totemPos, Position tokenPos) {
        if (!board.isValidCoordinate(tokenPos) || !board.isEmpty(tokenPos)) {
            return false;
        }

        // Vérifier adjacence normale
        int dx = Math.abs(totemPos.x() - tokenPos.x());
        int dy = Math.abs(totemPos.y() - tokenPos.y());
        boolean isAdjacent = (dx == 1 && dy == 0) || (dx == 0 && dy == 1);

        if (isAdjacent) {
            return true;
        }

        // Si pas adjacent, vérifier si le totem est enclavé (règle B)
        if (isEnclaved(board, totemPos)) {
            return true; // Peut placer n'importe où
        }

        return false;
    }
    /**
     * Checks if a totem movement to a given position is valid.
     *
     * @param board    The current game board.
     * @param totem    The totem we want to move.
     * @param position The target position for the move.
     * @return true if the move is valid, otherwise false.
     */
    public boolean isValidMove(Board board, Totem totem, Position position) {
        // Checks if the target position is valid on the board
        if (!board.isValidCoordinate(position)) {
            return false;
        }

        // Checks if the target cell is empty
        if (!board.isEmpty(position)) {
            return false;
        }

        // Checks if the move respects the rules of totem movement
        Position totemPosition = board.findTotemPosition(totem);
        int dx = Math.abs(position.x() - totemPosition.x());
        int dy = Math.abs(position.y() - totemPosition.y());

        // Example: A totem can only move 1 cell in one direction
        if ((dx <= 1) && (dy <= 1)) {
            return true;
        }

        return false;
    }


}