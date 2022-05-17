package tömbök.sudoku;

public class T3 {

    public static void main(String[] args) {
        SudokuTable sudokuTable = new SudokuTable();
        sudokuTable.createPuzzle(24);

        System.out.println("Puzzle:");
        System.out.println(sudokuTable);

        sudokuTable.solve();
        System.out.println("Solution:");
        System.out.println(sudokuTable);
    }
}
