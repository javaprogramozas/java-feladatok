package oo.labyrinth.generator;

import oo.labyrinth.generator.model.Cell;
import oo.labyrinth.generator.model.Direction;

public abstract class AbstractMazeGenerator {
    protected Cell[][] grid;

    protected AbstractMazeGenerator(int rows, int columns) {
        this.grid = initGrid(rows, columns);
    }

    public abstract void generate();

    public Cell getCell(int row, int column) {
        return grid[row][column];
    }

    public Cell getCell(Cell cell, Direction direction) {
        return getCell(cell.getRow() + direction.rowDelta, cell.getColumn() + direction.columnDelta);
    }

    private static Cell[][] initGrid(int rows, int columns) {
        Cell[][] grid = new Cell[rows][columns];
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                grid[row][column] = new Cell(row, column);
            }
        }
        return grid;
    }
}
