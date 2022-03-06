package alapfeladatok;

import java.util.Scanner;

/**
 * Lábak és szarvak
 *
 * Egy mesebeli farmon járunk ahol lovak, tehenek és unikornisok élnek együtt. Amikor csodálkozásunk közepette megkérdezzük
 * a farm tulajdonosát, hogy mennyi állata van, ő azzal válaszol, hogy az állatoknak összesen mennyi szarva és mennyi lába van.
 * Írjunk programot, ami bekéri az említett két adatot és kiszámítja az állatok számának lehetséges kombinációit.
 * Feltételezzük, hogy mindegyik állatból van legalább 1 van a farmon. Ellenőrizzük a megadott adatokat és írjuk ki azt
 * is ha nem lehet megoldani a feladatot.
 *
 * Példa
 * Bemenet
 * Lábak száma: 12
 * Szarvak száma: 3
 * Kimenet:
 * Ló: 1 db
 * Tehén: 1 db
 * Unikornis: 1 db
 */
public class A11 {

    public static void main(String[] args) {
        Scanner bemenet = new Scanner(System.in);
        System.out.print("Lábak száma: ");
        int lábakSzáma = bemenet.nextInt();

        System.out.print("Szarvak száma: ");
        int szarvakSzáma = bemenet.nextInt();

        int állatokMaximálisSzáma = lábakSzáma / 4;
        boolean voltMegoldás = false;

        for (int lovakSzáma = 1; lovakSzáma < állatokMaximálisSzáma; lovakSzáma++) {
            for (int tehenekSzáma = 1; tehenekSzáma + lovakSzáma < állatokMaximálisSzáma; tehenekSzáma++) {
                int unikornisokSzáma = állatokMaximálisSzáma - lovakSzáma - tehenekSzáma;
                //System.out.printf("L=%d T=%d U=%d%n", lovakSzáma, tehenekSzáma, unikornisokSzáma);
                if (lábakSzáma == (lovakSzáma + tehenekSzáma + unikornisokSzáma) * 4
                    && szarvakSzáma == tehenekSzáma * 2 + unikornisokSzáma) {
                    System.out.println("Ló: " + lovakSzáma);
                    System.out.println("Tehén: " + tehenekSzáma);
                    System.out.println("Unikornis: " + unikornisokSzáma);
                    voltMegoldás = true;
                }
            }
        }
        if (!voltMegoldás) {
            System.out.println("Nincs lehetséges megoldás");
        }
        bemenet.close();
    }
}
