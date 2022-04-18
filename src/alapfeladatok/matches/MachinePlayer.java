package alapfeladatok.matches;

import java.util.Random;

public class MachinePlayer implements Player {

    private final Random random = new Random();

    @Override
    public String getName() {
        return "AI";
    }

    @Override
    public int chooseMatchesToPick(GameContext context) {
        int pick;
        if ((context.getNumberOfMatches() + context.getPreviousPick() - 1) % 4 == 0) {
            //Winning strategy
            System.out.println("Nyerő");
            pick = 4 - context.getPreviousPick();
        } else {
            //Aim for winning strategy
            pick = (context.getNumberOfMatches() - 1) % 4;
            if (pick == 0) {
                //No strategy
                pick = random.nextInt(context.getMaxPick()) + 1;
                System.out.println("Random");
            } else {
                System.out.println("Terelő");
            }
        }

        System.out.println(context.getEcho(getName()) + pick);
        return pick;
    }
}
