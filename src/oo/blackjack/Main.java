package oo.blackjack;

import oo.blackjack.model.AbstractPlayer;
import oo.blackjack.model.Action;
import oo.blackjack.model.Card;
import oo.blackjack.model.Dealer;
import oo.blackjack.model.HumanPlayer;
import oo.blackjack.model.PlayerStatus;
import oo.blackjack.model.Rank;
import oo.blackjack.model.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.StringJoiner;

public class Main {

    public static void main(String[] args) {
        List<Card> deck = createDeck();
        Collections.shuffle(deck);

        Dealer dealer = new Dealer();
        List<HumanPlayer> players = List.of(new HumanPlayer("Player #1"), new HumanPlayer("Player #2"));

        // Betting

        // Setup of first round of cards
        List<AbstractPlayer> firstRoundOfDraws = new ArrayList<>(players);
        firstRoundOfDraws.add(dealer);
        firstRoundOfDraws.addAll(players);
        drawAllPlayers(deck, firstRoundOfDraws);

        // Next round, each player draws
        try (Scanner scanner = new Scanner(System.in)) {
            for (HumanPlayer player : players) {
                System.out.println(player);
                while (player.getStatus() == PlayerStatus.PLAYING) {
                    List<Action> actions = player.getAvailableActions();
                    System.out.print("Actions: " + getActionLabels(actions) + "? ");
                    String userInput = scanner.nextLine();
                    Optional<Action> selectedAction = findActionByCommand(actions, userInput);
                    if (selectedAction.isPresent()) {
                        player.apply(selectedAction.get(), deck);
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


            // Evaluate player hands
            for (HumanPlayer player : players) {
                String message = switch (player.getStatus()) {
                    case BUSTED -> player.getName() + " busted and lost";
                    case SURRENDERED -> player.getName() + " surrendered";
                    case BLACKJACK -> handlePlayerBlackJack(player, dealer);
                    case STANDING -> handlePlayerStanding(player, dealer);
                    case PLAYING -> throw new IllegalStateException(player.getName() + " should not be in " + player.getStatus() + " status");
                };
                System.out.println(message);
            }
        }
    }

    private static boolean isAnyPlayerIn(List<HumanPlayer> players, Set<PlayerStatus> desiredStatuses) {
        for (HumanPlayer player : players) {
            if (desiredStatuses.contains(player.getStatus())) {
                return true;
            }
        }
        return false;
    }

    private static String handlePlayerStanding(HumanPlayer player, Dealer dealer) {
        String playerName = player.getName();
        String dealerName = dealer.getName();
        if (dealer.getStatus() == PlayerStatus.BUSTED) {
            return playerName + " won, because " + dealerName + " busted";
        }
        if (dealer.getHandValue() > player.getHandValue()) {
            return playerName + " lost to " + dealerName + " by having less points";
        } else if (dealer.getHandValue() == player.getHandValue()) {
            return playerName + " is in tie with " + dealerName;
        } else {
            return playerName + " won";
        }
    }

    private static String handlePlayerBlackJack(HumanPlayer player, Dealer dealer) {
        String playerName = player.getName();
        if (dealer.getStatus() == PlayerStatus.BLACKJACK) {
            return playerName + " lost, because " + dealer.getName() + " has BLACKJACK too";
        } else {
            return playerName + " won with BLACKJACK";
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

    private static void drawAllPlayers(List<Card> deck, List<AbstractPlayer> players) {
        for (AbstractPlayer player : players) {
            player.draw(deck);
        }
    }

    private static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        return deck;
    }
}
