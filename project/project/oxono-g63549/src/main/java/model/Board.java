package model;

import java.util.List;


public class Board {
    private final int size = 6;
    private Piece[][] board = new Piece[size][size];

    public Board() {
    }

    public boolean putPiece(Position pos, Piece piece) {
        validatePosition(pos);
        if (!isEmpty(pos)) {
            throw new IllegalStateException("Cell occupied");
        }
        board[pos.x()][pos.y()] = piece;
        return true;
    }

    public void removePiece(Position pos) {
        validatePosition(pos);
        board[pos.x()][pos.y()] = null;
    }

    public Piece getPiece(Position pos) {
        validatePosition(pos);
        return board[pos.x()][pos.y()];
    }

    public boolean isValidCoordinate(Position pos) {
        return pos.x() >= 0 && pos.x() < size && pos.y() >= 0 && pos.y() < size;
    }

    public boolean isEmpty(Position pos) {
        validatePosition(pos);
        return board[pos.x()][pos.y()] == null;
    }

    public void reset() {
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                board[i][j] = null;
            }
        }
    }

    public Position findTotemPosition(Totem totem) {
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                Piece p = board[i][j];
                if (p instanceof Totem t && t.equals(totem)) {
                    return new Position(i,j);
                }
            }
        }
        throw new IllegalStateException("Totem not found");
    }

    private void validatePosition(Position pos) {
        if (!isValidCoordinate(pos)) {
            throw new IllegalArgumentException("Invalid coordinate: " + pos);
        }
    }

    public List<Position> getValidTotemPositions(Totem totem) {
        return GameRules.getValidTotemPositions(this, totem);
    }


}



