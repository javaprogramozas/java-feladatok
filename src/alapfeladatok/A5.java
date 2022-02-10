package alapfeladatok;

import java.util.Locale;
import java.util.Scanner;

/**
 * A5. Kérjünk be két (egész) számot és írjuk ki a két szám átlagát
 *
 * példa
 * Bemenet: 5 2
 * Kimenet: 3.5
 */
public class A5 {

    public static void main(String[] args) {
        Scanner bemenet = new Scanner(System.in);


        /*
        int a = bemenet.nextInt();
        int b = bemenet.nextInt();
        double összeg = a + b;
        double átlag = összeg / 2;
         */

        bemenet.useLocale(Locale.ENGLISH);
        double a = bemenet.nextDouble();
        double b = bemenet.nextDouble();
        double összeg = a + b;
        double átlag = összeg / 2;

        System.out.println(átlag);

        bemenet.close();
    }
}
