package tömbök;

import java.util.Arrays;
import java.util.Random;

public class T1 {

    public static void main(String[] args) {
        //sortIntArray();
        sortObjectArray();
    }

    private static void sortIntArray() {
        int[] numbers = generateRandomArray(10);

        System.out.println("Rendezés előtt: " + Arrays.toString(numbers));
        for (int i = numbers.length - 1; i >= 0; i--) {
            System.out.println("Scan #" + (numbers.length - i));
            boolean wasSwap = false;
            for (int j = 0; j < i; j++) {
                if (numbers[j] > numbers[j + 1]) {
                    int temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                    wasSwap = true;
                }
            }
            if (!wasSwap) {
                break;
            }
        }
        System.out.println("Rendezés után: " + Arrays.toString(numbers));
    }

    private static void sortObjectArray() {
        String[] words = new String[]{"monitor", "chair", "apple", "desk", "zebra", "mouse", "dog", "cat"};

        System.out.println("Rendezés előtt: " + Arrays.toString(words));
        for (int i = words.length - 1; i >= 0; i--) {
            boolean wasSwap = false;
            for (int j = 0; j < i; j++) {
                if (words[j].compareTo(words[j + 1]) > 0) {
                    String temp = words[j];
                    words[j] = words[j + 1];
                    words[j + 1] = temp;
                    wasSwap = true;
                }
            }
            if (!wasSwap) {
                break;
            }
        }
        System.out.println("Rendezés után: " + Arrays.toString(words));
    }

    private static int[] generateRandomArray(int length) {
        int[] numbers = new int[length];
        Random random = new Random();

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(length * 2) + 1;
        }

        return numbers;
    }
}
