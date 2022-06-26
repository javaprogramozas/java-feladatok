package oo.blackjack.model.players;

import oo.blackjack.model.cards.Deck;
import oo.blackjack.model.cards.Rank;

import static oo.blackjack.model.players.Hand.BLACK_JACK_VALUE;
import static oo.blackjack.model.players.PlayerStatus.BLACKJACK;
import static oo.blackjack.model.players.PlayerStatus.BUSTED;
import static oo.blackjack.model.players.PlayerStatus.PLAYING;
import static oo.blackjack.model.players.PlayerStatus.STANDING;

public class Dealer extends AbstractPlayer {

    private static final int TARGET_HAND_VALUE = 17;

    public Dealer() {
        super("Bank");
    }

    @Override
    public void draw(Deck deck) {
        if (status != PLAYING) {
            throw new IllegalStateException("Cannot draw in " + status + " status!");
        }
        hand.addCard(deck.getCard());
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

    public void resetHand() {
        hand = new Hand(0);
        status = PLAYING;
    }

    public boolean isFirstCardAce() {
        return hand.getCard(0).rank() == Rank.ACE;
    }
}
