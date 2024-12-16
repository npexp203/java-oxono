package model;


import java.util.ArrayList;
import java.util.List;

public class GameRules {

    /**
     * Vérifie si un totem est enclavé (toutes les cases adjacentes occupées).
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
     * Vérifie si le déplacement est rectiligne (même ligne ou même colonne).
     */
    public static boolean isRectilinearMove(Position start, Position target) {
        return start.x() == target.x() || start.y() == target.y();
    }

    /**
     * Vérifie si le chemin entre start et target est libre.
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
     * Gère le saut en cas d'enclavement.
     * Retourne la position finale si un saut est possible, sinon null.
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
     * Effectue le déplacement du totem sur le board, en vérifiant les règles.
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
         * Renvoie la liste des positions valides pour le totem donné à partir de sa position actuelle sur le board.
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

            // Déplacement normal (horizontal/vertical)
            for (int[] dir : DIRECTIONS) {
                int x = start.x() + dir[0];
                int y = start.y() + dir[1];
                while (board.isValidCoordinate(new Position(x,y))) {
                    Position current = new Position(x,y);
                    if (board.getPiece(current) != null) {
                        break; // On rencontre une pièce, on s'arrête
                    }
                    validMoves.add(current);
                    x += dir[0];
                    y += dir[1];
                }
            }
            return validMoves;
        }

        /**
         * Renvoie la liste des positions valides pour placer un token autour du totem,
         * à partir de la position actuelle du totem sur le board.
         * (Non utilisée par la stratégie, mais conservée si besoin.)
         */

    /**
     * Renvoie les positions valides pour placer un token autour d'une position donnée (totemPos).
     * Ceci permet de calculer les positions de token en se basant sur la position finale où le totem sera déplacé.
     */
    public static List<Position> getValidTokenPositionsForPosition(Board board, Position totemPos) {
        List<Position> validPositions = new ArrayList<>();
        for (int[] dir : DIRECTIONS) {
            Position adj = new Position(totemPos.x()+dir[0], totemPos.y()+dir[1]);
            if (board.isValidCoordinate(adj) && board.isEmpty(adj)) {
                validPositions.add(adj);
            }
        }
        return validPositions;
    }

    /**
     * Vérifie si le placement du token est valide par rapport à la position actuelle du totem sur le plateau.
     */
    public static boolean isValidTokenPlacement(Board board, Position totemPos, Position tokenPos) {
        if (!board.isValidCoordinate(tokenPos) || !board.isEmpty(tokenPos)) {
            return false;
        }
        int dx = Math.abs(totemPos.x() - tokenPos.x());
        int dy = Math.abs(totemPos.y() - tokenPos.y());
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }

}

