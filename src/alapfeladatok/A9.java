package alapfeladatok;

import java.util.Scanner;

/**
 * Kérjünk be egy tetszőleges pozitív egész számot, és írjunk ki minden számot 1-től a megadott számig
 * a következő feltételekkel:
 * - Ha a soron következő szám 3 többszöröse, akkor írjuk ki azt hogy “Fizz”
 * - Ha a soron következő szám 5 többszöröse, akkor írjuk ki azt hogy “Buzz”
 * - Ha a soron következő szám 3 és 5 többszöröse is, akkor írjuk ki hogy “FizzBuzz”
 * - Minden más esetben írjuk ki magát a számot
 *
 * 1.) példa
 * Bemenet: 6
 * Kimenet: 1 2 Fizz 4 Buzz Fizz
 *
 * 2.) példa
 * Bemenet: 16
 * Kimenet: 1 2 Fizz 4 Buzz Fizz 7 8 Fizz Buzz 11 Fizz 13 14 FizzBuzz 16
 */
public class A9 {

    public static void main(String[] args) {
        Scanner bemenet = new Scanner(System.in);
        int utolsó = bemenet.nextInt();

        for (int i = 1; i <= utolsó; i++) {
            String kimenet = "";

            if (i % 3 == 0) {
                kimenet += "Fizz";
            }
            if (i % 5 == 0) {
                kimenet += "Buzz";
            }
            if (kimenet.isEmpty()) {
                kimenet = Integer.toString(i);
            }
            System.out.print(kimenet + " ");
        }

        bemenet.close();
    }
}
