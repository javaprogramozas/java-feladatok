package oo.labyrinth.generator.impl;

import oo.labyrinth.generator.AbstractMazeGenerator;
import oo.labyrinth.generator.model.Cell;
import oo.labyrinth.generator.model.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BacktrackMazeGenerator extends AbstractMazeGenerator {

    public BacktrackMazeGenerator(int rows, int columns) {
        super(rows, columns);
    }

    @Override
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

    private Optional<Direction> selectRandomNeighbourDirection(Set<Cell> visitedCells, Cell current) {
        List<Direction> neighboursDirections = new ArrayList<>(getNeighboursDirections(current));
        Collections.shuffle(neighboursDirections);
        return neighboursDirections.stream()
                .filter(direction -> !visitedCells.contains(getCell(current, direction)))
                .findAny();
    }

}
