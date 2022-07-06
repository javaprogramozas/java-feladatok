package oo.blackjack.model.players;

public abstract class AbstractPlayer {

    private final String name;
    private final int targetHandValue;

    public AbstractPlayer(String name) {
        this(name, Integer.MAX_VALUE);
    }

    public AbstractPlayer(String name, int targetHandValue) {
        this.name = name;
        this.targetHandValue = targetHandValue;
    }

    public String getName() {
        return name;
    }

    public int getTargetHandValue() {
        return targetHandValue;
    }
}
