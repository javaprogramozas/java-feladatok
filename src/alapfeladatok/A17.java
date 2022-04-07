package alapfeladatok;

import java.util.Scanner;

public class A17 {

    private static final String MENU = """
            1 - Számsorozat ellenőrzés
            2 - Ellenőrző számjegy számítása
            3 - Kilépés
            --------------
            ? \
            """;

    public static void main(String[] args) {

        try (Scanner input = new Scanner(System.in)) {
            int selectedItem;
            do {
                System.out.print(MENU);
                selectedItem = Integer.parseInt(input.nextLine());
                switch (selectedItem) {
                    case 1:
                        doNumberCheck(input);
                        break;
                    case 2:
                        doCheckDigitCalculation(input);
                        break;
                    case 3:
                        System.out.println("Program vége");
                        break;
                    default:
                        System.out.println("Nincs ilyen opció!");
                }
                System.out.println();
            } while (selectedItem != 3);
        }
    }

    private static void doCheckDigitCalculation(Scanner input) {
        System.out.println("Ellenőrző számjegy számítás");
        System.out.print("Add meg a bemeneti számsort: ");
        String numbers = input.nextLine();
        int check = calculateCheckDigit(numbers);
        System.out.println("A számsor ellenőrző összeggel: " + numbers + check);
    }

    private static void doNumberCheck(Scanner input) {
        System.out.println("Szám ellenőrzés");
        System.out.print("Add meg az ellenőrzendő számsort: ");
        String numbers = input.nextLine();
        String payload = numbers.substring(0, numbers.length() - 1);
        int checkDigit = calculateCheckDigit(payload);
        if (checkDigit == Character.getNumericValue(numbers.charAt(numbers.length() - 1))) {
            System.out.println("A megadott számsor helyes");
        } else {
            System.out.println("A megadott számsor hibás");
        }
    }

    private static int calculateCheckDigit(String numbers) {
        int sum = 0;
        boolean hasToDouble = true;
        for (int i = numbers.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(numbers.charAt(i));
            if (hasToDouble) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            hasToDouble = !hasToDouble;
        }
        return (10 - (sum % 10)) % 10;
    }
}
