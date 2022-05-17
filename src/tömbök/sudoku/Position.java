package tömbök.sudoku;

import static tömbök.sudoku.SudokuTable.SUDOKU_SIZE;

public record Position(int row, int column) {

    public boolean hasNext() {
        return row < SUDOKU_SIZE - 1 || column < SUDOKU_SIZE - 1;
    }

    public Position next() {
        int newColumn = column + 1;
        int carryOver = newColumn / SUDOKU_SIZE;
        return new Position(row + carryOver, newColumn % SUDOKU_SIZE);
    }

    public static Position position(int row, int column) {
        return new Position(row, column);
    }
}
