package tömbök.sudoku;

public class T3 {

    public static void main(String[] args) {
        int[][] data = {
                {0, 3, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 0, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 0},
                {4, 0, 0, 8, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 0, 0, 0, 7, 0}
        };

        SudokuTable sudokuTable = new SudokuTable();

        System.out.println(sudokuTable.getPossibleValues(6, 5));
    }
}
