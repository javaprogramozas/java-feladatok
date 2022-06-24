package oo.blackjack.model.cards;

public record Card(Suit suit, Rank rank) {

    @Override
    public String toString() {
        return suit.name() + " " + rank.name();
    }
}
