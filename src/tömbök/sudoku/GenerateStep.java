package tömbök.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GenerateStep {

    private GenerateStep previous;

    private int row, column;

    private List<Integer> possibleValues;

    public GenerateStep(GenerateStep previous, int row, int column, Set<Integer> possibleValues) {
        this.previous = previous;
        this.row = row;
        this.column = column;
        this.possibleValues = new ArrayList<>(possibleValues);
        Collections.shuffle(this.possibleValues);
    }

    public GenerateStep getPrevious() {
        return previous;
    }

    public List<Integer> getPossibleValues() {
        return possibleValues;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
