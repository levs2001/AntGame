import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private static final String FIELD_FILENAME = "fieldFiles/field.csv";
    private static final int IDEAL_APPLE_COUNT = 89;
    private static final boolean[] IDEAL_CHROMOSOME = {
            false, false, false, // 000 - начальное состояние
            false, false, true, false, true, false, true, false, false, false, // 001 01    010 00
            false, true, true, false, true, true, false, false, false, false, // 011 01     100 00
            true, false, true, false, false, false, false, true, false, false, // 101 00    001 00
            true, false, true, true, false, false, true, false, false, false, // 101 10     010 00
            true, false, true, false, false, true, true, false, false, false, // 101 00     110 00
            true, true, true, false, false, true, true, true, false, false, // 111 00       111 00
            false, false, false, true, false, false, false, true, false, false, // 000 10   001 00
            false, false, false, true, false, true, true, false, false, false // 000 10     110 00
    };

    private static final boolean[] BAD_CHROMOSOME = {true, false};

    @BeforeAll
    public static void setup() {
    }

    @Test
    public void testAnt() throws Exception {
        Throwable thrown = assertThrows(Exception.class, () -> {
            new Ant(BAD_CHROMOSOME);
        });
        assertNotNull(thrown.getMessage());

        Ant ant = new Ant(IDEAL_CHROMOSOME);
    }

    @Test
    public void testFieldTestAnt() throws Exception {
        GameField field = new GameField();
        field.fill(FIELD_FILENAME);

        Ant ant = new Ant(IDEAL_CHROMOSOME);
        Assertions.assertEquals(field.testAnt(ant), IDEAL_APPLE_COUNT);
        Assertions.assertEquals(field.testAnt(ant), IDEAL_APPLE_COUNT);
    }
}