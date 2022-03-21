package com;

import java.util.*;
import java.util.stream.Collectors;

public class Util {

    public static int POPULATION_SIZE = 5_000;
    public static int ITERATIONS = 100;
    public static int CHROMOSOME_LENGTH = 83;
    public static int SEED = 1488;
    public static final Random RANDOM = new Random(SEED);
    public static GameField FIELD;

    public static void optimize(GameField field) {
        if (Objects.isNull(Util.FIELD)) {
            Util.FIELD = field;
        }

        List<boolean[]> population = new ArrayList<>();
        for (int i = 0; i < Util.POPULATION_SIZE; i++) {
            population.add(Util.randomBools(CHROMOSOME_LENGTH, RANDOM));
        }

        for (int i = 0; i < Util.ITERATIONS; i++) {
            population = population.stream()
                                   .map(Operations::mutate)
                                   .map(Operations::mutate)
                                   .map(Operations::mutate)
                                   .collect(Collectors.toList());
            population = Operations.select(population);
            System.out.println("Best: " + field.testAnt(population.get(0)));
            population = Operations.crossover(population);
        }
    }

    public static boolean[] randomBools(int len, Random random) {
        boolean[] arr = new boolean[len];
        for (int i = 0; i < len; i++) {
            arr[i] = random.nextBoolean();
        }
        return arr;
    }
}