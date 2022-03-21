package com;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

public class Operations {

    private static final Random random = Util.RANDOM;
    private static final GameField field = Util.FIELD;
    private static final int CHROMOSOME_LENGTH = Util.CHROMOSOME_LENGTH;
    private static final int POPULATION_SIZE = Util.POPULATION_SIZE;

    public static boolean[] mutate(boolean[] ant) {
        boolean[] mutated = invertBit(ant, random.nextInt(CHROMOSOME_LENGTH));
        return field.testAnt(mutated) > field.testAnt(ant)
            ? mutated : ant;
    }

    public static boolean[] invertBit(boolean[] chromosome, int ind) {
        boolean[] mutated = Arrays.copyOf(chromosome, chromosome.length);
        mutated[ind] = !mutated[ind];
        return mutated;
    }

    public static List<boolean[]> select(List<boolean[]> population) {
        return population.stream()
                         .map(chromosome -> Map.entry(chromosome, field.testAnt(chromosome)))
                         .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                         .map(Entry::getKey)
                         .limit(population.size() / 2)
                         .collect(Collectors.toList());
    }

    public static List<boolean[]> crossover(List<boolean[]> parents) {
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

        return nextGeneration.subList(0, POPULATION_SIZE);
    }

    private static boolean[] makeChild(boolean[] parent1, boolean[] parent2) {
        int quater = CHROMOSOME_LENGTH / 4;
        int half = CHROMOSOME_LENGTH / 2;

        boolean[] parent1Copy = Arrays.copyOf(parent1, parent1.length);
        boolean[] parent2Copy = Arrays.copyOf(parent2, parent2.length);

        System.arraycopy(parent2Copy, 0, parent1Copy, 0, quater);

        System.arraycopy(parent2Copy, half, parent1Copy, half, half + quater - half);

        return parent1Copy;
    }

}
