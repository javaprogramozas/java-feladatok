package oo.labyrinth;

public enum Direction {

    NORTH(-1, 0),
    EAST(0, 1),
    SOUTH(1, 0),
    WEST(0, -1);

    public final int rowDelta, columnDelta;

    Direction(int rowDelta, int columnDelta) {
        this.rowDelta = rowDelta;
        this.columnDelta = columnDelta;
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
