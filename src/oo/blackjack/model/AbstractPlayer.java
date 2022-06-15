package oo.blackjack.model;

import java.util.List;

import static oo.blackjack.model.Hand.BLACK_JACK_VALUE;
import static oo.blackjack.model.PlayerStatus.BUSTED;
import static oo.blackjack.model.PlayerStatus.FINISHED;
import static oo.blackjack.model.PlayerStatus.PLAYING;

public abstract class AbstractPlayer {

    private final String name;
    protected PlayerStatus status = PLAYING;
    protected Hand hand = new Hand();

    public AbstractPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void draw(List<Card> deck) {
        if (status != PLAYING) {
            throw new IllegalStateException("Cannot draw in " + status + " status!");
        }
        hand.addCard(deck.remove(0));
        int value = hand.getValue();
        if (value > BLACK_JACK_VALUE) {
            status = BUSTED;
        }
        if (value == BLACK_JACK_VALUE) {
            status = FINISHED;
        }
    }
}
