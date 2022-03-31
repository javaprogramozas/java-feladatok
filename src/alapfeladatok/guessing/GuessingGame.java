package alapfeladatok.guessing;

import java.util.Random;

public class GuessingGame {

    private final int maximumNumber;
    private final int maximumAttempts;
    private final int numberToGuess;

    private int attemptsMade = 1;

    public GuessingGame() {
        maximumNumber = 100;
        maximumAttempts = 10;
        numberToGuess = new Random().nextInt(maximumNumber) + 1;
    }

    public GuessResult evaluateGuess(int guess) {
        if (!hasMoreAttempts()) {
            return GuessResult.OUT_OF_ATTEMPTS;
        }
        if (guess < 1 || guess > maximumNumber) {
            return GuessResult.GUESS_OUT_OF_RANGE;
        }
        attemptsMade++;
        if (guess > numberToGuess) {
            return GuessResult.GUESS_IS_GREATER;
        }
        if (guess < numberToGuess) {
            return GuessResult.GUESS_IS_LESS;
        }
        return GuessResult.GUESS_IS_EQUAL;
    }

    public int getAttemptsMade() {
        return attemptsMade;
    }

    public int getMaximumNumber() {
        return maximumNumber;
    }

    public int getMaximumAttempts() {
        return maximumAttempts;
    }

    public boolean hasMoreAttempts() {
        return attemptsMade <= maximumAttempts;
    }
}
