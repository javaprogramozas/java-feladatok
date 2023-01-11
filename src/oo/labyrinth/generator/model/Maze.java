package oo.labyrinth.generator.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Maze {

    private final int rows, columns;
    private final Cell[][] grid;

    public Maze(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.grid = initGrid(rows, columns);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Cell getCell(int row, int column) {
        return grid[row][column];
    }

    public Cell getCell(Cell cell, Direction direction) {
        return getCell(cell.getRow() + direction.rowDelta, cell.getColumn() + direction.columnDelta);
    }

    public Stream<Cell> getCellsInRow(int row) {
        return Arrays.stream(grid[row]);
    }

    public Set<Direction> getNeighboursDirections(Cell cell) {
        Set<Direction> neighbourDirections = new HashSet<>();
        for (Direction direction : Direction.values()) {
            int row = cell.getRow() + direction.rowDelta;
            int column = cell.getColumn() + direction.columnDelta;
            if (row >= 0 && row < rows && column >= 0 && column < columns) {
                neighbourDirections.add(direction);
            }
        }
        return neighbourDirections;
    }

    public void connect(Cell cell, Direction direction) {
        Cell neightbour = getCell(cell, direction);
        cell.removeWall(direction);
        neightbour.removeWall(direction.opposite());
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
