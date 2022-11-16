package oo.labyrinth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Maze {

    private Cell[][] grid;

    public Maze(int rows, int columns) {
        this.grid = initGrid(rows, columns);
    }

    public void generate() {
        Set<Cell> visitedCells = new HashSet<>();
        LinkedList<Cell> trackedCells = new LinkedList<>();

        Cell startingCell = getCell(0, 0);

        visitedCells.add(startingCell);
        trackedCells.add(startingCell);

        while (!trackedCells.isEmpty()) {
            Cell current = trackedCells.getLast();
            Optional<Direction> optionalDirection = selectRandomNeighbourDirection(visitedCells, current);

            if (optionalDirection.isPresent()) {
                Direction direction = optionalDirection.get();
                Cell next = getCell(current, direction);
                trackedCells.addLast(next);
                visitedCells.add(next);
                current.removeWall(direction);
                next.removeWall(direction.opposite());
            } else {
                trackedCells.removeLast();
            }
        }
    }

    public Set<Direction> getNeighboursDirections(Cell cell) {
        Set<Direction> neighbourDirections = new HashSet<>();
        for (Direction direction : Direction.values()) {
            int row = cell.getRow() + direction.rowDelta;
            int column = cell.getColumn() + direction.columnDelta;
            if (row >= 0 && row < grid.length && column >= 0 && column < grid[row].length) {
                neighbourDirections.add(direction);
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

    private static Cell[][] initGrid(int rows, int columns) {
        Cell[][] grid = new Cell[rows][columns];
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                grid[row][column] = new Cell(row, column);
            }
        }
        return grid;
    }

    private Optional<Direction> selectRandomNeighbourDirection(Set<Cell> visitedCells, Cell current) {
        List<Direction> neighboursDirections = new ArrayList<>(getNeighboursDirections(current));
        Collections.shuffle(neighboursDirections);
        return neighboursDirections.stream()
                .filter(direction -> !visitedCells.contains(getCell(current, direction)))
                .findAny();
    }

}
