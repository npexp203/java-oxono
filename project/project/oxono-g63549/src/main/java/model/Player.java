package model;

import Util.MoveStrategy;

public class Player {

    private BagPiece playerBag;
    private Colors colors;
    private MoveStrategy moveStrategy;

    public Player(Colors colors, MoveStrategy moveStrategy) {
        this.colors = colors;
        this.playerBag = new BagPiece(colors);
        this.moveStrategy = moveStrategy;
    }

    public Player(Colors colors) {
        this(colors, null);
    }



    public BagPiece getBag() {
        return playerBag;
    }

    public Colors getColor() {
        return colors;
    }
    public MoveStrategy getMoveStrategy() {
        return moveStrategy;
    }


    public boolean isAutomated() {
        return moveStrategy != null;
    }
    public int countSymbol(Symbol symbol) {
        return playerBag.countSymbol(symbol);
    }
    public String toString() {
        return "Player{color=" + colors + ", automated=" + isAutomated() + "}";
    }

}
