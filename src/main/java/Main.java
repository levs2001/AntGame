import java.io.IOException;

public class Main {
    private static final String FIELD_FILENAME = "fieldFiles/field.csv";

    public static void main(String[] args) throws Exception {
        GameField field = new GameField();
        field.fill(FIELD_FILENAME);
        boolean[] chr = new boolean[83];

        Util.optimize(field);
    }
}
