package alapfeladatok;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class A23 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("input1.txt"));

        List<Integer> topCalorieValues = new ArrayList<>(List.of(0, 0, 0, 0));
        int actualCalories = 0;
        for (String line : lines) {
            if (!line.isBlank()) {
                actualCalories += Integer.parseInt(line);
            } else {
                topCalorieValues.set(3, actualCalories);
                topCalorieValues.sort(Comparator.reverseOrder());
                actualCalories = 0;
            }
        }
        int sum = topCalorieValues.stream()
                .limit(3)
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println("Maximális 3 kalória érték összege: " + sum);
    }

}
