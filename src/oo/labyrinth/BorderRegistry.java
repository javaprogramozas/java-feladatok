package oo.labyrinth;

import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BorderRegistry {

    private final Map<Integer, Border> borders = new ConcurrentHashMap<>();

    public Border getBorder(Set<Direction> directions) {
        int key = generateKey(directions);
        return borders.computeIfAbsent(key, k -> generateBorderForDirections(directions));
    }

    private int generateKey(Set<Direction> directions) {
        return directions.stream().mapToInt(direction -> direction.key).sum();
    }

    private Border generateBorderForDirections(Set<Direction> directions) {
        BorderStrokeBuilder builder = BorderStrokeBuilder.builder();

        for (Direction direction : directions) {
            switch (direction) {
                case NORTH -> builder.topStroke(Color.BLACK);
                case EAST -> builder.rightStroke(Color.BLACK);
                case SOUTH -> builder.bottomStroke(Color.BLACK);
                case WEST -> builder.leftStroke(Color.BLACK);
            }
        }

        return new Border(builder.build());
    }

}
