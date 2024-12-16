package model;

import java.util.Objects;

public class Totem extends Piece {


    public Totem(Symbol symbol) {
        super(Color.BLUE, symbol);
    }


    public String toString() {
        return "Totem{" +
                "color=" + getColor() +
                ", symbol=" + getSymbol() +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Totem totem = (Totem) obj;
        return getSymbol() == totem.getSymbol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSymbol());
    }


}