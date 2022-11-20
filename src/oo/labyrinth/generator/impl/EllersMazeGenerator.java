package oo.labyrinth.generator.impl;

import oo.labyrinth.generator.AbstractMazeGenerator;
import oo.labyrinth.generator.model.Cell;
import oo.labyrinth.generator.model.Direction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class EllersMazeGenerator extends AbstractMazeGenerator {

    public EllersMazeGenerator(int rows, int columns) {
        super(rows, columns);
    }

    public void generate() {
        Random random = new Random();
        Map<Cell, Integer> cellToGroup = new HashMap<>();
        Map<Integer, Set<Cell>> groupToCells = new HashMap<>();

        int groupCounter = 0;

        // 1. lépés: minden mező egy saját külön csoportba kerül
        for (int column = 0; column < grid[0].length; column++) {
            Cell cell = grid[0][column];
            int group = groupCounter++;
            cellToGroup.put(cell, group);
            HashSet<Cell> cellGroup = new HashSet<>();
            cellGroup.add(cell);
            groupToCells.put(group, cellGroup);
        }

        for (int row = 0; row < grid.length - 1; row++) {
            // 2. lépés: véletlenszerűen összekötünk szomszédos mezőket, balról-jobbra haladva
            for (int column = 0; column < grid[row].length - 1; column++) {
                boolean shouldConnect = random.nextBoolean();
                Cell current = grid[row][column];
                int currentGroup = cellToGroup.get(current);

                Cell right = grid[row][column + 1];
                int rightGroup = cellToGroup.get(right);

                if (shouldConnect && currentGroup != rightGroup) {
                    Set<Cell> rightGroupCells = groupToCells.remove(rightGroup);
                    groupToCells.get(currentGroup).addAll(rightGroupCells);
                    rightGroupCells.forEach(cell -> cellToGroup.put(cell, currentGroup));
                    current.removeWall(Direction.EAST);
                    right.removeWall(Direction.WEST);
                }
            }

            // 3. lépés: minden csoportból képezünk legalább egy véletlen függőleges kapcsolatot
            Set<Integer> groupsInCurrentRow = Arrays.stream(grid[row])
                    .map(cellToGroup::get)
                    .collect(Collectors.toSet());
            for (Integer currentGroup : groupsInCurrentRow) {
                int currentRow = row;
                Set<Cell> cells = groupToCells.get(currentGroup).stream()
                        .filter(cell -> cell.getRow() == currentRow)
                        .collect(Collectors.toSet());
                int numOfCellsToSelect = cells.size() == 1 ? 1 : random.nextInt(cells.size()) + 1;
                Iterator<Cell> cellsIterator = cells.iterator();
                while (numOfCellsToSelect > 0) {
                    Cell current = cellsIterator.next();
                    Cell below = grid[row + 1][current.getColumn()];

                    groupToCells.get(currentGroup).add(below);
                    cellToGroup.put(below, currentGroup);
                    current.removeWall(Direction.SOUTH);
                    below.removeWall(Direction.NORTH);

                    numOfCellsToSelect--;
                }
            }

            // 4. lépés: a következő sor minden mezőjét ami még nem tartozik csoportba, betesszük egy új csoportba
            for (int column = 0; column < grid[row + 1].length; column++) {
                Cell current = grid[row + 1][column];
                if (!cellToGroup.containsKey(current)) {
                    int group = groupCounter++;
                    cellToGroup.put(current, group);
                    HashSet<Cell> cellGroup = new HashSet<>();
                    cellGroup.add(current);
                    groupToCells.put(group, cellGroup);
                }
            }
        }

        // 5. lépés: összekötünk minden mezőt akik különböző csoportba tartoznak, balról-jobbra haladva
        int lastRowIndex = grid.length - 1;
        for (int column = 0; column < grid[lastRowIndex].length - 1; column++) {
            Cell current = grid[lastRowIndex][column];
            int currentGroup = cellToGroup.get(current);

            Cell right = grid[lastRowIndex][column + 1];
            int rightGroup = cellToGroup.get(right);

            if (currentGroup != rightGroup) {
                Set<Cell> rightGroupCells = groupToCells.remove(rightGroup);
                groupToCells.get(currentGroup).addAll(rightGroupCells);
                rightGroupCells.forEach(cell -> cellToGroup.put(cell, currentGroup));
                current.removeWall(Direction.EAST);
                right.removeWall(Direction.WEST);
            }
        }
    }
}
