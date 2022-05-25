package tömbök.sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static tömbök.sudoku.Position.position;

public class SudokuTable {

    public static final int SUDOKU_SIZE = 9;
    public static final int NUMBER_OF_ZONES = SUDOKU_SIZE / 3;
    public static final int ZONE_SIZE = SUDOKU_SIZE / NUMBER_OF_ZONES;
    public static final int EMPTY_VALUE = 0;
    private static final Set<Integer> ALL_POSSIBLE_VALUES = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    private final Random random = new Random();
    private final int[][] data;
    private final Set<Position> userFilledPositions = new HashSet<>();

    public SudokuTable() {
        this(new int[SUDOKU_SIZE][SUDOKU_SIZE]);
    }

    public SudokuTable(int[][] data) {
        validateInputSize(data);
        this.data = data;
    }

    public void solve() {
        solve(1, false);
    }

    public void createPuzzle(int numberInPuzzle) {
        clear();
        solve(1, false);
        //System.out.println(this);
        List<Position> allFilledPositions = getAllFilledPositions();
        Position position = allFilledPositions.get(random.nextInt(allFilledPositions.size()));
        RemoveStep step = new RemoveStep(null, position, getValue(position));
        clear(position);

        int removedCount = 1;

        while (true) {
            int count = solve(2, true);
            if (count == 1) {
                if (removedCount == SUDOKU_SIZE * SUDOKU_SIZE - numberInPuzzle) {
                    break;
                }
                allFilledPositions = getAllFilledPositions();
                allFilledPositions.removeAll(step.previouslyRemovedPositions());
                if (allFilledPositions.isEmpty()) {
                    RemoveStep previous = step.previous();
                    if (previous == null) {
                        throw new IllegalStateException("Not possible to create puzzle");
                    }
                    setValueInternal(step.position(), step.value());
                    removedCount--;
                    previous.previouslyRemovedPositions().add(step.position());
                    step = previous;
                    continue;
                }
                position = allFilledPositions.get(random.nextInt(allFilledPositions.size()));
                step = new RemoveStep(step, position, getValue(position));
                clear(position);
                removedCount++;
            } else {
                RemoveStep previous = step.previous();
                if (previous == null) {
                    throw new IllegalStateException("Not possible to create puzzle");
                }
                setValueInternal(step.position(), step.value());
                removedCount--;
                previous.previouslyRemovedPositions().add(step.position());
                step = previous;
            }
        }
    }

    public void clear() {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                clear(row, column);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            if (row % ZONE_SIZE == 0) {
                builder.append("+---+---+---+\n");
            }
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                if (column % ZONE_SIZE == 0) {
                    builder.append("|");
                }
                builder.append(getValue(row, column));
            }
            builder.append("|\n");
        }
        builder.append("+---+---+---+");
        return builder.toString();
    }

    private int solve(int maxCount, boolean undoChanges) {
        Position position = findNextEmptyPosition(position(0, -1))
                .orElseThrow(() -> new IllegalStateException("The table is already full"));
        GenerateStep step = new GenerateStep(null, position, getPossibleValues(position));
        int count = 0;
        while (count < maxCount) {
            if (!step.possibleValues().isEmpty()) {
                Integer value = step.possibleValues().remove(0);
                setValueInternal(position, value);
                if (allCellsAreFilled()) {
                    count++;
                    continue;
                }
                Position error = position;
                position = findNextEmptyPosition(position)
                        .orElseThrow(() -> new IllegalStateException("No more positions to fill: " + error));
                step = new GenerateStep(step, position, getPossibleValues(position));
            } else {
                clear(position);
                step = step.previous();
                if (step == null) {
                    return count;
                }
                position = step.position();
            }
        }

        if (undoChanges) {
            while (step != null) {
                clear(step.position());
                step = step.previous();
            }
        }
        return count;
    }

    private List<Position> getAllFilledPositions() {
        List<Position> results = new ArrayList<>();
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                if (!isEmpty(row, column)) {
                    results.add(new Position(row, column));
                }
            }
        }
        return results;
    }

    private int getValue(Position position) {
        return getValue(position.row(), position.column());
    }

    private int getValue(int row, int column) {
        return data[row][column];
    }

    public boolean setValue(int row, int column, int value) {
        Position position = position(row, column);
        if (ALL_POSSIBLE_VALUES.contains(value)
                && (isEmpty(row, column) || userFilledPositions.contains(position))
                && getPossibleValues(row, column).contains(value)) {
            setValueInternal(row, column, value);
            userFilledPositions.add(position);
            return true;
        }
        return false;
    }

    private void setValueInternal(Position position, int value) {
        setValueInternal(position.row(), position.column(), value);
    }

    private void setValueInternal(int row, int column, int value) {
        data[row][column] = value;
    }

    private boolean isEmpty(Position position) {
        return isEmpty(position.row(), position.column());
    }

    private boolean isEmpty(int row, int column) {
        return getValue(row, column) == EMPTY_VALUE;
    }

    private void clear(Position position) {
        clear(position.row(), position.column());
    }

    private void clear(int row, int column) {
        setValueInternal(row, column, EMPTY_VALUE);
    }

    private boolean allCellsAreFilled() {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                if (isEmpty(row, column)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Set<Integer> getPossibleValues(Position position) {
        return getPossibleValues(position.row(), position.column());
    }

    private Set<Integer> getPossibleValues(int row, int column) {
        Set<Integer> possibleValues = getPossibleValuesInRow(row);
        possibleValues.retainAll(getPossibleValuesInColumn(column));
        possibleValues.retainAll(getPossibleValuesInZone(convertRowAndColumToZone(row, column)));
        return possibleValues;
    }

    private Set<Integer> getPossibleValuesInRow(int row) {
        Set<Integer> result = new HashSet<>(ALL_POSSIBLE_VALUES);
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            result.remove(getValue(row, i));
        }
        return result;
    }

    private Set<Integer> getPossibleValuesInColumn(int column) {
        Set<Integer> result = new HashSet<>(ALL_POSSIBLE_VALUES);
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            result.remove(getValue(i, column));
        }
        return result;
    }

    private Set<Integer> getPossibleValuesInZone(int zone) {
        Set<Integer> result = new HashSet<>(ALL_POSSIBLE_VALUES);
        int column = (zone % ZONE_SIZE) * ZONE_SIZE;
        int row = (zone / ZONE_SIZE) * ZONE_SIZE;
        for (int i = 0; i < ZONE_SIZE; i++) {
            for (int j = 0; j < ZONE_SIZE; j++) {
                result.remove(getValue(row + i, column + j));
            }
        }
        return result;
    }

    private int convertRowAndColumToZone(int row, int column) {
        return (row / ZONE_SIZE) * ZONE_SIZE + (column / ZONE_SIZE);
    }

    private void validateInputSize(int[][] data) {
        if (data.length != SUDOKU_SIZE) {
            throw new IllegalArgumentException("Sudoku size must be 9x9");
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i].length != SUDOKU_SIZE) {
                throw new IllegalArgumentException("Sudoku size must be 9x9");
            }
        }
    }

    private Optional<Position> findNextEmptyPosition(Position position) {
        Position current = position;
        while (current.hasNext()) {
            current = current.next();
            if (isEmpty(current)) {
                return Optional.of(current);
            }
        }
        return Optional.empty();
    }
}
