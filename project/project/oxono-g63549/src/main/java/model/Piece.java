package model;

public abstract class Piece {
    private final Color color;
    private final Symbol symbol;



    public Piece(Color color, Symbol symbol){
        if(color == null && symbol == null){
            throw new IllegalArgumentException("Color and Symbol can not be null");
        }
        this.color = color;
        this.symbol = symbol;
    }

    public Color getColor() {
        return color;
    }

    public  Symbol getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                ", symbol=" + symbol +
                '}';
    }

}
