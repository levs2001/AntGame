package com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    private static final String FIELD_FILENAME = "fieldFiles/field.csv";

    public static void main(String[] args) throws IOException {
        GameField field = new GameField();
        field.fill(FIELD_FILENAME);
        Util.optimize(field);
    }
}
