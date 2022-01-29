package alapfeladatok;

import java.util.Scanner;

public class A4 {

    public static void main(String[] args) {
        Scanner bemenet = new Scanner(System.in);

        int a = bemenet.nextInt();
        int b = bemenet.nextInt();
        int c = bemenet.nextInt();
        int összeg = a + b;

        if (összeg < c) {
            System.out.println(a + " + " + b + " kisebb mint " + c);
        } else if (összeg > c) {
            System.out.println(a + " + " + b + " nagyobb mint " + c);
        } else {
            System.out.println(a + " + " + b + " egyenlő " + c);
        }

        /*
        // Alternatív megoldás
        String reláció;
        if (összeg < c) {
            reláció = "kisebb mint";
        } else if (összeg > c) {
            reláció = "nagyobb mint";
        } else {
            reláció = "egyenlő";
        }
        System.out.println(a + " + " + b + " " + reláció + " " + c);
        */

        bemenet.close();
    }
}
