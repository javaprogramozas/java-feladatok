package tömbök.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Step {

    private Step previous;

    private int row, column;

    private List<Integer> possibleValues;

    public Step(Step previous, int row, int column, Set<Integer> possibleValues) {
        this.previous = previous;
        this.row = row;
        this.column = column;
        this.possibleValues = new ArrayList<>(possibleValues);
        Collections.shuffle(this.possibleValues);
    }

    public Step getPrevious() {
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
