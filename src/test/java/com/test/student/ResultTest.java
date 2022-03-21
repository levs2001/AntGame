package com.test.student;

import com.game.GameField;
import com.play.Optimizer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class ResultTest {
    private static final String FIELD_FILENAME = "fieldFiles/field.csv";
    private static final int TIMEOUT_SECONDS = 50;

    @Test
    public void testResult() throws IOException {
        GameField field = new GameField();
        field.fill(FIELD_FILENAME);
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT_SECONDS), () -> {
                    boolean[] bestChromosome = Optimizer.optimize(field);
                    System.out.println("Your result is " + field.testAnt(bestChromosome));
                    System.out.println("Max is 89");
                }
        );
    }
}
