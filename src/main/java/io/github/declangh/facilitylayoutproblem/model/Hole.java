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

    /**
     * Attempts to place a Station on a spot.
     *
     * @param station The station to be placed on the spot
     */
    public void placeStation(@NotNull Station station) {
        if (this.station == null) {
            this.station = station;
        } else {
            throw new RuntimeException("Can't place Station in an occupied hole");
        }
    }

    /**
     * Removes a station from the Hole if any.
     * @return Station s where s is the station that was removed
     */
    public Station removeStation() {
        if (this.station != null) {
            Station clone = this.station.deepCopy();
            this.station = null;
            return clone;
        }
        return null;
    }

    public boolean isOccupied() {
        return station != null;
    }

    public Hole deepCopy() {
        return new Hole(this.xCoordinate, this.yCoordinate, isOccupied() ? this.station.deepCopy() : null);
    }
}
