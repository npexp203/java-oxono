package model;

public class Token extends Piece{
    public Token(Colors colors, Symbol symbol) {
        super(colors, symbol);
    }
    @Override
    public String toString() {
        return "Token{" +
                "color=" + getColor() +
                ", symbol=" + getSymbol() +
                '}';
    }
}
