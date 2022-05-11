package tömbök.sudoku;

import java.util.ArrayList;
import java.util.List;

public record RemoveStep(RemoveStep previous,
                         Position position,
                         int value,
                         List<Position> previouslyRemovedPositions) {

    public RemoveStep(RemoveStep previous,
                      Position position,
                      int value) {
        this(previous, position, value, new ArrayList<>());
    }
}
