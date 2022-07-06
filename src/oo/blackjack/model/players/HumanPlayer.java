package oo.blackjack.model.players;

import oo.blackjack.model.RoundResults;
import oo.blackjack.model.hand.Hand;

import java.util.Optional;

public class HumanPlayer extends AbstractPlayer {

    private int budget;
    private int insurance;

    public HumanPlayer(String name) {
        super(name);
        this.budget = 50;
    }

    public Optional<Hand> createHand(int bet) {
        if (bet < 0) {
            throw new IllegalArgumentException("Bet cannot be negative!");
        }
        if (bet > budget) {
            throw new IllegalArgumentException("Bet (" + bet + ") cannot be greater than player budget (" + budget + ")");
        }
        Hand hand = null;
        if (bet != 0) {
            hand = new Hand(this, bet);
            budget -= bet;
        }
        return Optional.ofNullable(hand);
    }

    public void setInsurance(int insurance, double maxInsurance) {
        if (insurance <= 0) {
            throw new IllegalArgumentException("Insurance must be positive!");
        }
        if (insurance > budget) {
            throw new IllegalArgumentException("Insurance (" + insurance + ") cannot be greater than player budget (" + budget + ")");
        }
        if (insurance > maxInsurance) {
            throw new IllegalArgumentException("Insurance (" + insurance + ") cannot be greater than half of bet (" + maxInsurance + ")");
        }
        budget -= insurance;
        this.insurance = insurance;
    }

    public void collectReward(RoundResults results, Hand hand) {
        budget += hand.getBet() * results.multiplier();
        if (insurance > 0 && results.dealerHadBlackjack()) {
            budget += insurance * 2;
            insurance = 0;
        }
    }

    public int getBudget() {
        return budget;
    }

    public void decreaseBudget(int amount) {
        if (budget < amount) {
            throw new IllegalStateException("Cannot substract amount (" + amount + ") from remaining budget " + budget);
        }
        budget -= amount;
    }
}
