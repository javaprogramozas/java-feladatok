package oo.labyrinth.generator.model;

import java.util.EnumSet;

public class Cell {

    private EnumSet<Direction> walls = EnumSet.allOf(Direction.class);
    private final int row, column;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void removeWall(Direction direction) {
        walls.remove(direction);
    }

    public boolean hasWall(Direction direction) {
        return walls.contains(direction);
    }

    public EnumSet<Direction> getWalls() {
        return walls;
    }
}
