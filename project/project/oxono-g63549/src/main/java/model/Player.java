package model;

public class Player {

    private BagPiece playerBag;
    private Color color;
    private boolean hasMovedTotem;


    public Player(Color color) {
        this.color = color;
        playerBag = new BagPiece(color);
    }


    public Piece playPiece(Symbol symbol){
        return playerBag.removePieceFromBag(symbol);
    }

    public boolean canPlacePiece(){

        return hasMovedTotem;
    }



}
