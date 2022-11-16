package oo.labyrinth;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MazeViewModel {

    private List<Property<ObservableSet<Direction>>> wallsPropertyList;
    private int rows, columns;

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
        Maze maze = new Maze(rows, columns);
        long startTime = System.currentTimeMillis();
        maze.generate();
        long endTime = System.currentTimeMillis();
        long durationTime = endTime - startTime;
        System.out.println("Maze generated in " + durationTime + " ms");
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Cell cell = maze.getCell(row, column);
                wallsPropertyList.get(index).setValue(FXCollections.observableSet(cell.getWalls()));
                index++;
            }
        }
    }
}
