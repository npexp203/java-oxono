package model;

public class Token extends Piece{
    public Token(Color color, Symbol symbol) {
        super(color, symbol);
    }
    @Override
    public String toString() {
        return "Token{" +
                "color=" + getColor() +
                ", symbol=" + getSymbol() +
                '}';
    }
}
