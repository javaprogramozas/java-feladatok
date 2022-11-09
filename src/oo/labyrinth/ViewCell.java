package oo.labyrinth;

import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.SetChangeListener;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.EnumSet;

public class ViewCell extends Region implements SetChangeListener<Direction> {

    private BorderRegistry registry;
    private SetProperty<Direction> walls
            = new SimpleSetProperty<>(FXCollections.observableSet(EnumSet.of(Direction.SOUTH, Direction.EAST)));

    public ViewCell(BorderRegistry registry) {
        this.registry = registry;
        setMinSize(30, 30);
        setBorder(registry.getBorder(walls));
        setBackground(Background.fill(Color.WHITE));
        walls.addListener(this);
    }

    @Override
    public void onChanged(Change<? extends Direction> change) {
        setBorder(registry.getBorder(walls));
    }
}
