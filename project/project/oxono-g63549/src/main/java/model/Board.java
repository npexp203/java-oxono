package model;

public class Board {

    private Pieces[][] board;

    public Board() {
        board = new Pieces[6][6];

    }

    public void putPieces(int x, int y, Pieces pieces) {
        if (isValidCoordinate(x,y)){
            board[x][y] = pieces;
        }
        else {
            throw new IllegalArgumentException("Invalid coordinates: (" + x + ", " + y + ")");
            }

        }

    }
    public boolean isValidCoordinate(int x, int y){
        return x >= 0 && x < board.length && y >= 0 && y < board[0].length
    }

    public boolean isFull() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) {
                    return false;
                }

            }
        }
        return true;
    }

    public boolean checkWin(Pieces pieces) {
        int rows = board.length;
        int cols = board[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (j <= cols - 4 && board[i][j] != null && board[i][j].equals(pieces) && board[i][j + 1].equals(pieces) && board[i][j + 2].equals(pieces) && board[i][j + 3].equals(pieces))
                    return true;
                if (i <= rows - 4 && board[i][j] != null && board[i][j].equals(pieces) && board[i + 1][j].equals(pieces) && board[i + 2][j].equals(pieces) && board[i + 3][j].equals(pieces))
                    return true;
            }
        }

        return false;
    }

    public void reset() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = null;
            }


        }

    }
}
