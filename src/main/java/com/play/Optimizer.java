package com.play;

import com.game.GameField;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Optimizer {
    // Размер популяции можно менять
    private static final int POPULATION_SIZE = 5_000;
    // Количество итераций можно менять, но проверьте, что вы проходите по времени, запустив тест
    private static final int ITERATIONS = 100;

    // Не меняйте эти константы
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
            // Возможный вариант реализации:
            // Проведите несколько мутаций с каждой особью
            // Сделайте селекцию
            // Сделайте кроссовер
        }

        population = evolution.select(population);
        // select оставляет половину лучших и сортирует по убыванию, поэтому лучшая будет в начале
        return population.get(0);
    }
}