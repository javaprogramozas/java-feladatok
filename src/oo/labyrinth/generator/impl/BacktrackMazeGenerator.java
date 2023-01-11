package oo.labyrinth.generator.impl;

import oo.labyrinth.generator.MazeGenerator;
import oo.labyrinth.generator.model.Cell;
import oo.labyrinth.generator.model.Direction;
import oo.labyrinth.generator.model.Maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BacktrackMazeGenerator implements MazeGenerator {

    @Override
    public Maze generate(int rows, int columns) {
        Maze maze = new Maze(rows, columns);
        Set<Cell> visitedCells = new HashSet<>();
        LinkedList<Cell> trackedCells = new LinkedList<>();

        Cell startingCell = maze.getCell(0, 0);

        visitedCells.add(startingCell);
        trackedCells.add(startingCell);

        while (!trackedCells.isEmpty()) {
            Cell current = trackedCells.getLast();
            Optional<Direction> optionalDirection = selectRandomNeighbourDirection(maze, visitedCells, current);

            if (optionalDirection.isPresent()) {
                Direction direction = optionalDirection.get();
                Cell next = maze.getCell(current, direction);
                trackedCells.addLast(next);
                visitedCells.add(next);
                current.removeWall(direction);
                next.removeWall(direction.opposite());
            } else {
                trackedCells.removeLast();
            }
        }
        return maze;
    }

    private Optional<Direction> selectRandomNeighbourDirection(Maze maze, Set<Cell> visitedCells, Cell current) {
        List<Direction> neighboursDirections = new ArrayList<>(maze.getNeighboursDirections(current));
        Collections.shuffle(neighboursDirections);
        return neighboursDirections.stream()
                .filter(direction -> !visitedCells.contains(maze.getCell(current, direction)))
                .findAny();
    }

}
