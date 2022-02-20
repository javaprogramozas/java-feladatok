package alapfeladatok;

import java.util.Scanner;

/**
 * Kérjünk be egész számokat egészen addig amíg a 0-t nem írnak be, majd írjuk ki a beírt számok átlagát.
 * A 0-t mint végjelet nem kell belevenni a számításba.
 *
 * példa
 * Bemenet: 4 2 1 8 3 0
 * Kimenet: 3.6
 */
public class A7 {

    public static void main(String[] args) {
        Scanner bemenet = new Scanner(System.in);

        int szám, számláló = 0;
        double összeg = 0;

        while ((szám = bemenet.nextInt()) != 0) {
            összeg += szám;
            számláló++;
        }

        if (számláló != 0) {
            double átlag = összeg / számláló;
            System.out.printf("%.3f%n", átlag);
        } else {
            System.out.println("Nem adtál meg egyetlen számot sem");
        }

        bemenet.close();
    }
}
