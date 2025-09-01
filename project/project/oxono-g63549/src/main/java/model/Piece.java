package model;

public abstract class Piece {
    private final Colors colors;
    private final Symbol symbol;



    public Piece(Colors colors, Symbol symbol){
        if(colors == null || symbol == null){
            throw new IllegalArgumentException("Color and Symbol can not be null");
        }
        this.colors = colors;
        this.symbol = symbol;
    }

    public Colors getColor() {
        return colors;
    }

    public  Symbol getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + colors +
                ", symbol=" + symbol +
                '}';
    }

}
