package oo.conway;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {

    private final BooleanProperty aliveProperty = new SimpleBooleanProperty(false);

    public Cell() {
        super(20, 20, Color.WHITE);
        aliveProperty.addListener(this::handleAliveChange);
    }

    private void handleAliveChange(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            setFill(Color.BLACK);
        } else {
            setFill(Color.WHITE);
        }
    }

    public BooleanProperty aliveProperty() {
        return aliveProperty;
    }
}
