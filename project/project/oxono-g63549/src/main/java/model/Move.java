package model;

public class    Move {
    private final Position totemTarget;
    private final Position tokenTarget;
    private final Symbol tokenSymbol;

    public Move(Position totemTarget, Position tokenTarget, Symbol tokenSymbol) {
        this.totemTarget = totemTarget;
        this.tokenTarget = tokenTarget;
        this.tokenSymbol = tokenSymbol;
    }

    public Position getTotemTarget() {
        return totemTarget;
    }

    public Position getTokenTarget() {
        return tokenTarget;
    }

    public Symbol getTokenSymbol() {
        return tokenSymbol;
    }
}

