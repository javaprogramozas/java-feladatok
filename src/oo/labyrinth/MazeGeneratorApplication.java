package oo.labyrinth;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MazeGeneratorApplication extends Application {

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new MazeView(30)));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
