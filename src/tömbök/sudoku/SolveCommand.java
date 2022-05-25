package tömbök.sudoku;

import java.util.Set;

public class SolveCommand extends AbstractCommand {
    protected SolveCommand(SudokuTable table) {
        super(Set.of("solve", "so"), table);
    }

    @Override
    public void execute(String[] commandWithParams) {
        table.solve();
        System.out.println(table);
    }
}
