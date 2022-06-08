package tömbök;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class T6 {

    public static void main(String[] args) {
//        sieveOfEratosthenes();
        calculatePrimes();
    }

    private static void sieveOfEratosthenes() {
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Meddig írjuk ki a prímszámokat? ");
            int max = input.nextInt();

            int[] sieve = new int[max];
            for (int i = 0; i < sieve.length; i++) {
                sieve[i] = i + 1;
            }

            sieve[0] = 0;
            int index = 1;

            while (index < sieve.length) {
                int prime = sieve[index];
                if (prime != 0) {
                    for (int i = index + prime; i < sieve.length; i += prime) {
                        sieve[i] = 0;
                    }
                }
                index++;
            }

            for (int i = 0; i < sieve.length; i++) {
                if (sieve[i] != 0) {
                    System.out.print(sieve[i] + " ");
                }
            }
        }
    }

    private static void calculatePrimes() {
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Meddig írjuk ki a prímszámokat? ");
            int max = input.nextInt();

            SortedSet<Integer> primes = new TreeSet<>();
            primes.add(2);
            primes.add(3);

            int k = 1;
            while (6 * k + 1 < max) {
                int low = 6 * k - 1;
                int high = 6 * k + 1;
                if (isPrime(low, primes)) {
                    primes.add(low);
                }
                if (isPrime(high, primes)) {
                    primes.add(high);
                }
                k++;
            }

            System.out.println(primes);
            System.out.println(BigInteger.probablePrime(31, new SecureRandom()));
            int a = 1343656151;
        }
    }

    private static boolean isPrime(int number, SortedSet<Integer> primes) {
        double limit = Math.sqrt(number);
        for (Integer prime : primes) {
            if (prime > limit) {
                break;
            }
            if (number % prime == 0) {
                return false;
            }
        }
        return true;
    }

}
