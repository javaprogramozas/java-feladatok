package oo.labyrinth.generator.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CellGroups {

    private Map<Cell, Integer> cellToGroup = new HashMap<>();
    private Map<Integer, Set<Cell>> groupToCells = new HashMap<>();

    private int groupCounter = 0;

    public int getCellGroup(Cell cell) {
        return cellToGroup.getOrDefault(cell, -1);
    }

    public Set<Cell> getCellsInGroup(int group) {
        return new HashSet<>(groupToCells.getOrDefault(group, Set.of()));
    }

    public boolean hasGroup(Cell cell) {
        return cellToGroup.containsKey(cell);
    }

    public void addCellToNewGroup(Cell cell) {
        addCellToGroup(cell, groupCounter++);
    }

    public void addCellToGroup(Cell cell, int group) {
        cellToGroup.put(cell, group);
        groupToCells.computeIfAbsent(group, key -> new HashSet<>()).add(cell);
    }

    public void moveCellsToGroup(int oldGroup, int newGroup) {
        Set<Cell> cellsToMove = groupToCells.remove(oldGroup);
        cellsToMove.forEach(cell -> addCellToGroup(cell, newGroup));
    }
}
