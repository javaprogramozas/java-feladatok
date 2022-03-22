package alapfeladatok;

import java.time.Duration;
import java.util.Scanner;

/**
 * Kérjünk be egy pozitív számot ami egy időtartamot jelent másodpercben,
 * és írjuk ki hogy az mennyi órát, percet és másodpercet jelent
 *
 * 1.) példa
 * Bemenet: 5000
 * Kimenet: 1ó 23p 20m
 *
 * 2.) példa
 * Bemenet: 1398
 * Kimenet: 23p 18m
 *
 * 3.) példa
 * Bemenet: 120
 * Kimenet: 2p 0m
 */
public class A14 {

    private static final int SECONDS_IN_MINUTE = 60;
    private static final int SECONDS_IN_HOUR = 60 * SECONDS_IN_MINUTE;
    private static final int SECONDS_IN_DAY = 24 * SECONDS_IN_HOUR;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int totalSeconds = scanner.nextInt();
        Duration duration = Duration.ofSeconds(totalSeconds);

        long days = duration.toDaysPart();
        if (days > 0) {
            System.out.print(days + "n ");
        }

        int hours = duration.toHoursPart();

        if (days > 0 || hours > 0) {
            System.out.print(hours + "ó ");
        }

        int minutes = duration.toMinutesPart();

        if (days > 0 || hours > 0 || minutes > 0) {
            System.out.print(minutes + "p ");
        }

        System.out.println(duration.toSecondsPart() + "m");

        scanner.close();
    }

    private static void manuálisMegoldás() {
        Scanner scanner = new Scanner(System.in);

        int totalSeconds = scanner.nextInt();

        int days = totalSeconds / SECONDS_IN_DAY;
        totalSeconds %= SECONDS_IN_DAY;

        if (days > 0) {
            System.out.print(days + "n ");
        }

        int hours = totalSeconds / SECONDS_IN_HOUR;
        totalSeconds %= SECONDS_IN_HOUR;

        if (days > 0 || hours > 0) {
            System.out.print(hours + "ó ");
        }

        int minutes = totalSeconds / SECONDS_IN_MINUTE;
        totalSeconds %= SECONDS_IN_MINUTE;

        if (days > 0 || hours > 0 || minutes > 0) {
            System.out.print(minutes + "p ");
        }

        System.out.println(totalSeconds + "m");

        scanner.close();
    }
}
