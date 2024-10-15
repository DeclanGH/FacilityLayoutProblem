package io.github.declangh.facilitylayoutproblem.logic;

import io.github.declangh.facilitylayoutproblem.model.Floor;
import io.github.declangh.facilitylayoutproblem.view.Display;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class ParallelGeneticAlgorithm {

    private final Display snapShotsDisplay;
    public static final int NUMBER_OF_THREADS = 32;

    public ParallelGeneticAlgorithm() {
        snapShotsDisplay = new Display();
    }

    /**
     * Spawns 32 threads to run {@link FLPGeneticAlgorithm} in parallel. <br>
     * Stops when one thread converges or all threads have gotten to the maximum number of generations.
     */
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        Phaser phaser = new Phaser(NUMBER_OF_THREADS);
        Exchanger<Floor> exchanger = new Exchanger<>();

        /*FLPGeneticAlgorithm f = new FLPGeneticAlgorithm(("Thread-1"), phaser, exchanger, snapShotsDisplay);
        f.run();*/

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            executor.submit(new FLPGeneticAlgorithm(("Thread-"+ i), phaser, exchanger, snapShotsDisplay));
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(1000001, TimeUnit.MILLISECONDS)) {
                System.out.println("Thread pool didn't terminate properly");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        snapShotsDisplay.showSnapshots();
    }
}
