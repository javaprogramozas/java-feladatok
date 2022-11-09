package oo.labyrinth;

public enum Direction {

    NORTH(-1, 0, 1),
    EAST(0, 1, 2),
    SOUTH(1, 0, 4),
    WEST(0, -1, 8);

    public final int rowDelta, columnDelta, key;

    Direction(int rowDelta, int columnDelta, int key) {
        this.rowDelta = rowDelta;
        this.columnDelta = columnDelta;
        this.key = key;
    }

    public Direction opposite() {
        for (Direction direction : values()) {
            if (direction.rowDelta == -rowDelta && direction.columnDelta == -columnDelta) {
                return direction;
            }
        }
        throw new IllegalStateException("Direction " + this.name() + " has no opposite!");
    }
}
