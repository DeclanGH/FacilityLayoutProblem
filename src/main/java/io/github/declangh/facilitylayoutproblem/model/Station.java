package io.github.declangh.facilitylayoutproblem.model;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class Station {

    private final int stationId;
    private final StationType stationType;

    public Station(int stationId, @NotNull StationType stationType) {
        this.stationType = stationType;
        this.stationId = stationId;
    }

    public Station deepCopy() {
        return new Station(this.stationId, this.stationType);
    }

    public StationType getType() {
        return stationType;
    }

    public int getId() {
        return stationId;
    }

    public String getIdAsString() {
        return String.valueOf(stationId);
    }

    public Color getColor() {
        return stationType.getColor();
    }
}
