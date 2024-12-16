package model;

import java.util.List;

/**
 * Represents the game board.
 * It contains the positions of all pieces and provides methods to manipulate them.
 */
public class Board {
    private final int size = 6;
    private Piece[][] board = new Piece[size][size];

    public Board() {
    }

    /**
     * Tries to put a piece at the given position.
     *
     * @param pos   The position to place the piece.
     * @param piece The piece to place.
     * @return true if the piece was placed, false otherwise.
     * @throws IllegalStateException if the cell is already occupied.
     */
    public boolean putPiece(Position pos, Piece piece) {
        validatePosition(pos);
        if (!isEmpty(pos)) {
            throw new IllegalStateException("Cell occupied");
        }
        board[pos.x()][pos.y()] = piece;
        return true;
    }

    /**
     * Removes a piece from the given position.
     *
     * @param pos The position of the piece to remove.
     */
    public void removePiece(Position pos) {
        validatePosition(pos);
        board[pos.x()][pos.y()] = null;
    }

    /**
     * Returns the piece at the given position.
     *
     * @param pos The position of the piece.
     * @return The piece at the given position or null.
     */
    public Piece getPiece(Position pos) {
        validatePosition(pos);
        return board[pos.x()][pos.y()];
    }

    /**
     * Checks if the given position is a valid coordinate.
     *
     * @param pos The position to check.
     * @return true if the position is valid, false otherwise.
     */
    public boolean isValidCoordinate(Position pos) {
        return pos.x() >= 0 && pos.x() < size && pos.y() >= 0 && pos.y() < size;
    }

    /**
     * Checks if the given position is empty.
     *
     * @param pos The position to check.
     * @return true if the position is empty, false otherwise.
     */
    public boolean isEmpty(Position pos) {
        validatePosition(pos);
        return board[pos.x()][pos.y()] == null;
    }

    /**
     * Resets the board to its initial state.
     */
    public void reset() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = null;
            }
        }
    }

    /**
     * Finds the position of the given totem.
     *
     * @param totem The totem to find.
     * @return The position of the totem.
     * @throws IllegalStateException if the totem is not found.
     */
    public Position findTotemPosition(Totem totem) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Piece p = board[i][j];
                if (p instanceof Totem t && t.equals(totem)) {
                    return new Position(i, j);
                }
            }
        }
        throw new IllegalStateException("Totem not found");
    }

    /**
     * Returns the valid positions for the given totem.
     *
     * @param totem The totem for which to find valid positions.
     * @return The list of valid positions.
     */
    public List<Position> getValidTotemPositions(Totem totem) {
        return GameRules.getValidTotemPositions(this, totem);
    }

    private void validatePosition(Position pos) {
        if (!isValidCoordinate(pos)) {
            throw new IllegalArgumentException("Invalid coordinate: " + pos);
        }
    }
}