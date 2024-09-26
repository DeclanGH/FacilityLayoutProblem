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
            case DISTRIBUTION -> this.stationColor = new Color(66, 0, 248);
            case SECURITY -> this.stationColor = new Color(9,93,216);
            case HEALTH -> this.stationColor = new Color(57, 255, 20);
            case POWER -> this.stationColor = new Color(128, 128, 128);
            case HOME -> this.stationColor = new Color(81, 59, 41);
            case FOOD -> this.stationColor = new Color(247, 255, 0);
            case FIRE -> this.stationColor = new Color(255, 51, 51);
        }
    }

    public Station(@NotNull Station station) {
        this.stationId = station.getStationId();
        this.stationType = station.getStationType();
        this.stationColor = station.getStationColor();
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
