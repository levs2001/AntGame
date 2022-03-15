import java.time.LocalTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Util {

    public static int POPULATION_SIZE = 10_000;
    public static int ITERATIONS = 100;
    public static int CHROMOSOME_LENGTH = 83;

    public static void optimize(GameField field) {
        Random random = new Random(LocalTime.now().toSecondOfDay());
        List<boolean[]> population = new ArrayList<>();
        for (int i = 0; i < Util.POPULATION_SIZE; i++) {
            population.add(Util.randomBools(CHROMOSOME_LENGTH, random));
        }

//        List<boolean[]> bestHalf = Util.select(field, population);

//        System.out.println(field.testAnt(new Ant(bestHalf.get(0))));
//        System.out.println(bestHalf.size());
//        System.out.println(field.testAnt(new Ant(bestHalf.get(bestHalf.size() - 1))));


        for (int i = 0; i < Util.ITERATIONS; i++) {
            population = population.stream().map(ant -> {
                boolean[] mutated = Util.invertBit(ant, random.nextInt(CHROMOSOME_LENGTH));
                return field.testAnt(new Ant(mutated)) > field.testAnt(new Ant(ant))
                        ? mutated : ant;
            }).collect(Collectors.toList());
            population = Util.select(field, population);
            System.out.println("Best: " + field.testAnt(new Ant(population.get(0))));
            population = Util.crossover(population);
        }
    }

    public static boolean[] invertBit(boolean[] chromosome, int ind) {
        boolean[] mutated = Arrays.copyOf(chromosome, chromosome.length);
        mutated[ind] = !mutated[ind];
        return mutated;
    }

    public static List<boolean[]> select(GameField field, List<boolean[]> population) {
        return population.stream()
                .map(chromosome -> Map.entry(chromosome, field.testAnt(new Ant(chromosome))))
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

    public static boolean[] makeChild(boolean[] parent1, boolean[] parent2) {
        int quater = CHROMOSOME_LENGTH / 4;
        int half = CHROMOSOME_LENGTH / 2;

        boolean[] parent1Copy = Arrays.copyOf(parent1, parent1.length);
        boolean[] parent2Copy = Arrays.copyOf(parent2, parent2.length);

        System.arraycopy(parent2Copy, 0, parent1Copy, 0, quater);

        System.arraycopy(parent2Copy, half, parent1Copy, half, half + quater - half);

        return parent1Copy;
    }

    public static boolean[] randomBools(int len, Random random) {
        boolean[] arr = new boolean[len];
        for (int i = 0; i < len; i++) {
            arr[i] = random.nextBoolean();
        }
        return arr;
    }
}