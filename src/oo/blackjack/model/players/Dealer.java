package oo.blackjack.model.players;

public class Dealer extends AbstractPlayer {

    private static final int TARGET_HAND_VALUE = 17;

    public Dealer() {
        super("Bank", TARGET_HAND_VALUE);
    }
}
