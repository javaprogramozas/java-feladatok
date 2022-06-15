package oo.blackjack;

import oo.blackjack.model.AbstractPlayer;
import oo.blackjack.model.Card;
import oo.blackjack.model.Dealer;
import oo.blackjack.model.HumanPlayer;
import oo.blackjack.model.PlayerStatus;
import oo.blackjack.model.Rank;
import oo.blackjack.model.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Card> deck = createDeck();
        Collections.shuffle(deck);

        Dealer dealer = new Dealer();
        List<HumanPlayer> players = List.of(new HumanPlayer("Player #1"));

        // Betting

        // Setup of first round of cards
        List<AbstractPlayer> firstRoundOfDraws = new ArrayList<>(players);
        firstRoundOfDraws.add(dealer);
        firstRoundOfDraws.addAll(players);
        drawAllPlayers(deck, firstRoundOfDraws);

        // Next round, each player draws
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
