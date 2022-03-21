package com.play;

import com.game.GameField;

import java.util.*;
import java.util.stream.Collectors;

public class Optimizer {

    private static final int POPULATION_SIZE = 5_000;
    private static final int ITERATIONS = 100;
    private static final int CHROMOSOME_LENGTH = 83;
    private static final int SEED = 1488;
    private static final Random RANDOM = new Random(SEED);

    /**
     * Создает лучшую битовую строку (хромосому), для прохода по лабиринту.
     *
     * @param field поле для испытания муравьев (их хромосом)
     * @return лучшая строка, которая у вас получилась в процессе оптимизации
     */
    public static boolean[] optimize(GameField field) {
        Evolution evolution = new Evolution(RANDOM, field, CHROMOSOME_LENGTH, POPULATION_SIZE);

        List<boolean[]> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(evolution.randomChromosome());
        }

        for (int i = 0; i < ITERATIONS; i++) {
            population = population.stream()
                    .map(evolution::mutate)
                    .map(evolution::mutate)
                    .map(evolution::mutate)
                    .collect(Collectors.toList());
            population = evolution.select(population);
            System.out.println("Best: " + field.testAnt(population.get(0)));
            population = evolution.crossover(population);
        }

        population = evolution.select(population);
        return population.get(0);
    }
}