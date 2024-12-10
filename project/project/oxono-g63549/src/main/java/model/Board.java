package model;

public class Board {

    private Piece[][] board;

    public Board() {
        board = new Piece[6][6];

    }

    public boolean putPieces(Position pos, Piece pieces) {
        if (!isValidCoordinate(pos)) {
            throw new IllegalArgumentException("Invalid coordinates: (" + pos.x() + ", " + pos.y() + ")");
        }
        if (!isEmpty(pos)) {
            throw new IllegalStateException("Cell is already occupied at: (" + pos.x() + ", " + pos.y() + ")");
        }
        board[pos.x()][pos.y()] = pieces;
        return true;
    }

    public boolean isValidCoordinate(Position pos){
        return pos.x() >= 0 && pos.x() < board.length && pos.y() >= 0 && pos.y() < board[0].length;
    }

    public boolean isEmpty(Position pos) {
        if (!isValidCoordinate(pos)) {
            throw new IllegalArgumentException("Invalid coordinates: (" + pos.x() + ", " + pos.y() + ")");
        }
        return board[pos.x()][pos.y()] == null;
    }

    public void reset() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = null;
            }


        }

    }
}
