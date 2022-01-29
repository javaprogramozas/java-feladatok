package alapfeladatok;

import java.util.Scanner;

public class A3 {

    public static void main(String[] args) {
        Scanner bemenet = new Scanner(System.in);

        int a = bemenet.nextInt();
        int b = bemenet.nextInt();
        int c = bemenet.nextInt();
        int összeg = a + b;

        if (összeg > c) {
            System.out.println(a + " + " + b + " nagyobb mint " + c);
        } else {
            System.out.println(a + " + " + b + " nem nagyobb mint " + c);
        }

        /*
        // Alternatív megoldás:
        String tagadás = (összeg <= c) ? " nem" : "";
        System.out.printf("%d + %d%s nagyobb mint %d%n", a, b, tagadás, c);
         */

        bemenet.close();
    }
}
