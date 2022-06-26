package oo.blackjack.model;

public record RoundResults(String message, double multiplier, boolean dealerHadBlackjack) {

    public RoundResults(String message, double multiplier) {
        this(message, multiplier, false);
    }

}
