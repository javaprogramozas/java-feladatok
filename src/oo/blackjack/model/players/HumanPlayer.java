package oo.blackjack.model.players;

import oo.blackjack.model.RoundResults;
import oo.blackjack.model.cards.Deck;

public class HumanPlayer extends AbstractPlayer {

    private int budget;
    private int insurance;

    public HumanPlayer(String name) {
        super(name);
        this.budget = 50;
    }

    public void createHand(int bet) {
        if (bet < 0) {
            throw new IllegalArgumentException("Bet cannot be negative!");
        }
        if (bet > budget) {
            throw new IllegalArgumentException("Bet (" + bet + ") cannot be greater than player budget (" + budget + ")");
        }
        if (bet != 0) {
            hand = new Hand(bet);
            budget -= bet;
            status = PlayerStatus.PLAYING;
        } else {
            status = PlayerStatus.SKIPPED;
        }
    }

    public void setInsurance(int insurance) {
        if (insurance <= 0) {
            throw new IllegalArgumentException("Insurance must be positive!");
        }
        if (insurance > budget) {
            throw new IllegalArgumentException("Insurance (" + insurance + ") cannot be greater than player budget (" + budget + ")");
        }
        double maxInsurance = hand.getBet() * 0.5;
        if (insurance > maxInsurance) {
            throw new IllegalArgumentException("Insurance (" + insurance + ") cannot be greater than half of bet (" + maxInsurance + ")");
        }
        budget -= insurance;
        this.insurance = insurance;
    }

    public void collectReward(RoundResults results) {
        if (status != PlayerStatus.SKIPPED) {
            budget += hand.getBet() * results.multiplier();
            hand = null;
            if (insurance > 0 && results.dealerHadBlackjack()) {
                budget += insurance * 2;
                insurance = 0;
            }
        }
    }

    public int getBudget() {
        return budget;
    }

    public void setStatus(PlayerStatus status) {
        if (status != PlayerStatus.PLAYING) {
            throw new IllegalStateException("Status can be set only for player in " + PlayerStatus.PLAYING + " status!");
        }
    }

    public void executeDoubleAction(Deck deck) {
        draw(deck);
        budget -= hand.getBet();
        hand.doubleBet();
        if (status == PlayerStatus.PLAYING) {
            status = PlayerStatus.STANDING;
        }
    }
}
