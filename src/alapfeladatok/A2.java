package alapfeladatok;

import java.util.Scanner;

/**
 * A2. Kérjünk be egész számokat 0 végjelig és írjuk ki a legnagyobbat a beírt számok közül.
 * A 0 mint végjel, nem része a sorozatnak!
 *
 * példa
 * Bemenet: 5 1 8 4 11 1 2 0
 * Kimenet: 11
 *
 * Kiegészítés:
 * ne csak a legnagyobbat, hanem a legkisebbet is írjuk ki (a 0-s végjel nem tartozik a bemeneti számok közé
 * a fentiek mellett írjuk ki a számok átlagát is
 */
public class A2 {

    public static void main(String[] args) {
        Scanner bemenet = new Scanner(System.in);

        int beolvasott, legnagyobb = Integer.MIN_VALUE, legkisebb = Integer.MAX_VALUE;
        int összeg = 0, darabszám = 0;
        do {
            beolvasott = bemenet.nextInt();
            if (beolvasott != 0) {
                if (beolvasott > legnagyobb) {
                    legnagyobb = beolvasott;
                }
                if (beolvasott < legkisebb) {
                    legkisebb = beolvasott;
                }
                összeg += beolvasott;
                darabszám++;
            }
        } while (beolvasott != 0);

        double átlag = (double)összeg / darabszám;
        System.out.println("Legnagyobb=" + legnagyobb + " legkisebb=" + legkisebb + " átlag=" + átlag);
        System.out.printf("Legnagyobb=%d legkisebb=%d átlag=%.3f%n", legnagyobb, legkisebb, átlag);

        bemenet.close();
    }

}
