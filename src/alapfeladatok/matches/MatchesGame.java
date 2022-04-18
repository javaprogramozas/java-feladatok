package alapfeladatok.matches;

import java.util.Scanner;
import java.util.StringJoiner;

public class MatchesGame {

    public void play() {
        System.out.println("Gyufás játék");
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Mi a neved? ");
            String playerName = input.nextLine();

            System.out.print("Mennyi gyufával játsszunk? ");
            //TODO validáció
            GameContext context = new GameContext(Integer.parseInt(input.nextLine()));

            Player[] players = new Player[]{new HumanPlayer(playerName, input), new MachinePlayer()};

            while (context.isNextRoundPossible()) {
                int round = context.getRound();
                Player currentPlayer = players[round % players.length];
                System.out.printf("""
                        %d. kör
                        Az asztalon van %d gyufa
                        """, round + 1, context.getNumberOfMatches());
                int playerPick = currentPlayer.chooseMatchesToPick(context);
                context.pick(playerPick);
                System.out.println();
            }
            Player winner = players[context.getRound() % players.length];
            System.out.println("\n" + winner.getName() + " a győztes!");
        }
    }

}
