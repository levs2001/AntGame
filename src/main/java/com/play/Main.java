package com.play;

import com.game.GameField;

import java.io.IOException;

public class Main {
    private static final String FIELD_FILENAME = "fieldFiles/field.csv";

    public static void main(String[] args) throws IOException {
        GameField field = new GameField();
        field.fill(FIELD_FILENAME);

        boolean[] bestChromosome = Optimizer.optimize(field);
        System.out.println("Your result is " + field.testAnt(bestChromosome));
        System.out.println("Max is 89");
    }
}
