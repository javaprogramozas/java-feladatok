package alapfeladatok;

import java.util.Scanner;

/**
 * A6. Kérjünk be két egész számot és számoljuk ki az első szám második számú hatványát,
 * azaz ha a két bekért számot a-val és b-vel jelöljük akkor a^b értékét, és írjuk ki azt.
 *
 * 1. példa
 * Bemenet: 5 2
 * Kimenet: 25
 *
 * 2. példa
 * Bemenet: 3 8
 * Kimenet: 6561
 */
public class A6 {
    public static void main(String[] args) {
        Scanner bemenet = new Scanner(System.in);

        int alap = bemenet.nextInt();
        int kitevő = bemenet.nextInt();
        int abszolútKitevő = kitevő >= 0 ? kitevő : -kitevő;
        double hatvány = 1;

        for (int i = 1; i <= abszolútKitevő; i++) {
            if (kitevő >= 0) {
                hatvány = hatvány * alap; // hatvány *= alap;
            } else {
                hatvány = hatvány / alap; // hatvány /= alap;
            }
        }

        double hatvány2 = Math.pow(alap, kitevő);

        System.out.println(hatvány);
        System.out.println(hatvány2);

        bemenet.close();
    }
}
