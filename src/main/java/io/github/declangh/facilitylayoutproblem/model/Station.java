package io.github.declangh.facilitylayoutproblem.model;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Station {

    private final int stationId;
    private final StationType stationType;
    private Color stationColor;

    public Station(int stationId, @NotNull StationType stationType) {
        this.stationType = stationType;
        this.stationId = stationId;

        switch (stationType) {
            case MANUFACTURING -> this.stationColor = new Color(204, 102, 0);
            case DISTRIBUTION -> this.stationColor = new Color(204, 204, 0);
            case SECURITY -> this.stationColor = new Color(9,93,216);
            case HEALTH -> this.stationColor = new Color(57, 255, 20);
            case POWER -> this.stationColor = new Color(128, 128, 128);
            case FIRE -> this.stationColor = new Color(255, 51, 51);
        }
    }

    public StationType getStationType() {
        return stationType;
    }

    public int getStationId() {
        return stationId;
    }

    public Color getStationColor() {
        return stationColor;
    }
}
