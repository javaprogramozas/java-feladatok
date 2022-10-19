package alapfeladatok;

import java.util.Random;

/**
 * Monte-Carlo szimuláció
 */
public class A22 {

    public static void main(String[] args) {
        //chanceOfSixes();
        for (int i = 0; i < 10; i++) {
            calculatePi();
        }
    }

    private static void chanceOfSixes() {
        Random random = new Random();

        int numOfAttempts = 10000000;
        int numOfThrows = 3;
        int numOfDesiredSixes = 2;
        int numOfSuccessfulAttempts = 0;

        for (int i = 0; i < numOfAttempts; i++) {
            int counter = 0;
            for (int j = 0; j < numOfThrows; j++) {
                int result = random.nextInt(6) + 1;
                if (result == 6) {
                    counter++;
                }
            }

            if (counter >= numOfDesiredSixes) {
                numOfSuccessfulAttempts++;
            }
        }

        double probability = (double)numOfSuccessfulAttempts / numOfAttempts * 100;
        System.out.printf("%.4f%%%n", probability);
    }

    private static void calculatePi() {
        Random random = new Random();
        int numOfAttempts = 100000000;
        int numOfDesiredPoints = 0;

        for (int i = 0; i < numOfAttempts; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();

            if (x * x + y * y <= 1) {
                numOfDesiredPoints++;
            }
        }
        double pi = 4.0 * numOfDesiredPoints / numOfAttempts;
        System.out.printf("PI=%.6f%n", pi);
    }

}
