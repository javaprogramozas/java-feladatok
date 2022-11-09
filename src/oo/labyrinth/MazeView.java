package oo.labyrinth;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MazeView extends VBox {

    private final MazeViewModel viewModel;
    private TilePane tilePane;
    private Button generateButton;
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
                // TODO bind cell to view model
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
        generateButton = createButton("Generate", this::handleGenerateButton);
        HBox buttons = new HBox(generateButton);
        buttons.setSpacing(5);
        return buttons;
    }

    private void handleGenerateButton(ActionEvent event) {
        // TODO start maze generation
    }

}
