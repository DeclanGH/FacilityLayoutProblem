package io.github.declangh.facilitylayoutproblem.model;

import java.awt.Color;

public enum StationType {
    MANUFACTURING(new Color(204, 102, 0)),
    DISTRIBUTION(new Color(66, 0, 248)),
    SECURITY(new Color(9,93,216)),
    HEALTH(new Color(57, 255, 20)),
    POWER(new Color(128, 128, 128)),
    HOME(new Color(81, 59, 41)),
    FOOD(new Color(247, 255, 0)),
    FIRE(new Color(255, 51, 51));

    private final Color color;

    StationType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
