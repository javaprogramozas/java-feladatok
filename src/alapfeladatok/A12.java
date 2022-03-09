package alapfeladatok;

import java.util.Scanner;

/**
 * Kérjünk be egy pozitív decimális számot és írjuk ki a bináris megfelelőjét.
 *
 * Egy decimális, azaz tízes számrendszerbeli szám számjegyei azt jelölik, hogy az egyes helyiértékeken szereplő számszor
 * kell venni a helyiértékhez tartozó kitevőt, azaz például a 231 = 2 * 100 + 3 * 10 + 1, ahol a 100 a 10-es alap második hatványa,
 * a 10 az első és az 1 a nulladik hatvány.
 * Ennek megfelelően egy bináris szám esetén az egyes helyiértékekhez a 2 megfelelő hatványai tartoznak,
 * azaz például a 1101 binárisan az 1 * 8 + 1 * 4 + 0 * 2 + 1 * 1 = 13 (vegyük észre hogy 8 = 2^3, 4 = 2^2, 2 = 2^1, 1 = 2^0).
 *
 * Példa
 * Bemenet: 231
 * Kimenet: 11100111 -> 128 + 64 + 32 + 4 + 2 + 1
 * 231 - 128 = 103
 *
 */
public class A12 {

    public static void main(String[] args) {
        osztósMegfordítósMegoldás();
        //kivonósMegoldás();
        //profiMegoldás();
    }

    public static void osztósMegfordítósMegoldás() {
        Scanner bemenet = new Scanner(System.in);

        int szám = bemenet.nextInt();
        String binárisSzám = "";

        while (szám > 0) {
            binárisSzám += szám % 2;
            szám /= 2;
        }

        String fordított = "";
        for (int i = binárisSzám.length() - 1; i >= 0; i--) {
            fordított = fordított + binárisSzám.charAt(i);
        }
        /*
        Egy picit kultúráltabb megoldás:
        String fordított = new StringBuilder(binárisSzám).reverse().toString();
        sőt egy lépéssel továbbmenve maga a binárisSzám lehetne a StringBuilder és abba gyűjteni a számjegyeket
         */
        System.out.println(fordított);

        bemenet.close();
    }

    public static void kivonósMegoldás() {
        Scanner bemenet = new Scanner(System.in);

        int szám = bemenet.nextInt();

        int kettőHatvány = 1;
        while (szám >= kettőHatvány * 2) {
            kettőHatvány *= 2;
        }

        String binárisSzám = "";
        while (kettőHatvány > 0) {
            if (szám >= kettőHatvány) {
                szám -= kettőHatvány;
                binárisSzám += "1";
            } else {
                binárisSzám += "0";
            }
            kettőHatvány /= 2;
        }

        System.out.println(binárisSzám);
        bemenet.close();
    }

    private static void profiMegoldás() {
        Scanner bemenet = new Scanner(System.in);

        int szám = bemenet.nextInt();
        System.out.println(Integer.toString(szám, 2));
    }
}
