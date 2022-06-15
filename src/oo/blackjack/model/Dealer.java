package oo.blackjack.model;

import java.util.List;

import static oo.blackjack.model.Hand.BLACK_JACK_VALUE;
import static oo.blackjack.model.PlayerStatus.BUSTED;
import static oo.blackjack.model.PlayerStatus.FINISHED;
import static oo.blackjack.model.PlayerStatus.PLAYING;

public class Dealer extends AbstractPlayer {

    private static final int TARGET_HAND_VALUE = 17;

    public Dealer() {
        super("Bank");
    }

    @Override
    public void draw(List<Card> deck) {
        if (status != PLAYING) {
            throw new IllegalStateException("Cannot draw in " + status + " status!");
        }
        hand.addCard(deck.remove(0));
        int value = hand.getValue();
        if (value >= TARGET_HAND_VALUE) {
            status = FINISHED;
        }
        if (value > BLACK_JACK_VALUE) {
            status = BUSTED;
        }
    }
}
