package oo.conway;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameOfLifeModel {

    private final int numberOfCells;
    private final int rows;
    private final int columns;

    public GameOfLifeModel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.numberOfCells = rows * columns;
    }

    public List<Integer> provideRandomCellPositions() {
        List<Integer> indices = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfCells; i++) {
            if (random.nextInt(100) < 20) {
                indices.add(i);
            }
        }
        return indices;
    }

    public List<Integer> calculateNextState(List<Boolean> cellStateList) {
        boolean[][] cellStates = new boolean[rows][columns];
        int index = 0;
        for (int row = 0; row < cellStates.length; row++) {
            for (int column = 0; column < cellStates[row].length; column++) {
                cellStates[row][column] = cellStateList.get(index++);
            }
        }
        return GameEngine.tick(cellStates).stream()
                .map(position -> position.row() * columns + position.column())
                .toList();
    }
}
