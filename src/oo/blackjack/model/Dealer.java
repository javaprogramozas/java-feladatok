package oo.blackjack.model;

import java.util.List;

import static oo.blackjack.model.Hand.BLACK_JACK_VALUE;
import static oo.blackjack.model.PlayerStatus.BLACKJACK;
import static oo.blackjack.model.PlayerStatus.BUSTED;
import static oo.blackjack.model.PlayerStatus.PLAYING;
import static oo.blackjack.model.PlayerStatus.STANDING;

public class Dealer extends AbstractPlayer {

    private static final int TARGET_HAND_VALUE = 17;

    public Dealer() {
        super("Bank");
        hand = new Hand(0);
    }

    @Override
    public void draw(List<Card> deck) {
        if (status != PLAYING) {
            throw new IllegalStateException("Cannot draw in " + status + " status!");
        }
        hand.addCard(deck.remove(0));
        int value = hand.getValue();
        if (value >= TARGET_HAND_VALUE) {
            if (hand.getNumberOfCards() == 2 && value == BLACK_JACK_VALUE) {
                status = BLACKJACK;
            } else {
                status = STANDING;
            }
        }
        if (value > BLACK_JACK_VALUE) {
            status = BUSTED;
        }
    }

    @Override
    public List<Action> getAvailableActions() {
        return List.of();
    }

    @Override
    public void apply(Action action, List<Card> deck) {
        throw new UnsupportedOperationException("The bank has internal decision making!");
    }
}
