package tömbök.sudoku;

import java.util.Set;

public abstract class AbstractCommand implements Command {

    private final Set<String> acceptedWords;

    protected SudokuTable table;

    protected AbstractCommand(Set<String> acceptedWords, SudokuTable table) {
        this.acceptedWords = acceptedWords;
        this.table = table;
    }

    @Override
    public boolean acceptsCommand(String command) {
        return acceptedWords.contains(command);
    }
}
