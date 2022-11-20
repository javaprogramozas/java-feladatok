package oo.labyrinth.view;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import oo.labyrinth.generator.AbstractMazeGenerator;
import oo.labyrinth.generator.impl.BacktrackMazeGenerator;
import oo.labyrinth.generator.impl.EllersMazeGenerator;
import oo.labyrinth.generator.model.MazeGeneratorType;
import oo.labyrinth.generator.model.Cell;
import oo.labyrinth.generator.model.Direction;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MazeViewModel {

    private List<Property<ObservableSet<Direction>>> wallsPropertyList;
    private int rows, columns;
    private MazeGeneratorType mazeGeneratorType = MazeGeneratorType.BACKTRACK;

    public MazeViewModel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        wallsPropertyList = new ArrayList<>(rows * columns);
        for (int i = 0; i < rows * columns; i++) {
            wallsPropertyList.add(new SimpleSetProperty<>(FXCollections.observableSet(EnumSet.allOf(Direction.class))));
        }
    }

    public Property<ObservableSet<Direction>> getWallsProperty(int index) {
        return wallsPropertyList.get(index);
    }

    public void generateMaze() {
        AbstractMazeGenerator generator = getMazeGenerator();
        long startTime = System.currentTimeMillis();
        generator.generate();
        long endTime = System.currentTimeMillis();
        long durationTime = endTime - startTime;
        System.out.println("Maze generated in " + durationTime + " ms");
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Cell cell = generator.getCell(row, column);
                wallsPropertyList.get(index).setValue(FXCollections.observableSet(cell.getWalls()));
                index++;
            }
        }
    }

    public void setMazeGeneratorType(MazeGeneratorType value) {
        this.mazeGeneratorType = value;
    }

    private AbstractMazeGenerator getMazeGenerator() {
        return switch (mazeGeneratorType) {
            case BACKTRACK -> new BacktrackMazeGenerator(rows, columns);
            case ELLERS -> new EllersMazeGenerator(rows, columns);
        };
    }
}
