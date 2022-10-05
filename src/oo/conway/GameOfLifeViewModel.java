package oo.conway;

import javafx.application.Platform;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;

public class GameOfLifeViewModel {

    private final List<BooleanProperty> cellStates;
    private final BooleanProperty simulationRunning = new SimpleBooleanProperty(false);
    private final GameOfLifeModel model;
    private Thread worker = null;

    public GameOfLifeViewModel(int rows, int columns) {
        int numberOfCells = rows * columns;
        this.cellStates = new ArrayList<>(numberOfCells);
        for (int i = 0; i < numberOfCells; i++) {
            this.cellStates.add(new SimpleBooleanProperty(false));
        }
        this.model = new GameOfLifeModel(rows, columns);
    }

    public void toggleSimulationExecution() {
        flip(simulationRunning);
        if (simulationRunning.get()) {
            worker = new Thread(this::scheduleSimulationStepping);
            worker.setDaemon(true);
            worker.start();
        } else {
            worker.interrupt();
            worker = null;
        }
    }

    private void scheduleSimulationStepping() {
        while (true) {
            Platform.runLater(this::stepSimulation);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void clearSimulationArea() {
        cellStates.forEach(cellState -> cellState.set(false));
    }

    public void randomizeSimulationArea() {
        List<Integer> indicesToFlip = model.provideRandomCellPositions();
        indicesToFlip.forEach(index -> flip(cellStates.get(index)));
    }

    public void stepSimulation() {
        List<Boolean> booleanCellStates = cellStates.stream().map(BooleanExpression::getValue).toList();
        long start = System.currentTimeMillis();
        List<Integer> indicesToFlip = model.calculateNextState(booleanCellStates);
        long stop = System.currentTimeMillis();
        System.out.println("Execution time: " + (stop - start) + " ms");
        indicesToFlip.forEach(index -> flip(cellStates.get(index)));
    }

    public void toggleCell(int index) {
        flip(this.cellStates.get(index));
    }

    public Property<Boolean> getCellProperty(int index) {
        return this.cellStates.get(index);
    }

    public Property<Boolean> getSimulationExecutionProperty() {
        return this.simulationRunning;
    }

    private void flip(BooleanProperty booleanProperty) {
        booleanProperty.set(!booleanProperty.get());
    }
}
