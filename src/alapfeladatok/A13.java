package alapfeladatok;

import java.util.Scanner;

/**
 * Kérjünk be egy bináris számot és írjuk ki a decimális megfelelőjét
 *
 * Példa
 * Bemenet: 11100111
 * Kimenet: 231
 */
public class A13 {

    public static void main(String[] args) {
        Scanner bemenet = new Scanner(System.in);

        String binárisSzám = bemenet.nextLine();
        System.out.println(Integer.parseInt(binárisSzám, 2));

        bemenet.close();
    }

    private static void manuálisMódszer() {
        Scanner bemenet = new Scanner(System.in);

        String binárisSzám = bemenet.nextLine();

        int decimálisSzám = 0, kettőhatvány = 1;
        for (int i = binárisSzám.length() - 1; i >= 0; i--) {
            char karakter = binárisSzám.charAt(i);
            int helyiérték = Character.getNumericValue(karakter); // '0' -> 0, '1' -> 1
            decimálisSzám += helyiérték * kettőhatvány;
            kettőhatvány *= 2;
        }

        System.out.println(decimálisSzám);

        bemenet.close();
    }

}
