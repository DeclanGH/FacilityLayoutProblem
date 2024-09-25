package io.github.declangh.facilitylayoutproblem;

import io.github.declangh.facilitylayoutproblem.logic.ParallelGeneticAlgorithm;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to Facility Layout Problem!\n");

        ParallelGeneticAlgorithm parallelGeneticAlgorithm = new ParallelGeneticAlgorithm();
        parallelGeneticAlgorithm.run();
    }
}
