package oo.conway;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GameOfLifeView extends VBox {

    private final GameOfLifeViewModel viewModel;
    private TilePane tilePane;
    private Button startButton;
    private Button clearButton;
    private Button randomButton;
    private Button stepButton;

    public GameOfLifeView(int rows, int columns) {
        this.viewModel = new GameOfLifeViewModel(rows, columns);
        createView(rows, columns);
        bindViewModel();
    }

    private void createView(int numOfRows, int numOfColumns) {
        tilePane = createTilePane(numOfRows, numOfColumns);

        StackPane stackPane = new StackPane(tilePane);
        HBox buttons = createButtons();
        this.getChildren().addAll(stackPane, buttons);
    }

    private void bindViewModel() {
        ObservableList<Node> children = tilePane.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node node = children.get(i);
            if (node instanceof Cell cell) {
                cell.aliveProperty().bindBidirectional(viewModel.getCellProperty(i));
            }
        }
        clearButton.disableProperty().bindBidirectional(viewModel.getSimulationExecutionProperty());
        randomButton.disableProperty().bindBidirectional(viewModel.getSimulationExecutionProperty());
        stepButton.disableProperty().bindBidirectional(viewModel.getSimulationExecutionProperty());
    }

    private TilePane createTilePane(int numOfRows, int numOfColumns) {
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

        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                tilePane.getChildren().add(new Cell());
            }
        }
        tilePane.setOnMouseClicked(this::handleTilePaneClick);
        return tilePane;
    }

    private Button createButton(String title, EventHandler<ActionEvent> handler) {
        Button startButton = new Button(title);
        startButton.setOnAction(handler);
        return startButton;
    }

    private HBox createButtons() {
        startButton = createButton("Start", this::handleStartButton);
        clearButton = createButton("Clear", this::handleClearButton);
        randomButton = createButton("Random", this::handleRandomButton);
        stepButton = createButton("Step", this::handleStepButton);
        HBox buttons = new HBox(startButton, clearButton, randomButton, stepButton);
        buttons.setSpacing(5);
        return buttons;
    }

    private void handleStartButton(ActionEvent event) {
        viewModel.toggleSimulationExecution();
        if (viewModel.getSimulationExecutionProperty().getValue()) {
            startButton.setText("Stop");
        } else {
            startButton.setText("Start");
        }
    }

    private void handleClearButton(ActionEvent actionEvent) {
       viewModel.clearSimulationArea();
    }

    private void handleRandomButton(ActionEvent actionEvent) {
        viewModel.randomizeSimulationArea();
    }

    private void handleStepButton(ActionEvent actionEvent) {
        viewModel.stepSimulation();
    }

    private void handleTilePaneClick(MouseEvent mouseEvent) {
        ObservableList<Node> children = tilePane.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node node = children.get(i);
            if (node.contains(node.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY(), true))) {
                viewModel.toggleCell(i);
                return;
            }
        }
    }
}
