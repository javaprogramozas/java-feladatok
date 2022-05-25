package tömbök.sudoku;

import java.util.Set;

public class ExitCommand extends AbstractCommand {


    protected ExitCommand(SudokuTable table) {
        super(Set.of("exit"), table);
    }

    @Override
    public void execute(String[] commandWithParams) {
        System.exit(0);
    }
}
