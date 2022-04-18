package alapfeladatok.matches;

import java.util.Scanner;

public class HumanPlayer implements Player {

    private final String name;
    private final Scanner scanner;

    public HumanPlayer(String name, Scanner scanner) {
        this.name = name;
        this.scanner = scanner;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int chooseMatchesToPick(GameContext context) {
        int userPick = 0;
        int maxPick = context.getMaxPick();
        do {
            try {
                System.out.print(context.getEcho(name));
                userPick = Integer.parseInt(scanner.nextLine());
                if (userPick < 1 || userPick > maxPick) {
                    System.out.printf("1 és %d közötti számot kell beírnod!%n", maxPick);
                }
            } catch (NumberFormatException e) {
                System.out.println("Nem számot írtál be!");
            }
        } while (userPick < 1 || userPick > maxPick);
        return userPick;
    }
}
