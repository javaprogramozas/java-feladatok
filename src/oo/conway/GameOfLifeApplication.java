package oo.conway;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameOfLifeApplication extends Application {

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new GameOfLifeView(30, 30)));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}