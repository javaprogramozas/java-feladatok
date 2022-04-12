package alapfeladatok.matches;

import java.util.Random;

public class MachinePlayer implements Player {

    private final Random random = new Random();

    @Override
    public String getName() {
        return "AI";
    }

    @Override
    public int chooseMatchesToPick(int maxPick, String echoString) {
        int pick = random.nextInt(maxPick) + 1;
        System.out.println(echoString + pick);
        return pick;
    }
}
