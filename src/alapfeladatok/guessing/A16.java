package alapfeladatok.guessing;

import java.util.Scanner;

public class A16 {

    public static void main(String[] args) {
        GuessingGame game = new GuessingGame();
        System.out.printf("Gondoltam egy számra 1 és %d között, találd ki!%n", game.getMaximumNumber());

        GuessResult lastResult;

        try (Scanner input = new Scanner(System.in)) {
            do {
                System.out.printf("(%d/%d) Tipped? ", game.getAttemptsMade(), game.getMaximumAttempts());
                int guess = input.nextInt();
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
                System.out.println("Nincs több próbálkozási lehetőséged, vesztettél :(");
            }
        }
    }
}
