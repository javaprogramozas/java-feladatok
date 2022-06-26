package oo.blackjack.model.players;

import oo.blackjack.model.cards.Deck;

import static oo.blackjack.model.players.Hand.BLACK_JACK_VALUE;
import static oo.blackjack.model.players.PlayerStatus.BLACKJACK;
import static oo.blackjack.model.players.PlayerStatus.BUSTED;
import static oo.blackjack.model.players.PlayerStatus.PLAYING;
import static oo.blackjack.model.players.PlayerStatus.SKIPPED;
import static oo.blackjack.model.players.PlayerStatus.STANDING;

public abstract class AbstractPlayer {

    private final String name;
    protected PlayerStatus status = PLAYING;
    protected Hand hand;

    public AbstractPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void draw(Deck deck) {
        if (status != PLAYING) {
            throw new IllegalStateException("Cannot draw in " + status + " status!");
        }
        hand.addCard(deck.getCard());
        int value = hand.getValue();
        if (value > BLACK_JACK_VALUE) {
            status = BUSTED;
        }
        if (value == BLACK_JACK_VALUE) {
            if (hand.getNumberOfCards() == 2) {
                status = BLACKJACK;
            } else {
                status = STANDING;
            }
        }
    }

    @Override
    public String toString() {
        if (status == SKIPPED) {
            return name + " skipped";
        }
        return name + ": " + hand;
    }

    public int getHandValue() {
        return hand.getValue();
    }

    public Hand getHand() {
        return hand;
    }
}
