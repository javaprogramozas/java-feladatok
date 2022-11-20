package oo.labyrinth.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import oo.labyrinth.generator.model.MazeGeneratorType;

import java.util.EnumSet;
import java.util.List;

public class MazeView extends VBox {

    private final MazeViewModel viewModel;
    private TilePane tilePane;
    private Button generateButton;
    private ComboBox<MazeGeneratorType> typeSelector;
    private BorderRegistry borderRegistry = new BorderRegistry();

    public MazeView(int size) {
        this.viewModel = new MazeViewModel(size, size);
        createView(size, size);
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
            if (node instanceof ViewCell cell) {
                cell.wallsProperty().bindBidirectional(viewModel.getWallsProperty(i));
            }
        }
    }

    private TilePane createTilePane(int numOfRows, int numOfColumns) {
        TilePane tilePane = new TilePane();
        tilePane.setPrefColumns(numOfColumns);
        tilePane.setPrefRows(numOfRows);
        tilePane.setTileAlignment( Pos.CENTER );
        tilePane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        tilePane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        tilePane.setBackground(Background.fill(Color.GRAY));
        tilePane.setBorder(Border.stroke(Color.GRAY));

        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                tilePane.getChildren().add(new ViewCell(borderRegistry));
            }
        }
        return tilePane;
    }

    private Button createButton(String title, EventHandler<ActionEvent> handler) {
        Button startButton = new Button(title);
        startButton.setOnAction(handler);
        return startButton;
    }

    private HBox createButtons() {
        createTypeSelector();
        generateButton = createButton("Generate", this::handleGenerateButton);
        HBox buttons = new HBox(typeSelector, generateButton);
        buttons.setSpacing(5);
        return buttons;
    }

    private void createTypeSelector() {
        typeSelector = new ComboBox<>(FXCollections.observableList(List.copyOf(EnumSet.allOf(MazeGeneratorType.class))));
        typeSelector.getSelectionModel().selectFirst();
        typeSelector.setOnAction(this::handleTypeSelector);
        typeSelector.setCellFactory(view -> createListCell());
        typeSelector.setButtonCell(createListCell());
    }

    private static ListCell<MazeGeneratorType> createListCell() {
        return new ListCell<>() {
            @Override
            public void updateItem(MazeGeneratorType item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                } else {
                    setText(item.label);
                }
            }
        };
    }

    private void handleTypeSelector(ActionEvent actionEvent) {
        viewModel.setMazeGeneratorType(typeSelector.getValue());
    }

    private void handleGenerateButton(ActionEvent event) {
        viewModel.generateMaze();
    }

}
