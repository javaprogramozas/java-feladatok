package oo.blackjack;

import oo.blackjack.model.RoundResults;
import oo.blackjack.model.cards.Deck;
import oo.blackjack.model.players.AbstractPlayer;
import oo.blackjack.model.players.Action;
import oo.blackjack.model.players.Dealer;
import oo.blackjack.model.players.Hand;
import oo.blackjack.model.players.HumanPlayer;
import oo.blackjack.model.players.PlayerStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.IntConsumer;

public class Blackjack {

    private static final List<Action> DEFAULT_ACTIONS = List.of(Action.HIT, Action.STAND);

    private Deck deck = new Deck(1);
    private Dealer dealer = new Dealer();
    private List<HumanPlayer> players = new ArrayList<>();
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
            collectBets();
            if (!hasPlayerWillingToPlay()) {
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

    private boolean hasPlayerWillingToPlay() {
        for (HumanPlayer player : players) {
            if (player.getStatus() == PlayerStatus.PLAYING) {
                return true;
            }
        }
        return false;
    }

    private void collectBets() {
        System.out.println("Please make your bets!");
        System.out.println("(place 0 bet, if you would like to skip this round)");
        for (HumanPlayer player : players) {
            IntConsumer consumer = new IntConsumer() {
                @Override
                public void accept(int value) {
                    player.createHand(value);
                }
            };
            getPlayerBetOrInsurance(String.format("%s's bet (0 - %d): ", player.getName(), player.getBudget()), consumer);
        }
        dealer.resetHand();
    }

    private void initialDraws() {
        List<AbstractPlayer> firstRoundOfDraws = new ArrayList<>(players);
        firstRoundOfDraws.add(dealer);
        firstRoundOfDraws.addAll(players);
        drawAllPlayers(deck, firstRoundOfDraws);
    }

    private void actionDraws() {
        for (HumanPlayer player : players) {
            System.out.println(player);
            while (player.getStatus() == PlayerStatus.PLAYING) {
                List<Action> actions = getPlayerActions(player);
                System.out.print("Actions: " + getActionLabels(actions) + "? ");
                String userInput = scanner.nextLine();
                Optional<Action> selectedAction = findActionByCommand(actions, userInput);
                if (selectedAction.isPresent()) {
                    apply(player, selectedAction.get());
                    System.out.println(player);
                } else {
                    System.out.println("Unknown action command: " + userInput);
                }
            }
        }

        // Dealer should draw cards
        if (isAnyPlayerIn(players, Set.of(PlayerStatus.BLACKJACK, PlayerStatus.STANDING))) {
            while (dealer.getStatus() == PlayerStatus.PLAYING) {
                dealer.draw(deck);
            }
            System.out.println(dealer);
        } else {
            System.out.println(dealer.getName() + " skips drawing cards");
        }
    }

    private void evaluateRound() {
        for (HumanPlayer player : players) {
            RoundResults results = switch (player.getStatus()) {
                case BUSTED -> new RoundResults(player.getName() + " busted and lost", 0, dealer.getStatus() == PlayerStatus.BLACKJACK);
                case SURRENDERED -> new RoundResults(player.getName() + " surrendered", 0.5);
                case BLACKJACK -> handlePlayerBlackJack(player, dealer);
                case STANDING -> handlePlayerStanding(player, dealer);
                case PLAYING -> throw new IllegalStateException(player.getName() + " should not be in " + player.getStatus() + " status");
                case SKIPPED -> new RoundResults(player.getName() + " skipped this round", 0);
            };
            player.collectReward(results);
            System.out.println(results.message());
            System.out.println(player.getName() + "'s budget: " + player.getBudget());
        }
    }

    private void displayPostGameStatus() {
        players.sort(new PlayerBudgetComparator().reversed());
        for (HumanPlayer player : players) {
            System.out.println(player.getName() + " finished with " + player.getBudget() + " tokens");
        }
    }

    private List<Action> getPlayerActions(HumanPlayer player) {
        if (player.getStatus() != PlayerStatus.PLAYING) {
            throw new IllegalStateException("There are no actions in " + player.getStatus() + " status!");
        }
        List<Action> actions = new ArrayList<>(DEFAULT_ACTIONS);
        Hand hand = player.getHand();
        if (hand.getNumberOfCards() == 2) {
            actions.add(Action.SURRENDER);
            if (player.getBudget() >= hand.getBet()) {
                actions.add(Action.DOUBLE);
            }
            if (dealer.isFirstCardAce() && player.getBudget() > 0) {
                actions.add(Action.INSURANCE);
            }
        }
        return actions;
    }

    private void apply(HumanPlayer player, Action action) {
        PlayerStatus status = player.getStatus();
        if (status != PlayerStatus.PLAYING) {
            throw new IllegalStateException("No actions should be applied in " + status + " status!");
        }
        switch (action) {
            case HIT -> player.draw(deck);
            case STAND -> player.setStatus(PlayerStatus.STANDING);
            case SURRENDER -> player.setStatus(PlayerStatus.SURRENDERED);
            case DOUBLE -> player.executeDoubleAction(deck);
            case INSURANCE -> executeInsuranceAction(player);
        }
    }

    private void executeInsuranceAction(HumanPlayer player) {
        IntConsumer consumer = new IntConsumer() {
            @Override
            public void accept(int value) {
                player.setInsurance(value);
            }
        };
        int maxPossibleInsurance = Math.min((int)(player.getHand().getBet() * 0.5), player.getBudget());
        getPlayerBetOrInsurance(String.format("%s's insurance (1 - %d): ", player.getName(), maxPossibleInsurance), consumer);
    }

    private void getPlayerBetOrInsurance(String label, IntConsumer consumer) {
        while (true) {
            try {
                System.out.print(label);
                String userInput = scanner.nextLine();
                consumer.accept(Integer.parseInt(userInput));
                break;
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

    private static boolean isAnyPlayerIn(List<HumanPlayer> players, Set<PlayerStatus> desiredStatuses) {
        for (HumanPlayer player : players) {
            if (desiredStatuses.contains(player.getStatus())) {
                return true;
            }
        }
        return false;
    }

    private static RoundResults handlePlayerStanding(HumanPlayer player, Dealer dealer) {
        String playerName = player.getName();
        String dealerName = dealer.getName();
        if (dealer.getStatus() == PlayerStatus.BUSTED) {
            return new RoundResults(playerName + " won, because " + dealerName + " busted", 2);
        }
        boolean dealerHadBlackjack = dealer.getStatus() == PlayerStatus.BLACKJACK;
        if (dealer.getHandValue() > player.getHandValue()) {
            return new RoundResults(playerName + " lost to " + dealerName + " by having less points", 0, dealerHadBlackjack);
        } else if (dealer.getHandValue() == player.getHandValue()) {
            // TODO check what if dealer has blackjack and player has hand value 21
            return new RoundResults(playerName + " is in tie with " + dealerName, 1, dealerHadBlackjack);
        } else {
            return new RoundResults(playerName + " won", 2, dealerHadBlackjack);
        }
    }

    private static RoundResults handlePlayerBlackJack(HumanPlayer player, Dealer dealer) {
        String playerName = player.getName();
        if (dealer.getStatus() == PlayerStatus.BLACKJACK) {
            return new RoundResults(playerName + " lost, because " + dealer.getName() + " has BLACKJACK too", 0, true);
        } else {
            return new RoundResults(playerName + " won with BLACKJACK", 2.5);
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

    private static void drawAllPlayers(Deck deck, List<AbstractPlayer> players) {
        for (AbstractPlayer player : players) {
            if (player.getStatus() == PlayerStatus.PLAYING) {
                player.draw(deck);
            }
        }
    }
}
