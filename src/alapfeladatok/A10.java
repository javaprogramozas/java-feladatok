package alapfeladatok;

import java.util.Scanner;

/**
 * Kérjünk be egy tetszőleges pozitív egész számot és írjuk ki a szám számjegyeinek összegét.
 *
 * 1.) példa
 * Bemenet: 123
 * Kimenet: 6
 *
 * 2.) példa
 * Bemenet: 56287
 * Kimenet: 28
 */
public class A10 {

    public static void main(String[] args) {

        Scanner bemenet = new Scanner(System.in);

        int összeg = 0;
        int szám = bemenet.nextInt();

        while (szám > 0) {
            összeg += szám % 10;
            szám /= 10;
        }

        System.out.println(összeg);
        bemenet.close();

    }
}
