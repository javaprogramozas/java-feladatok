package alapfeladatok;

import java.util.Scanner;

/**
 * Kérjünk be egy pozitív számot amely egy kör sugarának hosszát reprezentálja és írjuk ki a kör kerületét és területét.
 * Ha a felhasználó nem pozitív számot adott meg, akkor jelenítsünk meg egy hibaüzenetet és adjuk lehetőséget arra,
 * hogy új számot adjon meg.
 *
 * példa
 * Bemenet: 5
 * Kimenet:
 * Kerület:  31.415
 * Terület: 78.5398
 */
public class A8 {

    public static final double PI = 3.1415;

    public static void main(String[] args) {
        Scanner bemenet = new Scanner(System.in);
        double sugár;
        do {
            System.out.print("Add meg a sugár értékét: ");
            sugár = bemenet.nextDouble();
            if (sugár <= 0) {
                System.out.println("Pozitív számot kell megadnod!\n");
            }
        } while (sugár <= 0);

        System.out.printf("A kör kerülete: %.4f%n", 2 * sugár * Math.PI); // 2*r*Pi
        System.out.printf("A kör területe: %.4f%n", sugár * sugár * Math.PI); //r*r*Pi
    }
}
