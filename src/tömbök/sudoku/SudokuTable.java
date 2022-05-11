package tömbök.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SudokuTable {

    public static final int SUDOKU_SIZE = 9;
    public static final int EMPTY_VALUE = 0;
    private static final Set<Integer> ALL_POSSIBLE_VALUES = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    private final Random random = new Random();
    private int[][] data;

    public SudokuTable() {
        this(new int[SUDOKU_SIZE][SUDOKU_SIZE]);
    }

    public SudokuTable(int[][] data) {
        validateInputData(data);
        this.data = data;
    }

    public Set<Integer> getPossibleValues(int row, int column) {
        Set<Integer> possibleValues = getPossibleValuesInRow(row);
        possibleValues.retainAll(getPossibleValuesInColumn(column));
        possibleValues.retainAll(getPossibleValuesInZone(convertRowAndColumToZone(row, column)));
        return possibleValues;
    }

    public void generate() {
        int row = 0, column = 0;
        GenerateStep step = new GenerateStep(null, row, column, getPossibleValues(row, column));
        while (true) {
            if (!step.getPossibleValues().isEmpty()) {
                Integer value = step.getPossibleValues().remove(0);
                data[row][column] = value;
                if (allCellsAreFilled()) {
                    break;
                }
                row = column == 8 ? row + 1 : row;
                column = column == 8 ? 0 : column + 1;
                step = new GenerateStep(step, row, column, getPossibleValues(row, column));
            } else {
                data[row][column] = EMPTY_VALUE;
                step = step.getPrevious();
                row = step.getRow();
                column = step.getColumn();
            }
        }
    }

    public void createPuzzle(int numberInPuzzle) {
        generate();
        List<Position> allFilledPositions = getAllFilledPositions();
        Position position = allFilledPositions.get(random.nextInt(allFilledPositions.size()));
        RemoveStep step = new RemoveStep(null, position, getValue(position));
        clear(position);

        int removedCount = 1;

        while (true) {
            if (hasCellWithExactlyOnePossibleValue()) {
                if (removedCount == SUDOKU_SIZE * SUDOKU_SIZE - numberInPuzzle) {
                    break;
                }
                allFilledPositions = getAllFilledPositions();
                allFilledPositions.removeAll(step.previouslyRemovedPositions());
                if (allFilledPositions.isEmpty()) {
                    throw new IllegalStateException("Not possible to create puzzle");
                }
                position = allFilledPositions.get(random.nextInt(allFilledPositions.size()));
                step = new RemoveStep(step, position, getValue(position));
                clear(position);
                removedCount++;
            } else {
                RemoveStep previous = step.previous();
                setValue(step.position(), step.value());
                removedCount--;
                previous.previouslyRemovedPositions().add(step.position());
                step = previous;
            }
        }
    }

    private List<Position> getAllFilledPositions() {
        List<Position> results = new ArrayList<>();
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                if (data[row][column] != EMPTY_VALUE) {
                    results.add(new Position(row, column));
                }
            }
        }
        return results;
    }

    private void clear(Position position) {
        setValue(position, EMPTY_VALUE);
    }

    private int getValue(Position position) {
        return data[position.row()][position.column()];
    }

    private void setValue(Position position, int value) {
        data[position.row()][position.column()] = value;
    }

    private boolean hasCellWithExactlyOnePossibleValue() {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                if (data[row][column] == EMPTY_VALUE && getPossibleValues(row, column).size() == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            builder.append(Arrays.toString(data[i]))
                    .append("\n");
        }
        return builder.toString();
    }

    private boolean allCellsAreFilled() {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                if (data[row][column] == EMPTY_VALUE) {
                    return false;
                }
            }
        }
        return true;
    }

    private Set<Integer> getPossibleValuesInRow(int row) {
        Set<Integer> result = new HashSet<>(ALL_POSSIBLE_VALUES);
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            result.remove(data[row][i]);
        }
        return result;
    }

    private Set<Integer> getPossibleValuesInColumn(int column) {
        Set<Integer> result = new HashSet<>(ALL_POSSIBLE_VALUES);
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            result.remove(data[i][column]);
        }
        return result;
    }

    private Set<Integer> getPossibleValuesInZone(int zone) {
        Set<Integer> result = new HashSet<>(ALL_POSSIBLE_VALUES);
        int column = (zone % 3) * 3;
        int row = (zone / 3) * 3;
        for (int i = 0; i < SUDOKU_SIZE / 3; i++) {
            for (int j = 0; j < SUDOKU_SIZE / 3; j++) {
                result.remove(data[row + i][column + j]);
            }
        }
        return result;
    }

    private int convertRowAndColumToZone(int row, int column) {
        return (row / 3) * 3 + (column / 3);
    }

    private void validateInputData(int[][] data) {
        if (data.length != SUDOKU_SIZE) {
            throw new IllegalArgumentException("Sudoku size must be 9x9");
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i].length != SUDOKU_SIZE) {
                throw new IllegalArgumentException("Sudoku size must be 9x9");
            }
        }
    }

}
