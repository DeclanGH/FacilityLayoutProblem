package io.github.declangh.facilitylayoutproblem.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A square ground with a list of {@link Hole} that can be occupied by a {@link Station}
 */
public class Floor {

    private final int numberOfSpots;
    private final int numberOfHoles;
    private List<List<Hole>> holes;

    /**
     * @param stations List of stations to be put on the floor
     */
    public Floor(@NotNull final List<Station> stations) {
        this.numberOfSpots = stations.size();

        /* Since Floor is a square ground, we want holes to be a square value. To achieve this,
           holes must be the next square number when spots is multiplied by two */
        int rootNumber = ((int) Math.sqrt(numberOfSpots *2)) + 1;
        this.numberOfHoles = rootNumber*rootNumber;
        this.holes = new ArrayList<>(rootNumber);

        populateFloor(stations, rootNumber);
    }

    private void populateFloor(List<Station> stations, int rootNumber) {
        // make a deep copy
        List<Station> stationsCopy = stations.stream()
                .map(Station::new)
                .collect(Collectors.toCollection(ArrayList::new));

        for (int i=stationsCopy.size(); i<numberOfHoles; i++) {
            stationsCopy.add(null);
        }
        Collections.shuffle(stationsCopy);

        int index = 0;
        for (int i = 0; i < rootNumber; i++) {
            List<Hole> row = new ArrayList<>();
            for (int j = 0; j < rootNumber; j++) {
                row.add(new Hole(i, j, stationsCopy.get(index++)));
            }
            holes.add(row);
        }
    }

    public int getNumberOfSpots() {
        return numberOfSpots;
    }

    public int getNumberOfHoles() {
        return numberOfHoles;
    }
}
