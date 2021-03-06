package oo.blackjack.model.hand;

import oo.blackjack.model.cards.Card;
import oo.blackjack.model.cards.Deck;
import oo.blackjack.model.cards.Rank;
import oo.blackjack.model.players.AbstractPlayer;
import oo.blackjack.model.players.HumanPlayer;

import java.util.ArrayList;
import java.util.List;

import static oo.blackjack.model.hand.HandStatus.BLACKJACK;
import static oo.blackjack.model.hand.HandStatus.BUSTED;
import static oo.blackjack.model.hand.HandStatus.PLAYING;
import static oo.blackjack.model.hand.HandStatus.STANDING;

public class Hand {
    public static final int BLACK_JACK_VALUE = 21;
    private final List<Card> cards = new ArrayList<>();
    private final AbstractPlayer owner;
    protected HandStatus status = PLAYING;
    private int bet;
    private boolean splitted = false;
    private int index = 1;

    public Hand(AbstractPlayer owner, int bet) {
        this.owner = owner;
        this.bet = bet;
    }

    public void draw(Deck deck) {
        if (status != PLAYING) {
            throw new IllegalStateException("Cannot draw in " + status + " status!");
        }
        cards.add(deck.getCard());
        int value = getValue();
        if (value >= owner.getTargetHandValue()) {
            if (cards.size() == 2 && value == BLACK_JACK_VALUE) {
                status = BLACKJACK;
            } else {
                status = STANDING;
            }
        }
        if (value > BLACK_JACK_VALUE) {
            status = BUSTED;
        }
    }

    public int getValue() {
        int value = 0;
        int numberOfAces = 0;
        for (Card card : cards) {
            if (card.rank() != Rank.ACE) {
                value += card.rank().value;
            } else {
                numberOfAces++;
            }
        }
        for (int i = 0; i < numberOfAces; i++) {
            if (value + Rank.ACE.value > BLACK_JACK_VALUE) {
                value += 1;
            } else {
                value += Rank.ACE.value;
            }
        }
        return value;
    }

    @Override
    public String toString() {
        int value = getValue();
        String valueAsString;
        if (value == BLACK_JACK_VALUE && cards.size() == 2) {
            valueAsString = "BLACKJACK";
        } else {
            valueAsString = Integer.toString(value);
        }
        return String.format("%s: %s (%s)", getHandLabel(), cards, valueAsString);
    }

    public String getHandLabel() {
        String indexLabel = "";
        if (splitted) {
            indexLabel = " hand #" + index;
        }
        return owner.getName() + indexLabel;
    }

    public HandStatus getStatus() {
        return status;
    }

    public AbstractPlayer getOwner() {
        return owner;
    }

    // TODO refactor this
    public HumanPlayer getHumanOwner() {
        return (HumanPlayer) owner;
    }

    public int getNumberOfCards() {
        return cards.size();
    }

    public int getBet() {
        return bet;
    }

    public boolean isFirstCardAce() {
        return cards.get(0).rank() == Rank.ACE;
    }

    public void setStatus(HandStatus status) {
        if (this.status != HandStatus.PLAYING) {
            throw new IllegalStateException("Status can be set only for player in " + HandStatus.PLAYING + " status!");
        }
        this.status = status;
    }

    public void executeDoubleAction(Deck deck) {
        getHumanOwner().decreaseBudget(bet);
        draw(deck);
        bet *= 2;
        if (status == HandStatus.PLAYING) {
            status = HandStatus.STANDING;
        }
    }

    public boolean isSplittable() {
        return !splitted && cards.size() == 2 && cards.get(0).rank() == cards.get(1).rank();
    }

    public boolean isSplitted() {
        return splitted;
    }

    public Hand split(Deck deck) {
        if (status != PLAYING && isSplittable()) {
            throw new IllegalStateException("This hand is not splittable!");
        }
        this.splitted = true;
        Hand newHand = getHumanOwner().createHand(bet).orElseThrow();
        newHand.cards.add(this.cards.remove(1));
        newHand.splitted = true;
        newHand.index = this.index + 1;
        this.draw(deck);
        newHand.draw(deck);
        return newHand;
    }
}
