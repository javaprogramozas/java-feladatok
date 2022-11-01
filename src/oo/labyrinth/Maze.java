package oo.labyrinth;

import java.util.HashSet;
import java.util.Set;

public class Maze {

    private Cell[][] grid;

    public Maze(int size) {
        this.grid = initGrid(size);
    }

    public Set<CellWithDirection> getNeighboursDirections(Cell cell) {
        Set<CellWithDirection> neighbourDirections = new HashSet<>();
        for (Direction direction : Direction.values()) {
            int row = cell.getRow() + direction.rowDelta;
            int column = cell.getColumn() + direction.columnDelta;
            if (row >= 0 && row < grid.length && column >= 0 && column < grid[row].length) {
                neighbourDirections.add(new CellWithDirection(grid[row][column], direction));
            }
        }
        return neighbourDirections;
    }

    public Cell getCell(int row, int column) {
        return grid[row][column];
    }

    public Cell getCell(Cell cell, Direction direction) {
        return getCell(cell.getRow() + direction.rowDelta, cell.getColumn() + direction.columnDelta);
    }

    private static Cell[][] initGrid(int size) {
        Cell[][] grid = new Cell[size][size];
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                grid[row][column] = new Cell(row, column);
            }
        }
        return grid;
    }

}
