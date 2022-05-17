package tömbök.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public record GenerateStep(GenerateStep previous, Position position, List<Integer> possibleValues) {

    public GenerateStep(GenerateStep previous, Position position, Set<Integer> possibleValues) {
        this(previous, position, new ArrayList<>(possibleValues));
        Collections.shuffle(this.possibleValues);
    }
}
