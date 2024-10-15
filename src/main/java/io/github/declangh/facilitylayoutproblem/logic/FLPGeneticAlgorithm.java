package io.github.declangh.facilitylayoutproblem.logic;

import io.github.declangh.facilitylayoutproblem.model.RandomNumberBucket;
import io.github.declangh.facilitylayoutproblem.model.Floor;
import io.github.declangh.facilitylayoutproblem.model.Hole;
import io.github.declangh.facilitylayoutproblem.model.Station;
import io.github.declangh.facilitylayoutproblem.model.StationType;
import io.github.declangh.facilitylayoutproblem.view.Display;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FLPGeneticAlgorithm implements Runnable {

    public static final int MAX_GENERATIONS = 200;

    public static final int NUMBER_OF_MANUFACTURING_STATIONS = 8;//2;//8;
    public static final int NUMBER_OF_DISTRIBUTION_STATIONS = 10;//2;//10;
    public static final int NUMBER_OF_SECURITY_STATIONS = 14;//2;//14;
    public static final int NUMBER_OF_HEALTH_STATIONS = 2;
    public static final int NUMBER_OF_POWER_STATIONS = 2;
    public static final int NUMBER_OF_HOME_STATIONS = 16;//2;//16;
    public static final int NUMBER_OF_FOOD_STATIONS = 6;//2;//6;
    public static final int NUMBER_OF_FIRE_STATIONS = 2;

    private final int NUMBER_OF_STATIONS = NUMBER_OF_MANUFACTURING_STATIONS + NUMBER_OF_DISTRIBUTION_STATIONS
            + NUMBER_OF_SECURITY_STATIONS + NUMBER_OF_HEALTH_STATIONS + NUMBER_OF_POWER_STATIONS
            + NUMBER_OF_HOME_STATIONS + NUMBER_OF_FOOD_STATIONS + NUMBER_OF_FIRE_STATIONS;

    private final int POPULATION_SIZE = 10;//NUMBER_OF_STATIONS * 2;

    private final String threadName;
    private final Phaser phaser;
    private final Display display;
    private Floor currentFittestFloor;
    private Floor overallFittestFloor;
    private final Exchanger<Floor> exchanger;
    private final ArrayList<Floor> population;

    public FLPGeneticAlgorithm(String threadName, Phaser phaser, Exchanger<Floor> exchanger, Display display) {
        this.threadName = threadName;
        this.phaser = phaser;
        this.display = display;
        // length does not matter here. what matters is that a new Floor has a fitness score of Double.MIN_VALUE
        this.overallFittestFloor = new Floor(2);
        this.exchanger = exchanger;
        this.population = new ArrayList<>();
        createInitialPopulation();
    }

    private void createInitialPopulation() {
        List<Station> stations = new ArrayList<>();
        for (int i = 1; i <= NUMBER_OF_MANUFACTURING_STATIONS; i++) {
            stations.add(new Station(i, StationType.MANUFACTURING));
        }
        for (int i = 1; i <= NUMBER_OF_DISTRIBUTION_STATIONS; i++) {
            stations.add(new Station(i, StationType.DISTRIBUTION));
        }
        for (int i = 1; i <= NUMBER_OF_SECURITY_STATIONS; i++) {
            stations.add(new Station(i, StationType.SECURITY));
        }
        for (int i = 1; i <= NUMBER_OF_HEALTH_STATIONS; i++) {
            stations.add(new Station(i, StationType.HEALTH));
        }
        for (int i = 1; i <= NUMBER_OF_POWER_STATIONS; i++) {
            stations.add(new Station(i, StationType.POWER));
        }
        for (int i = 1; i <= NUMBER_OF_HOME_STATIONS; i++) {
            stations.add(new Station(i, StationType.HOME));
        }
        for (int i = 1; i <= NUMBER_OF_FOOD_STATIONS; i++) {
            stations.add(new Station(i, StationType.FOOD));
        }
        for (int i = 1; i <= NUMBER_OF_FIRE_STATIONS; i++) {
            stations.add(new Station(i, StationType.FIRE));
        }

        for (int i = 1; i <= POPULATION_SIZE; i++) {
            population.add(new Floor(stations));
        }
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        int generation = 0;

        while (generation < MAX_GENERATIONS) {
            performEvolutionarySteps();

            display.submitFloor(currentFittestFloor.deepCopy(), this.threadName, generation);

            if (generation % 10 == 0 && generation != 0) {
                phaser.arriveAndAwaitAdvance();
                exchangePopulation();
            }
            generation++;
        }

        phaser.arriveAndDeregister();
    }

    private void performEvolutionarySteps(){
        // Step 1: Find the fitness score for each member of the population
        calculateAndSetFitnessScore();

        // Step 2: Select the parents that will give rise to the next population
        ArrayList<Floor> parents = selectParentsFromPopulation();

        // Step 3: This is where mating happens and parents pass genes to their kids (new population)
        crossover(parents);

        /* Step 4: After crossover/mating, the new population (which are the kids) needs to be mutated to avoid
        getting stuck in a global minima/maxima */
        mutate(population, 0.02);
    }

    private void calculateAndSetFitnessScore() {
        for (Floor floor : population) {
            double fitnessScore = 0;
            ArrayList<Hole> holes = floor.getHoles();
            for (int i=0; i<holes.size(); i++) {
                Hole hole1 = holes.get(i);
                if (!hole1.isOccupied()) continue;
                for (int j=i+1; j<holes.size(); j++) {
                    Hole hole2 = holes.get(j);
                    if (!hole2.isOccupied()) continue;
                    Station station1 = hole1.getStation();
                    Station station2 = hole2.getStation();
                    int affinityBetweenStations = station1.affinityWith(station2);
                    fitnessScore += affinityBetweenStations/euclideanDistanceBetween(hole1,hole2);
                }
            }
            floor.setFitnessScore(fitnessScore);
        }
        // sort by ascending order of fitness scores
        population.sort(Comparator.comparingDouble(Floor::getFitnessScore));
        currentFittestFloor = population.getLast();
        overallFittestFloor = (overallFittestFloor.getFitnessScore() > currentFittestFloor.getFitnessScore())
                ? overallFittestFloor : currentFittestFloor.deepCopy();
    }

    private @NotNull ArrayList<Floor> selectParentsFromPopulation() {
        double probabilityOfSelectingHighFitnessFloors = 0.7;
        double probabilityOfSelectingLowFitnessFloors = 1 - probabilityOfSelectingHighFitnessFloors;

        int numberOfParentsToSelect = population.size()/2;
        ArrayList<Floor> parents = new ArrayList<>(numberOfParentsToSelect);

        int topSelectionBoundary = (int) (probabilityOfSelectingHighFitnessFloors * numberOfParentsToSelect);

        // first pick the top N fittest members of the population that must be involved in crossover
        for (int i=0; i<topSelectionBoundary; i++) {
            parents.add(population.getLast());
            population.removeLast();
        }

        int bottomSelectionBoundary = (int) (probabilityOfSelectingLowFitnessFloors * numberOfParentsToSelect);

        RandomNumberBucket randomNumberBucket = new RandomNumberBucket(population.size());

        // then randomly select from the remaining "weak" members
        for (int i=0; i<bottomSelectionBoundary; i++) {
            parents.add(population.get(randomNumberBucket.pickOutOne()));
        }

        return parents;
    }

    private void crossover(@NotNull ArrayList<Floor> parents) {
        if (parents.size() < 2) {
            throw new RuntimeException("Cannot perform Crossover with less than two parents");
        }

        population.clear();

        RandomNumberBucket randomNumberBucket = new RandomNumberBucket(parents.size());

        while (population.size() < POPULATION_SIZE) {
            // This could bring a possibility for asexual mating if parent size is odd
            // (Chances are low, but, the bigger parent size the lesser the chances of asexual mating)
            // I don't mind it since mutation is bound to happen to every child
            Floor parent1 = parents.get(randomNumberBucket.pickOutOne());
            Floor parent2 = parents.get(randomNumberBucket.pickOutOne());

            Floor child = mate(parent1, parent2);
            population.add(child);
        }
    }

    private @NotNull Floor mate(@NotNull Floor parent1, @NotNull Floor parent2) {
        if (parent1.getSideLength() != parent2.getSideLength()) {
            throw new RuntimeException("Cannot mate parents of different side lengths");
        }

        int sideLength = parent1.getSideLength();
        Floor child = new Floor(sideLength);

        ArrayList<Integer> childAvailableGeneSpace = new ArrayList<>(child.getNumberOfHoles());

        // make a gene pool
        HashMap<String, Station> genePool = new HashMap<>();
        for (int i=0; i< parent1.getNumberOfHoles(); i++) {
            Hole hole = parent1.getHoles().get(i);
            if (hole.isOccupied()) {
                genePool.put(hole.getStation().geneName(), hole.getStation().deepCopy());
            }
        }

        // first pass
        for (int i=0; i < child.getNumberOfHoles(); i++) {
            // a gene space is a space you can place a gene(station)
            Hole parentGeneSpace1 = parent1.getHoles().get(i);
            Hole parentGeneSpace2 = parent2.getHoles().get(i);
            Hole childGeneSpace = child.getHoles().get(i);

            if (parentGeneSpace1.isOccupied() && parentGeneSpace2.isOccupied()) {
                Station parentGene1 = parentGeneSpace1.getStation();
                Station parentGene2 = parentGeneSpace2.getStation();
                if (genePool.containsKey(parentGene1.geneName()) && genePool.containsKey(parentGene2.geneName())) {
                    int rndNum = ThreadLocalRandom.current().nextInt(10001);
                    if (rndNum % 2 == 0) {
                        childGeneSpace.placeStation(parentGene1.deepCopy());
                        genePool.remove(parentGene1.geneName());
                    } else {
                        childGeneSpace.placeStation(parentGene2.deepCopy());
                        genePool.remove(parentGene2.geneName());
                    }
                } else if (genePool.containsKey(parentGene1.geneName())) {
                    childGeneSpace.placeStation(parentGene1.deepCopy());
                    genePool.remove(parentGene1.geneName());
                } else if (genePool.containsKey(parentGene2.geneName())) {
                    childGeneSpace.placeStation(parentGene2.deepCopy());
                    genePool.remove(parentGene2.geneName());
                } else {
                    childAvailableGeneSpace.add(i);
                }
            } else if (parentGeneSpace1.isOccupied()) {
                if (genePool.containsKey(parentGeneSpace1.getStation().geneName())) {
                    childGeneSpace.placeStation(parentGeneSpace1.getStation().deepCopy());
                    genePool.remove(parentGeneSpace1.getStation().geneName());
                } else {
                    childAvailableGeneSpace.add(i);
                }
            } else if (parentGeneSpace2.isOccupied()) {
                if (genePool.containsKey(parentGeneSpace2.getStation().geneName())) {
                    childGeneSpace.placeStation(parentGeneSpace2.getStation().deepCopy());
                    genePool.remove(parentGeneSpace2.getStation().geneName());
                } else {
                    childAvailableGeneSpace.add(i);
                }
            } else {
                childAvailableGeneSpace.add(i);
            }
        }

        // second pass for unused genes
        for (String geneName : genePool.keySet()) {
            int rndIdx = ThreadLocalRandom.current().nextInt(childAvailableGeneSpace.size());
            int rndUnusedHoleIdx = childAvailableGeneSpace.get(rndIdx);
            child.getHoles().get(rndUnusedHoleIdx).placeStation(genePool.get(geneName));
            childAvailableGeneSpace.remove(rndIdx);
        }

        return child;
    }

    private void mutate(@NotNull ArrayList<Floor> children, double mutationRate) {
        for (Floor child : children) {
            int numGenesToMutate = (int) (child.getNumberOfHoles() * mutationRate);
            RandomNumberBucket randomNumberBucket = new RandomNumberBucket(child.getNumberOfHoles());

            for (int i=0; i< numGenesToMutate;) {
                int randomIndex1 = randomNumberBucket.pickOutOne();
                int randomIndex2 = randomNumberBucket.pickOutOne();

                Hole hole1 = child.getHoles().get(randomIndex1);
                Hole hole2 = child.getHoles().get(randomIndex2);

                if (randomIndex1 != randomIndex2) {
                    if (hole1.isOccupied() && hole2.isOccupied()) {
                        Station station1 = hole1.removeStation();
                        Station station2 = hole2.removeStation();
                        hole1.placeStation(station2);
                        hole2.placeStation(station1);
                    } else if (hole1.isOccupied() && !hole2.isOccupied()) {
                        Station station1 = hole1.removeStation();
                        hole2.placeStation(station1);
                    } else if (hole2.isOccupied() && !hole1.isOccupied()) {
                        Station station2 = hole2.removeStation();
                        hole1.placeStation(station2);
                    } else {
                        continue;
                    }
                    i++;
                }
            }
        }
    }

    private double euclideanDistanceBetween(@NotNull final Hole hole1, @NotNull final Hole hole2) {
        int x1 = hole1.getXCoordinate();
        int y1 = hole1.getYCoordinate();
        int x2 = hole2.getXCoordinate();
        int y2 = hole2.getYCoordinate();
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private double manhattanDistanceBetween(@NotNull final Hole hole1, @NotNull final Hole hole2) {
        int x1 = hole1.getXCoordinate();
        int y1 = hole1.getYCoordinate();
        int x2 = hole2.getXCoordinate();
        int y2 = hole2.getYCoordinate();
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private void exchangePopulation() {
        try {
            int randomIndex = ThreadLocalRandom.current().nextInt(this.population.size());
            Floor thisThreadsFloor = this.population.get(randomIndex);
            Floor otherThreadsFloor = exchanger.exchange(thisThreadsFloor.deepCopy(), 5000, TimeUnit.MILLISECONDS);
            this.population.set(randomIndex, otherThreadsFloor);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Exchange operation was interrupted", e);
        } catch (TimeoutException e) {
            throw new RuntimeException("Wait for exchange operation timed out", e);
        }
    }
}
