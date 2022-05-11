package tömbök.sudoku;

public class T3 {

    public static void main(String[] args) {
        SudokuTable sudokuTable = new SudokuTable();
        sudokuTable.createPuzzle(21);

        System.out.println("================");
        System.out.println(sudokuTable);
    }
}
