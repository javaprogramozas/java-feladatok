package alapfeladatok.guessing;

import java.util.Scanner;

public class A16 {

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            GuessingGame game = createNewGame(input);
            System.out.printf("Gondoltam egy számra 1 és %d között, találd ki!%n", game.getMaximumNumber());

            GuessResult lastResult;
            do {
                int guess = readInt(input, String.format("(%d/%d) Tipped? ", game.getAttemptsMade(), game.getMaximumAttempts()));
                lastResult = game.evaluateGuess(guess);
                switch (lastResult) {
                    case GUESS_IS_LESS -> System.out.println("Nem talált, a tipped kisebb");
                    case GUESS_IS_GREATER -> System.out.println("Nem talált, a tipped nagyobb");
                    case GUESS_OUT_OF_RANGE -> System.out.println("A tipped érvénytelen");
                    case OUT_OF_ATTEMPTS -> System.out.println("Nincs több lehetőséged tippelni"); //Elvileg ez nem lehetséges
                }
            } while (lastResult != GuessResult.GUESS_IS_EQUAL && game.hasMoreAttempts());

            if (lastResult == GuessResult.GUESS_IS_EQUAL) {
                System.out.println("Eltaláltad, nyertél!");
            } else {
                System.out.printf("Nincs több próbálkozási lehetőséged, a gondolt szám %d volt%n", game.getNumberToGuess());
            }
        }
    }

    private static GuessingGame createNewGame(Scanner input) {
        while (true) {
            try {
                int maximumNumber = readInt(input, "Add meg a tippelés felső határát: ");
                int maximumAttempts = readInt(input, "Add meg a tippelési lehetőségek számát: ");
                return new GuessingGame(maximumNumber, maximumAttempts);
            } catch (IllegalArgumentException e) {
                System.out.println("A beállítások nem megfelelőek");
            }
        }
    }

    private static int readInt(Scanner input, String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Nem megfelelő bemenet, próbálja újra...");
            }
        }
    }
}
