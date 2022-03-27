package com.play;

import com.game.GameField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

public class Evolution {
    private final Random random;
    private final GameField field;
    private final int chromosomeL;
    private final int populationS;

    public Evolution(Random random, GameField field, int chromosomeL, int populationS) {
        this.random = random;
        this.field = field;
        this.chromosomeL = chromosomeL;
        this.populationS = populationS;
    }

    /**
     * Меняет бит в хромосоме муравья, если муравей стал лучше возвращает измененную хромосому.
     *
     * @return измененную хромосому, либо прежнюю, если муравей стал хуже
     */
    public boolean[] mutate(boolean[] chromosome) {
        boolean[] mutated = invertBit(chromosome, random.nextInt(chromosomeL));
        return field.testAnt(mutated) > field.testAnt(chromosome)
                ? mutated : chromosome;
    }

    /**
     * Меняет бит в хромосоме муравья.
     *
     * @param ind индекс, где надо изменить бит
     * @return измененную хромосому
     */
    public boolean[] invertBit(boolean[] chromosome, int ind) {
        boolean[] mutated = Arrays.copyOf(chromosome, chromosome.length);
        mutated[ind] = !mutated[ind];
        return mutated;
    }

    /**
     * Выбирает лучшую половину популяции.
     * Лучшая половина сортируется по убыванию (в начале списка лучший муравей)
     *
     * @return отсортированный по убыванию список с лучшей половиной
     */
    public List<boolean[]> select(List<boolean[]> population) {
        return population.stream()
                .map(chromosome -> Map.entry(chromosome, field.testAnt(chromosome)))
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .map(Entry::getKey)
                .limit(population.size() / 2)
                .collect(Collectors.toList());
    }

    /**
     * Увеличивает популяцию в 2 раза, рождая дочерние хромосомы.
     * В этот метод рекомендуется передавать лучшую половину прошлой популяции.
     *
     * @param parents родительские хромосомы
     * @return увеличенную в 2 раза популяцию, которая содержит всех родителей и новых детей (хромосомы)
     */
    public List<boolean[]> crossover(List<boolean[]> parents) {
        List<boolean[]> nextGeneration = new ArrayList<>();
        int ind = 0;
        if (parents.size() % 2 == 1) {
            nextGeneration.add(Arrays.copyOf(parents.get(0), parents.get(0).length));
            ind++;
        }
        for (int i = ind; i < parents.size(); i += 2) {
            boolean[] child1 = makeChild(parents.get(i), parents.get(i + 1));
            boolean[] child2 = makeChild(parents.get(i + 1), parents.get(i));
            nextGeneration.add(child1);
            nextGeneration.add(child2);
            nextGeneration.add(parents.get(i));
            nextGeneration.add(parents.get(i + 1));
        }

        return nextGeneration.subList(0, populationS);
    }

    /**
     * Скрещивает биты двух родителей.
     *
     * @return скрещенную из 2х родителей хромосому
     */
    private boolean[] makeChild(boolean[] parent1, boolean[] parent2) {
        int quater = chromosomeL / 4;
        int half = chromosomeL / 2;

        boolean[] parent1Copy = Arrays.copyOf(parent1, parent1.length);
        boolean[] parent2Copy = Arrays.copyOf(parent2, parent2.length);

        System.arraycopy(parent2Copy, 0, parent1Copy, 0, quater);

        System.arraycopy(parent2Copy, half, parent1Copy, half, half + quater - half);

        return parent1Copy;
    }

    /**
     * Создает случайную хромосому для муравья.
     *
     * @return хромосому
     */
    public boolean[] randomChromosome() {
        boolean[] arr = new boolean[chromosomeL];
        for (int i = 0; i < chromosomeL; i++) {
            arr[i] = random.nextBoolean();
        }
        return arr;
    }
}
