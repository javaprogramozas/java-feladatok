package oo.labyrinth.generator.impl;

import oo.labyrinth.generator.MazeGenerator;
import oo.labyrinth.generator.model.Cell;
import oo.labyrinth.generator.model.CellGroups;
import oo.labyrinth.generator.model.Direction;
import oo.labyrinth.generator.model.Maze;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class EllersMazeGenerator implements MazeGenerator {

    private static final int PROBABILITY_OF_HORIZONTAL_EXPANSION = 60;
    private static final int PROBABILITY_OF_VERTICAL_EXPANSION = 40;

    private final Random random = new Random();

    public Maze generate(int rows, int columns) {
        Maze maze = new Maze(rows, columns);
        CellGroups groups = new CellGroups();

        // 1. lépés: minden mező egy saját külön csoportba kerül
        addGrouplessCellsToNewGroup(maze, 0, groups);

        for (int row = 0; row < maze.getRows() - 1; row++) {
            // 2. lépés: véletlenszerűen összekötünk szomszédos mezőket, balról-jobbra haladva
            createHorizontalConnections(maze, groups, row);

            // 3. lépés: minden csoportból képezünk legalább egy véletlen függőleges kapcsolatot
            createVerticalConnections(maze, groups, row);

            // 4. lépés: a következő sor minden mezőjét ami még nem tartozik csoportba, betesszük egy új csoportba
            addGrouplessCellsToNewGroup(maze, row + 1, groups);
        }

        // 5. lépés: összekötünk minden mezőt akik különböző csoportba tartoznak, balról-jobbra haladva
        completeLastRow(maze, groups);

        return maze;
    }

    private void createHorizontalConnections(Maze maze, CellGroups groups, int row) {
        maze.getCellsInRow(row)
                .limit(maze.getColumns() - 1)
                .forEach(current -> connectRandomlyWithEastNeightbour(maze, groups, current));
    }

    private void connectRandomlyWithEastNeightbour(Maze maze, CellGroups groups, Cell current) {
        boolean shouldConnect = random.nextInt(100) < PROBABILITY_OF_HORIZONTAL_EXPANSION;
        if (shouldConnect) {
            connectCellWithEastNeighbourIfNotInSameGroupAlready(maze, current, groups);
        }
    }

    private void createVerticalConnections(Maze maze, CellGroups groups, int row) {
        Map<Integer, Set<Cell>> cellsByGroup = maze.getCellsInRow(row)
                .collect(groupingBy(groups::getCellGroup, toSet()));
        for (Map.Entry<Integer, Set<Cell>> entry : cellsByGroup.entrySet()) {
            int currentGroup = entry.getKey();
            Set<Cell> cellsInGroup = entry.getValue();
            Set<Cell> selectedCells = selectCellsRandomly(cellsInGroup);
            for (Cell current : selectedCells) {
                Cell below = maze.getCell(current, Direction.SOUTH);
                groups.addCellToGroup(below, currentGroup);
                maze.connect(current, Direction.SOUTH);
            }
        }
    }

    private void completeLastRow(Maze maze, CellGroups groups) {
        int lastRowIndex = maze.getRows() - 1;
        maze.getCellsInRow(lastRowIndex)
                .limit(maze.getColumns() - 1)
                .forEach(current -> connectCellWithEastNeighbourIfNotInSameGroupAlready(maze, current, groups));
    }

    private void addGrouplessCellsToNewGroup(Maze maze, int row, CellGroups groups) {
        for (int column = 0; column < maze.getColumns(); column++) {
            Cell current = maze.getCell(row, column);
            if (!groups.hasGroup(current)) {
                groups.addCellToNewGroup(current);
            }
        }
    }

    private void connectCellWithEastNeighbourIfNotInSameGroupAlready(Maze maze, Cell current, CellGroups groups) {
        int currentGroup = groups.getCellGroup(current);

        Cell right = maze.getCell(current, Direction.EAST);
        int rightGroup = groups.getCellGroup(right);

        if (currentGroup != rightGroup) {
            groups.moveCellsToGroup(rightGroup, currentGroup);
            maze.connect(current, Direction.EAST);
        }
    }

    private Set<Cell> selectCellsRandomly(Set<Cell> source) {
        Set<Cell> candidates = source.stream()
                .filter(cell -> random.nextInt(100) < PROBABILITY_OF_VERTICAL_EXPANSION)
                .collect(toSet());
        if (candidates.isEmpty()) {
            candidates.add(source.iterator().next());
        }
        return candidates;
    }
}
