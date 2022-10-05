package oo.conway;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;


public class GameOfLife extends Application {

    private Cell[][] cells;
    private int numOfRows = 30;
    private int numOfColumns = 30;

    private Button startButton;
    private Button clearButton;
    private Button randomButton;
    private Button stepButton;

    private GameEngine engine = null;
    private Thread worker = null;

    private Parent createContent() {
        TilePane tilePane = new TilePane();
        tilePane.setPrefColumns(numOfColumns);
        tilePane.setPrefRows(numOfRows);
        tilePane.setTileAlignment( Pos.CENTER );
        tilePane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        tilePane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        tilePane.setHgap(1);
        tilePane.setVgap(1);
        tilePane.setBackground(Background.fill(Color.GRAY));
        tilePane.setBorder(Border.stroke(Color.GRAY));

        cells = new Cell[numOfRows][numOfColumns];
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                Cell cell = new Cell();
                cells[i][j] = cell;
                tilePane.getChildren().add(cell);
            }
        }
        engine = new GameEngine(cells);

        tilePane.setOnMouseClicked(event -> tilePane.getChildren().stream()
            .filter(cell -> cell.contains(cell.sceneToLocal(event.getSceneX(), event.getSceneY(), true)))
            .findFirst()
            .ifPresent(cell -> ((Cell)cell).flip())
        );

        StackPane stackPane = new StackPane(tilePane);
        startButton = new Button("Start");
        clearButton = new Button("Clear");
        randomButton = new Button("Random");
        stepButton = new Button("Step");
        startButton.setOnAction(this::handleStartButton);
        clearButton.setOnAction(this::handleClearButton);
        randomButton.setOnAction(this::handleRandomButton);
        stepButton.setOnAction(this::handleStepButton);
        HBox buttons = new HBox(startButton, clearButton, randomButton, stepButton);
        buttons.setSpacing(5);
        return new VBox(stackPane, buttons);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void handleStartButton(ActionEvent event) {
        if (worker == null) {
            worker = new Thread(new GameEngine(cells));
            worker.setDaemon(true);
            worker.start();
            startButton.setText("Stop");
            clearButton.setDisable(true);
            randomButton.setDisable(true);
            stepButton.setDisable(true);
        } else {
            worker.interrupt();
            worker = null;
            startButton.setText("Start");
            clearButton.setDisable(false);
            randomButton.setDisable(false);
            stepButton.setDisable(false);
        }
    }

    private void handleClearButton(ActionEvent actionEvent) {
        for (Cell[] cellRow : cells) {
            for (Cell cell : cellRow) {
                cell.reset();
            }
        }
    }

    private void handleRandomButton(ActionEvent actionEvent) {
        Random random = new Random();
        for (Cell[] cellRow : cells) {
            for (Cell cell : cellRow) {
                if (random.nextInt(100) < 20) {
                    cell.flip();
                }
            }
        }
    }

    private void handleStepButton(ActionEvent actionEvent) {
        engine.tick();
    }
}