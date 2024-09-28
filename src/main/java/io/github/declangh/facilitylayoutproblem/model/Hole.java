package io.github.declangh.facilitylayoutproblem.model;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;


public class Hole {
    public static final Color COLOR = new Color(238,238,238);

    private final int xCoordinate;
    private final int yCoordinate;
    private Station station;

    public Hole(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.station = null;
    }

    public Hole(int xCoordinate, int yCoordinate, Station station) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.station = station;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public int[] getCoordinates() {
        return new int[]{xCoordinate, yCoordinate};
    }

    public Station getStation() {
        return station;
    }

    public boolean setStation(@NotNull Station station) {
        if (this.station == null) {
            this.station = station;
            return true;
        }
        return false;
    }

    public boolean removeStation() {
        if (station != null) {
            station = null;
            return true;
        }
        return false;
    }

    public boolean isOccupied() {
        return station != null;
    }

    public Hole deepCopy() {
        return new Hole(this.xCoordinate, this.yCoordinate, isOccupied() ? this.station.deepCopy() : null);
    }
}
