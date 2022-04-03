package alapfeladatok.guessing;

import java.util.Random;

public class GuessingGame {

    private final int maximumNumber;
    private final int maximumAttempts;
    private final int numberToGuess;

    private int attemptsMade = 1;

    public GuessingGame() {
        this(100, 10);
    }

    public GuessingGame(int maximumNumber, int maximumAttempts) {
        validateSettings(maximumNumber, maximumAttempts);
        this.maximumNumber = maximumNumber;
        this.maximumAttempts = maximumAttempts;
        numberToGuess = new Random().nextInt(this.maximumNumber) + 1;
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

    public int getNumberToGuess() {
        return numberToGuess;
    }

    public boolean hasMoreAttempts() {
        return attemptsMade <= maximumAttempts;
    }

    private void validateSettings(int maximumNumber, int maximumAttempts) {
        if (maximumNumber / 2 < maximumAttempts) {
            throw new IllegalArgumentException(
                    String.format("Több mint 50%% esély van a kitalálásra: max=%d, kísérlet=%d", maximumNumber, maximumAttempts)
            );
        }
    }
}
