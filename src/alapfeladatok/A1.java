package alapfeladatok;

import java.util.Scanner;

/**
 * A1. Kérjünk be két egész számot és írjuk ki a nagyobbikat
 *
 * példa
 * Bemenet: 13 2
 * Kimenet: 13
 */
public class A1 {

    public static void main(String[] args) {
        Scanner bemenet = new Scanner(System.in);

        int a = bemenet.nextInt();
        int b = bemenet.nextInt();

        System.out.println(Math.max(a, b));

        bemenet.close();
    }
}
