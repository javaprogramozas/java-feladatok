package oo.labyrinth.view;

import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;

public class BorderStrokeBuilder {

    private Color topStroke = Color.WHITE;
    private Color rightStroke = Color.WHITE;
    private Color bottomStroke = Color.WHITE;
    private Color leftStroke = Color.WHITE;

    private BorderStrokeBuilder() {
    }

    public BorderStrokeBuilder topStroke(Color topStroke) {
        this.topStroke = topStroke;
        return this;
    }

    public BorderStrokeBuilder rightStroke(Color rightStroke) {
        this.rightStroke = rightStroke;
        return this;
    }

    public BorderStrokeBuilder bottomStroke(Color bottomStroke) {
        this.bottomStroke = bottomStroke;
        return this;
    }

    public BorderStrokeBuilder leftStroke(Color leftStroke) {
        this.leftStroke = leftStroke;
        return this;
    }

    public BorderStroke build() {
        return new BorderStroke(topStroke, rightStroke, bottomStroke, leftStroke,
                BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                null, null, null);
    }

    public static BorderStrokeBuilder builder() {
        return new BorderStrokeBuilder();
    }
}
