package tömbök.sudoku;

public interface Command {

    boolean acceptsCommand(String command);

    void execute(String[] commandWithParams);

}
