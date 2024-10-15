package io.github.declangh.facilitylayoutproblem.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * A square ground with a list of {@link Hole} that can be occupied by a {@link Station}
 */
public class Floor {

    private double fitnessScore;
    private final int sideLength;
    private final int numberOfHoles;
    private final ArrayList<Hole> holes;

    /**
     * @param stations List of stations to be put on the floor
     */
    public Floor(@NotNull final List<Station> stations) {
        this.fitnessScore = Integer.MIN_VALUE;
        int numberOfSpots = stations.size();
        /* Since Floor is a square ground, we want holes to be a square value. To achieve this,
           holes must be the next square number when spots is tripled */
        sideLength = ((int) Math.sqrt(numberOfSpots*2)) + 1;
        this.numberOfHoles = sideLength*sideLength;
        this.holes = new ArrayList<>(numberOfHoles);

        populateFloor(stations);
    }

    /**
     * <p>Creates an empty square floor (one with no station) using the specified side length. Number of
     * holes on this floor will be the square of the side length.<p/>
     *
     * @throws IllegalArgumentException If the sideLength parameter is less than 1
     *
     * @param sideLength number of holes on one side of the floor.
     */
    public Floor(int sideLength) {
        if (sideLength <= 1) {
            throw new IllegalArgumentException("Cannot have a side length less than 1");
        }
        this.fitnessScore = Double.MIN_VALUE;
        this.sideLength = sideLength;
        this.numberOfHoles = sideLength*sideLength;
        this.holes = new ArrayList<>(numberOfHoles);

        for (int i = 0; i < this.sideLength; i++) {
            for (int j = 0; j < this.sideLength; j++) {
                holes.add(new Hole(i, j));
            }
        }
    }

    // for deep copying purposes
    private Floor(final double fitnessScore,
                  final int numberOfHoles,
                  final int sideLength,
                  @NotNull final ArrayList<Hole> holesCopy) {
        this.fitnessScore = fitnessScore;
        this.numberOfHoles = numberOfHoles;
        this.sideLength = sideLength;
        this.holes = holesCopy;
    }

    private void populateFloor(@NotNull List<Station> stations) {
        // make a deep copy
        List<Station> stationsCopy = stations.stream()
                .map(Station::deepCopy)
                .collect(Collectors.toCollection(ArrayList::new));
        // add null stations that will serve as unoccupied holes
        for (int i=stationsCopy.size(); i<numberOfHoles; i++) {
            stationsCopy.add(null);
        }
        // shuffle before putting in hole so that hole coordinates are not changed/disrupted
        shuffle(stationsCopy);

        int index = 0;
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                holes.add(new Hole(i, j, stationsCopy.get(index++)));
            }
        }
    }

    public double getFitnessScore() {
        return this.fitnessScore;
    }

    public void setFitnessScore(final double fitnessScore) {
        this.fitnessScore = fitnessScore;
    }

    public int getNumberOfHoles() {
        return numberOfHoles;
    }

    public int getSideLength() {
        return sideLength;
    }

    public ArrayList<Hole> getHoles() {
        return holes;
    }

    private void shuffle(@NotNull List<Station> stations) {
        for (int currIdx= stations.size()-1; currIdx>0; currIdx--) {
            int rndIdx = ThreadLocalRandom.current().nextInt(currIdx);
            Collections.swap(stations, rndIdx, currIdx);
        }
    }

    public Floor deepCopy() {
        ArrayList<Hole> holesCopy = holes.stream()
                .map(Hole::deepCopy)
                .collect(Collectors.toCollection(ArrayList::new));

        return new Floor(this.fitnessScore, this.numberOfHoles, this.sideLength, holesCopy);
    }
}
