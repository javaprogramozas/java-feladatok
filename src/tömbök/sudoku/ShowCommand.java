package tömbök.sudoku;

import java.util.Set;

public class ShowCommand extends AbstractCommand {

    protected ShowCommand(SudokuTable table) {
        super(Set.of("show", "sh"), table);
    }

    @Override
    public void execute(String[] commandWithParams) {
        System.out.println(table);
    }
}
