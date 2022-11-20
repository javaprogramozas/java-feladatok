package oo.labyrinth.view;

import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.SetChangeListener;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import oo.labyrinth.generator.model.Direction;
import oo.labyrinth.view.BorderRegistry;

public class ViewCell extends Region implements SetChangeListener<Direction> {

    private BorderRegistry registry;
    private SetProperty<Direction> walls = new SimpleSetProperty<>();

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

    public SetProperty<Direction> wallsProperty() {
        return walls;
    }
}
