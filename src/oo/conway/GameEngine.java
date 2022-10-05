package oo.conway;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    public static List<Position> tick(boolean[][] cells) {
        List<Position> positionsToUpdate = new ArrayList<>();
        for (int row = 0; row < cells.length; row++) {
            for (int column = 0; column < cells[row].length; column++) {
                boolean alive = cells[row][column];
                int livingNeighbours = countLivingNeighbours(cells, row, column);
                if (alive) {
                    if (livingNeighbours < 2 || livingNeighbours > 3) {
                        positionsToUpdate.add(new Position(row, column));
                    }
                } else {
                    if (livingNeighbours == 3) {
                        positionsToUpdate.add(new Position(row, column));
                    }
                }
            }
        }
        return positionsToUpdate;
    }

    private static int countLivingNeighbours(boolean[][] cells, int row, int column) {
        int liveCount = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                int rowToCheck = row + rowOffset;
                int columnToCheck = column + columnOffset;
                if (isSelf(rowOffset, columnOffset) || isOutOfBounds(cells.length, cells[0].length, rowToCheck, columnToCheck)) {
                    continue;
                }
                if (cells[rowToCheck][columnToCheck]) {
                    liveCount++;
                }
            }
        }
        return liveCount;
    }

    private static boolean isOutOfBounds(int numberOfRows, int numberOfColumns, int rowToCheck, int columnToCheck) {
        return rowToCheck < 0 || rowToCheck >= numberOfRows || columnToCheck < 0 || columnToCheck >= numberOfColumns;
    }

    private static boolean isSelf(int rowOffset, int columnOffset) {
        return rowOffset == 0 && columnOffset == 0;
    }
}
