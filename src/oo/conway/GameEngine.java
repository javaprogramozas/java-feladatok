package oo.conway;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class GameEngine implements Runnable {

    private final Cell[][] cells;

    public GameEngine(Cell[][] cells) {
        this.cells = cells;
    }

    @Override
    public void run() {
        while (true) {
            tick();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void tick() {
        List<Cell> cellsToUpdate = calculateCellUpdates();
        Platform.runLater(() -> cellsToUpdate.forEach(Cell::flip));
    }

    private List<Cell> calculateCellUpdates() {
        List<Cell> cellsToUpdate = new ArrayList<>();
        for (int row = 0; row < cells.length; row++) {
            for (int column = 0; column < cells[row].length; column++) {
                Cell cell = cells[row][column];
                int livingNeighbours = countLivingNeighbours(row, column);
                if (cell.isAlive()) {
                    if (livingNeighbours < 2 || livingNeighbours > 3) {
                        cellsToUpdate.add(cell);
                    }
                } else {
                    if (livingNeighbours == 3) {
                        cellsToUpdate.add(cell);
                    }
                }
            }
        }
        return cellsToUpdate;
    }

    private int countLivingNeighbours(int row, int column) {
        int liveCount = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                int rowToCheck = row + rowOffset;
                int columnToCheck = column + columnOffset;
                if (isSelf(rowOffset, columnOffset) || isOutOfBounds(rowToCheck, columnToCheck)) {
                    continue;
                }
                if (cells[rowToCheck][columnToCheck].isAlive()) {
                    liveCount++;
                }
            }
        }
        return liveCount;
    }

    private boolean isOutOfBounds(int rowToCheck, int columnToCheck) {
        return rowToCheck < 0 || rowToCheck >= cells.length || columnToCheck < 0 || columnToCheck >= cells[0].length;
    }

    private static boolean isSelf(int rowOffset, int columnOffset) {
        return rowOffset == 0 && columnOffset == 0;
    }
}
