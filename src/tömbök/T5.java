package tömbök;

import java.util.Arrays;

public class T5 {

    public static void main(String[] args) {
        String[] szavak = {"alma", "körte", "kutya", "macska", "zebra", "csirke", "tulipán", "tök"};
        Arrays.sort(szavak);
        System.out.println(Arrays.toString(szavak));
        int index = keresésRekurzív(szavak, "tulipán", 0, szavak.length - 1);
        if (index >= 0) {
            System.out.printf("szavak[%d] = %s%n", index, szavak[index]);
        } else {
            System.out.println("A megadott szó nincs a tömbben");
        }

        System.out.println(Arrays.binarySearch(szavak, "a"));
        Arrays.asList(szavak).indexOf("zebra");
    }

    private static int keresés(String[] szavak, String keresett) {
        int kezdet = 0, vég = szavak.length - 1;
        while (kezdet <= vég) {
            int index = (kezdet + vég) / 2;
            System.out.printf("kezdet=%d, vég=%d, index=%d, szó=%s%n", kezdet, vég, index, szavak[index]);
            int relació = szavak[index].compareTo(keresett);
            if (relació == 0) {
                return index;
            } else if (relació < 0) {
                kezdet = index + 1;
            } else {
                vég = index -1;
            }
        }
        return -1;
    }

    private static int keresésRekurzív(String[] szavak, String keresett, int kezdet, int vég) {
        if (kezdet > vég) {
            return -1;
        }
        int index = (kezdet + vég) / 2;
        System.out.printf("kezdet=%d, vég=%d, index=%d, szó=%s%n", kezdet, vég, index, szavak[index]);
        int relació = szavak[index].compareTo(keresett);
        if (relació == 0) {
            return index;
        } else if (relació < 0) {
            return keresésRekurzív(szavak, keresett, index + 1, vég);
        } else {
            return keresésRekurzív(szavak, keresett, kezdet, index - 1);
        }
    }
}
