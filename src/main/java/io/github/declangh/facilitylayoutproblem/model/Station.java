package io.github.declangh.facilitylayoutproblem.model;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

import static io.github.declangh.facilitylayoutproblem.configuration.StationConfig.affinityTable;

public class Station {

    private final int stationId;
    private final StationType stationType;
    private final String geneName;

    public Station(int stationId, @NotNull StationType stationType) {
        this.stationType = stationType;
        this.stationId = stationId;
        this.geneName = this.stationType.name() + "_" + stationId;
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

    /**
     * <p>This is a unique name that represents a gene type.<p/>
     * <p>It is way to identify genes from two parents that are completely similar by features (e.g)but are
     * different by reference since the objects are not the same in memory.<p/>
     * <p>Example: Station one creates {@code  new Station(1, HOME)}</p>
     * @return String formed from {@code stationType + _ + stationId}
     */
    public String geneName() {
        return geneName;
    }

    public String getIdAsString() {
        return String.valueOf(stationId);
    }

    public Color getColor() {
        return stationType.getColor();
    }

    /**
     * Get the affinity between the current instance of a station and another station.
     * @param anotherStation some station (can be the current station too)
     * @return an integer value where negative int represents a negative affinity, zero represents
     * indifference and positive int represents a positive affinity.
     */
    public int affinityWith(@NotNull Station anotherStation) {
        int thisStationTypeOrdinal = this.stationType.ordinal();
        int anotherStationTypeOrdinal = anotherStation.getType().ordinal();

        return affinityTable[thisStationTypeOrdinal][anotherStationTypeOrdinal];
    }
}
