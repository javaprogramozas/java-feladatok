package alapfeladatok.matches;

import java.util.Scanner;
import java.util.StringJoiner;

public class MatchesGame {

    public void play() {
        System.out.println("Gyufás játék");
        int numberOfMatches = 13;
        int round = 0;
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Mi a neved? ");
            String playerName = input.nextLine();
            Player[] players = new Player[]{new HumanPlayer(playerName, input), new MachinePlayer()};

            while (numberOfMatches > 0) {
                int maxPick = Math.min(3, numberOfMatches);
                String possiblePicks = calculatePossiblePicks(maxPick);
                Player currentPlayer = players[round % players.length];
                String name = currentPlayer.getName();
                System.out.printf("""
                        %d. kör
                        Az asztalon van %d gyufa
                        """, round + 1, numberOfMatches);

                String echoString = String.format("%s: mennyit szeretnél elvenni? %s ", name, possiblePicks);
                int playerPick = currentPlayer.chooseMatchesToPick(maxPick, echoString);
                numberOfMatches -= playerPick;
                round++;
                System.out.println();
            }
            Player winner = players[round % players.length];
            System.out.println("\n" + winner.getName() + " a győztes!");
        }
    }

    private String calculatePossiblePicks(int maxPick) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (int i = 1; i <= maxPick; i++) {
            joiner.add(Integer.toString(i));
        }
        return joiner.toString();
    }

}
