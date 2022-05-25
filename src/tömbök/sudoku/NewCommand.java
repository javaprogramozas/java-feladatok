package tömbök.sudoku;

import java.util.Set;

public class NewCommand extends AbstractCommand {

    public NewCommand(SudokuTable table) {
        super(Set.of("new", "n"), table);
    }

    @Override
    public void execute(String[] commandWithParams) {
        table.createPuzzle(25);
        System.out.println("New Sudoku board created");
        System.out.println(table);
    }
}
