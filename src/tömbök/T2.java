package tömbök;

import java.util.Arrays;
import java.util.Random;

public class T2 {

    public static void main(String[] args) {
        int[] numbers = generateRandomArray(10);

        System.out.println("Rendezés előtt: " + Arrays.toString(numbers));
        quicksort(numbers, 0, numbers.length - 1);
        System.out.println("Rendezés után: " + Arrays.toString(numbers));
    }

    private static void quicksort(int[] numbers, int left, int right) {
        if (right > left) {
            int pivot = new Random().nextInt(right - left) + left;
            int newPivot = partition(numbers, left, right, pivot);
            quicksort(numbers, left, newPivot - 1);
            quicksort(numbers, newPivot + 1, right);
        }
    }

    private static int partition(int[] numbers, int left, int right, int pivotIndex) {
        int pivotValue = numbers[pivotIndex];
        swap(numbers, pivotIndex, right);
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (numbers[i] < pivotValue) {
                swap(numbers, i, storeIndex);
                storeIndex++;
            }
        }
        swap(numbers, storeIndex, right);
        return storeIndex;
    }

    private static void swap(int[] numbers, int i, int j) {
        int temp = numbers[i];
        numbers[i] = numbers[j];
        numbers[j] = temp;
    }

    private static int[] generateRandomArray(int length) {
        int[] numbers = new int[length];
        Random random = new Random();

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i + 1;
        }

        for (int i = 0; i < numbers.length; i++) {
            int newIndex = random.nextInt(numbers.length);
            if (newIndex != i) {
                swap(numbers, i, newIndex);
            }
        }

        return numbers;
    }
}
