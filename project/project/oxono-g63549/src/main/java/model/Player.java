package model;

import Util.MoveStrategy;

public class Player {

    private BagPiece playerBag;
    private Color color;
    private MoveStrategy moveStrategy;

    public Player(Color color, MoveStrategy moveStrategy) {
        this.color = color;
        this.playerBag = new BagPiece(color);
        this.moveStrategy = moveStrategy;
    }

    public Player(Color color) {
        this(color, null);
    }



    public BagPiece getBag() {
        return playerBag;
    }

    public Color getColor() {
        return color;
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
        return "Player{color=" + color + ", automated=" + isAutomated() + "}";
    }

}
