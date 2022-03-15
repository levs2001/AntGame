import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

public class Util {

    public static int POPULATION_SIZE = 10_000;
    public static int ITERATIONS = 100;
    public static int CHROMOSOME_LENGTH = 83;

    public static void optimize(GameField field) {
        Random random = new Random();
        List<boolean[]> population = new ArrayList<>();
        for (int i = 0; i < Util.POPULATION_SIZE; i++) {
            population.add(Util.randomBools(CHROMOSOME_LENGTH));
        }

        System.out.println(Util.getScore(field, Util.select(field, population).get(0)));
//        for (int i = 0; i < Util.ITERATIONS; i++) {
//
//            population = population.stream().map(ant -> {
//                var mutated = Util.invertBit(ant, random.nextInt(CHROMOSOME_LENGTH));
//                return Util.getScore(field, mutated) > Util.getScore(field, ant) ? mutated : ant;
//            }).collect(Collectors.toList());
//            population = Util.select(field, population);
//            System.out.println("Best: " + Util.getScore(field, population.get(0)));
//            population = Util.crossover(population);
//        }
    }

    public static boolean[] invertBit(boolean[] chromosome,int ind) {
        chromosome[ind] = !chromosome[ind];
        return chromosome;
    }

    public static List<boolean[]> select(GameField field, List<boolean[]> population) {

        return population.stream()
                         .map(chromosome -> {
                             int score = 0;
                             try {
                                 score = field.testAnt(new Ant(chromosome));
                             } catch (Exception e) {
                                 throw new RuntimeException(e);
                             }
                             return Map.entry(chromosome, score);
                         })
                         .sorted((e1, e2) -> (e1.getValue() - e2.getValue()))
                         .map(Entry::getKey)
//                         .limit(population.size() / 2)
                         .collect(Collectors.toList());
    }

    public static List<boolean[]> crossover(List<boolean[]> parents) {
        List<boolean[]> nextGeneration = new ArrayList<>();
        int ind = 0;
        if (parents.size() % 2 == 1) {
            nextGeneration.add(parents.get(0));
            ind++;
        }
        for (int i = ind; i < parents.size(); i+=2) {
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
        int firstSectionStart= CHROMOSOME_LENGTH / 4;
        int secondSectionStart = CHROMOSOME_LENGTH / 2;
        for (int i = 0; i < firstSectionStart; i++) {
            parent1[i] = parent2[i];
        }

        for (int i = secondSectionStart; i < secondSectionStart + 21; i++) {
            parent1[i] = parent2[i];
        }
        return parent1;
    }

    public static int getScore(GameField field, boolean[] chromosome) {
        try {
            return field.testAnt(new Ant(chromosome));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean[] randomBools(int len) {
        boolean[] arr = new boolean[len];
        Random random = new Random();
        for(int i = 0; i < len; i++) {
            arr[i] = random.nextBoolean();
        }
        return arr;
    }
}