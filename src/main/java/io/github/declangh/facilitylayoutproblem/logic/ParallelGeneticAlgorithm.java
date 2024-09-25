package io.github.declangh.facilitylayoutproblem.logic;

import io.github.declangh.facilitylayoutproblem.view.Display;

public class ParallelGeneticAlgorithm implements Runnable {

    public static final int MAX_GENERATIONS = 1000;

    public static final int NUMBER_OF_THREADS = 2;

    public static final int NUMBER_OF_MANUFACTURING_STATIONS = 10;
    public static final int NUMBER_OF_DISTRIBUTION_STATIONS = 10;
    public static final int NUMBER_OF_SECURITY_STATIONS = 10;
    public static final int NUMBER_OF_HEALTH_STATIONS = 10;
    public static final int NUMBER_OF_POWER_STATIONS = 10;
    public static final int NUMBER_OF_FIRE_STATIONS = 10;

    private final int NUMBER_OF_STATIONS = NUMBER_OF_MANUFACTURING_STATIONS + NUMBER_OF_DISTRIBUTION_STATIONS
            + NUMBER_OF_SECURITY_STATIONS + NUMBER_OF_HEALTH_STATIONS
            + NUMBER_OF_POWER_STATIONS + NUMBER_OF_FIRE_STATIONS;

    public ParallelGeneticAlgorithm() {
        createInitialPopulation();
    }

    private void createInitialPopulation() {

    }

    @Override
    public void run() {
        Display display = new Display();
        display.turnOn();
        // selection

        // cross-over

        // mutation

    }

    private void startParallelGeneticAlgorithm() {
        Display display = new Display();
        display.turnOn();
    }
}
