package oo.labyrinth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        int size = 10;
        Maze maze = new Maze(size);

        Set<Cell> visitedCells = new HashSet<>();
        LinkedList<Cell> trackedCells = new LinkedList<>();

        Cell startingCell = maze.getCell(0, 0);

        visitedCells.add(startingCell);
        trackedCells.add(startingCell);

        while (!trackedCells.isEmpty()) {
            Cell current = trackedCells.getLast();
            Optional<CellWithDirection> selected = selectRandomNeighbour(maze, visitedCells, current);

            if (selected.isPresent()) {
                Cell next = maze.getCell(current, selected.get().direction());
                trackedCells.addLast(next);
                visitedCells.add(next);
                current.removeWall(selected.get().direction());
                next.removeWall(selected.get().direction().opposite());
            } else {
                trackedCells.removeLast();
            }
        }

        System.out.println("_".repeat(2 * size + 1));
        for (int row = 0; row < size; row++) {
            System.out.print("|");
            for (int column = 0; column < size; column++) {
                Cell cell = maze.getCell(row, column);
                if (cell.hasWall(Direction.SOUTH)) {
                    System.out.print("_");
                } else {
                    System.out.print(" ");
                }
                if (cell.hasWall(Direction.EAST)) {
                    System.out.print("|");
                } else {
                    System.out.print("_");
                }
            }
            System.out.println();
        }

        System.out.println("Vége");
    }

    private static Optional<CellWithDirection> selectRandomNeighbour(Maze maze, Set<Cell> visitedCells, Cell current) {
        List<CellWithDirection> neighboursDirections = new ArrayList<>(maze.getNeighboursDirections(current));
        Collections.shuffle(neighboursDirections);
        return neighboursDirections.stream()
                .filter(cwd -> !visitedCells.contains(maze.getCell(current, cwd.direction())))
                .findAny();
    }
}
