package oo.blackjack;

import oo.blackjack.model.RoundResults;
import oo.blackjack.model.cards.Deck;
import oo.blackjack.model.players.Action;
import oo.blackjack.model.players.Dealer;
import oo.blackjack.model.hand.Hand;
import oo.blackjack.model.players.HumanPlayer;
import oo.blackjack.model.hand.HandStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.IntFunction;

public class Blackjack {

    private static final List<Action> DEFAULT_ACTIONS = List.of(Action.HIT, Action.STAND);

    private Deck deck = new Deck(1);
    private Dealer dealer = new Dealer();
    private List<HumanPlayer> players = new ArrayList<>();
    private Hand dealerHand;
    private List<Hand> playerHands;
    private Scanner scanner = new Scanner(System.in);

    public void setup() {
        System.out.println("Blackjack game");
        while (true) {
            try {
                System.out.print("How many players would like to play? ");
                String userInput = scanner.nextLine();
                int numberOfPlayers = Integer.parseInt(userInput);

                for (int i = 1; i <= numberOfPlayers; i++) {
                    System.out.print("Name of player #" + i + "? ");
                    userInput = scanner.nextLine();
                    if (userInput.isBlank()) {
                        userInput = "Player #" + i;
                    }
                    players.add(new HumanPlayer(userInput));
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please try again!");
            }
        }
    }

    public void play() {
        while (hasPlayerWithBudget()) {
            collectBetsAndCreateHands();
            if (playerHands.isEmpty()) {
                break;
            }
            initialDraws();
            actionDraws();
            evaluateRound();
        }
        displayPostGameStatus();
    }

    private boolean hasPlayerWithBudget() {
        for (HumanPlayer player : players) {
            if (player.getBudget() > 0) {
                return true;
            }
        }
        return false;
    }

    private void collectBetsAndCreateHands() {
        System.out.println("Please make your bets!");
        System.out.println("(place 0 bet, if you would like to skip this round)");
        playerHands = new ArrayList<>();
        for (HumanPlayer player : players) {
            IntFunction<Optional<Hand>> function = new IntFunction<>() {
                @Override
                public Optional<Hand> apply(int value) {
                    return player.createHand(value);
                }
            };
            Optional<Hand> optionalHand
                    = getPlayerBetOrInsurance(String.format("%s's bet (0 - %d): ", player.getName(), player.getBudget()), function);
            if (optionalHand.isPresent()) {
                playerHands.add(optionalHand.get());
            }
        }
        dealerHand = new Hand(dealer, 0);
    }

    private void initialDraws() {
        for (Hand hand : playerHands) {
            hand.draw(deck);
        }
        dealerHand.draw(deck);
        for (Hand hand : playerHands) {
            hand.draw(deck);
        }
    }

    private void actionDraws() {
        for (int i = 0; i < playerHands.size(); i++) {
            Hand hand = playerHands.get(i);
            System.out.println(hand);
            while (hand.getStatus() == HandStatus.PLAYING) {
                List<Action> actions = getHandActions(hand);
                System.out.print("Actions: " + getActionLabels(actions) + "? ");
                String userInput = scanner.nextLine();
                Optional<Action> selectedAction = findActionByCommand(actions, userInput);
                if (selectedAction.isPresent()) {
                    Optional<Hand> optionalHand = apply(hand, selectedAction.get());
                    if (optionalHand.isPresent()) {
                        playerHands.add(i + 1, optionalHand.get());
                    }
                    System.out.println(hand);
                } else {
                    System.out.println("Unknown action command: " + userInput);
                }
            }
        }

        // Dealer should draw cards
        if (isAnyPlayerIn(playerHands, Set.of(HandStatus.BLACKJACK, HandStatus.STANDING))) {
            while (dealerHand.getStatus() == HandStatus.PLAYING) {
                dealerHand.draw(deck);
            }
            System.out.println(dealerHand);
        } else {
            System.out.println(dealer.getName() + " skips drawing cards");
        }
    }

    private void evaluateRound() {
        for (Hand playerHand : playerHands) {
            HumanPlayer player = playerHand.getHumanOwner();
            String handLabel = playerHand.getHandLabel();
            RoundResults results = switch (playerHand.getStatus()) {
                case BUSTED -> new RoundResults(handLabel + " busted and lost", 0, dealerHand.getStatus() == HandStatus.BLACKJACK);
                case SURRENDERED -> new RoundResults(handLabel + " surrendered", 0.5);
                case BLACKJACK -> handlePlayerBlackJack(playerHand, dealerHand);
                case STANDING -> handlePlayerStanding(playerHand, dealerHand);
                case PLAYING -> throw new IllegalStateException(handLabel + " should not be in " + playerHand.getStatus() + " status");
            };
            player.collectReward(results, playerHand);
            System.out.println(results.message());
            if (lastHandOf(playerHand, player)) {
                System.out.println(player.getName() + "'s budget: " + player.getBudget());
            }
        }
    }

    private boolean lastHandOf(Hand hand, HumanPlayer player) {
        int currentIndex = playerHands.indexOf(hand);
        return !(currentIndex + 1 < playerHands.size() && playerHands.get(currentIndex + 1).getOwner().equals(player));
    }

    private void displayPostGameStatus() {
        players.sort(new PlayerBudgetComparator().reversed());
        for (HumanPlayer player : players) {
            System.out.println(player.getName() + " finished with " + player.getBudget() + " tokens");
        }
    }

    private List<Action> getHandActions(Hand hand) {
        HumanPlayer player = hand.getHumanOwner();
        if (hand.getStatus() != HandStatus.PLAYING) {
            throw new IllegalStateException("There are no actions in " + hand.getStatus() + " status!");
        }
        List<Action> actions = new ArrayList<>(DEFAULT_ACTIONS);
        if (hand.getNumberOfCards() == 2) {
            if (!hand.isSplitted()) {
                actions.add(Action.SURRENDER);
            }
            if (!hand.isSplitted() && player.getBudget() >= hand.getBet()) {
                actions.add(Action.DOUBLE);
            }
            if (dealerHand.isFirstCardAce() && player.getBudget() > 0) {
                actions.add(Action.INSURANCE);
            }
            if (hand.isSplittable() && player.getBudget() >= hand.getBet()) {
                actions.add(Action.SPLIT);
            }
        }
        return actions;
    }

    private Optional<Hand> apply(Hand hand, Action action) {
        HandStatus status = hand.getStatus();
        if (status != HandStatus.PLAYING) {
            throw new IllegalStateException("No actions should be applied in " + status + " status!");
        }
        Hand newHand = null;
        switch (action) {
            case HIT -> hand.draw(deck);
            case STAND -> hand.setStatus(HandStatus.STANDING);
            case SURRENDER -> hand.setStatus(HandStatus.SURRENDERED);
            case DOUBLE -> hand.executeDoubleAction(deck);
            case INSURANCE -> executeInsuranceAction(hand);
            case SPLIT -> newHand = hand.split(deck);
        }
        return Optional.ofNullable(newHand);
    }

    private void executeInsuranceAction(Hand hand) {
        double maxInsurance = hand.getBet() * 0.5;
        HumanPlayer player = hand.getHumanOwner();
        IntFunction<Void> function = new IntFunction<>() {
            @Override
            public Void apply(int value) {
                player.setInsurance(value, maxInsurance);
                return null;
            }
        };
        int maxPossibleInsurance = Math.min((int)maxInsurance, player.getBudget());
        getPlayerBetOrInsurance(String.format("%s's insurance (1 - %d): ", player.getName(), maxPossibleInsurance), function);
    }

    private <T> T getPlayerBetOrInsurance(String label, IntFunction<T> function) {
        while (true) {
            try {
                System.out.print(label);
                String userInput = scanner.nextLine();
                return function.apply(Integer.parseInt(userInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please try again!");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Blackjack game = new Blackjack();
        game.setup();
        game.play();
    }

    private static boolean isAnyPlayerIn(List<Hand> hands, Set<HandStatus> desiredStatuses) {
        for (Hand hand : hands) {
            if (desiredStatuses.contains(hand.getStatus())) {
                return true;
            }
        }
        return false;
    }

    private static RoundResults handlePlayerStanding(Hand playerHand, Hand dealerHand) {
        String playerName = playerHand.getHandLabel();
        String dealerName = dealerHand.getOwner().getName();
        if (dealerHand.getStatus() == HandStatus.BUSTED) {
            return new RoundResults(playerName + " won, because " + dealerName + " busted", 2);
        }
        boolean dealerHadBlackjack = dealerHand.getStatus() == HandStatus.BLACKJACK;
        if (dealerHand.getValue() > playerHand.getValue()) {
            return new RoundResults(playerName + " lost to " + dealerName + " by having less points", 0, dealerHadBlackjack);
        } else if (dealerHand.getValue() == playerHand.getValue()) {
            if (dealerHadBlackjack) {
                return new RoundResults(playerName + " lost to " + dealerName + " by having black jack", 0, true);
            } else {
                return new RoundResults(playerName + " is in tie with " + dealerName, 1, false);
            }
        } else {
            return new RoundResults(playerName + " won", 2, dealerHadBlackjack);
        }
    }

    private static RoundResults handlePlayerBlackJack(Hand playerHand, Hand dealerHand) {
        if (dealerHand.getStatus() == HandStatus.BLACKJACK) {
            return new RoundResults(playerHand.getHandLabel() + " lost, because " + dealerHand.getOwner().getName() + " has BLACKJACK too", 0, true);
        } else {
            return new RoundResults(playerHand.getHandLabel() + " won with BLACKJACK", 2.5);
        }
    }

    private static Optional<Action> findActionByCommand(List<Action> actions, String userInput) {
        for (Action action : actions) {
            if (Character.toString(action.command).equalsIgnoreCase(userInput)) {
                return Optional.of(action);
            }
        }
        return Optional.empty();
    }

    private static String getActionLabels(List<Action> actions) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Action action : actions) {
            joiner.add(action.label);
        }
        return joiner.toString();
    }
}
