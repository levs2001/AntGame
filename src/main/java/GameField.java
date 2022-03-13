import java.io.*;

public class GameField {
    private static final String FILE_SPLITTER = ";";
    private static final String S_EMPTY = "0";
    private static final int MOVE_MAX_COUNT = 200;

    private final Cell[][] field = new Cell[32][32];

    /**
     * Заполняет поле, в котором будут тестироваться муравьи.
     *
     * @param filename имя файла, из которого считывается поле
     */
    public void fill(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineN = 0;
            while ((line = reader.readLine()) != null) {
                fillLine(lineN, line);
                lineN++;
            }
        }
    }

    /**
     * Эмулирует проход муравья по полю.
     *
     * @param ant - тестирующийся муравей
     * @return количество съеденных муравьем яблок за 200 ходов
     */
    public int testAnt(Ant ant) {
        int eattenAppleC = 0;
        Direction antDirection = Direction.RIGHT;
        int[] curAntPos = {0, 0};

        for (int moveN = 0; moveN < MOVE_MAX_COUNT; moveN++) {
            int[] forwardCellCoord = getForwardCellCoord(curAntPos, antDirection);
            Cell forwardCell = getForwardCell(forwardCellCoord);
            Ant.Action antAct = ant.getAction(forwardCell);

            if (antAct.equals(Ant.Action.MOVE_FORWARD) && isInField(forwardCellCoord)) {
                curAntPos = forwardCellCoord;
                if (field[curAntPos[0]][curAntPos[1]].equals(Cell.APPlE)) {
                    field[curAntPos[0]][curAntPos[1]] = Cell.EMPTY;
                    eattenAppleC++;
                }
            }

            antDirection = getNewDirection(antDirection, antAct);
        }

        return eattenAppleC;
    }

    /**
     * Вычисляет новое направление муравья, в зависимости от принятого им действия.
     *
     * @param oldDir - старое направление
     * @param action - действие муравья
     * @return новое направление
     */
    private Direction getNewDirection(Direction oldDir, Ant.Action action) {
        if (action.equals(Ant.Action.TURN_LEFT)) {
            switch (oldDir) {
                case UP:
                    return Direction.LEFT;
                case DOWN:
                    return Direction.RIGHT;
                case LEFT:
                    return Direction.DOWN;
                case RIGHT:
                    return Direction.UP;
            }
        } else if (action.equals(Ant.Action.TURN_RIGHT)) {
            switch (oldDir) {
                case DOWN:
                    return Direction.LEFT;
                case UP:
                    return Direction.RIGHT;
                case LEFT:
                    return Direction.UP;
                case RIGHT:
                    return Direction.DOWN;
            }
        }

        return oldDir;
    }

    /**
     * Заполняет линию поля.
     *
     * @param lineN - номер линии поля, которую надо заполнить
     * @param line  - строка с данными для линии
     */
    private void fillLine(int lineN, String line) {
        String[] strCells = line.split(FILE_SPLITTER);
        for (int j = 0; j < strCells.length; j++) {
            field[lineN][j] = strCells[j].equals(S_EMPTY) ? Cell.EMPTY : Cell.APPlE;
        }
    }

    private Cell getForwardCell(int[] forwardCellCoord) {
        if (isInField(forwardCellCoord)) {
            return field[forwardCellCoord[0]][forwardCellCoord[1]];
        }

        return Cell.EMPTY;
    }

    private boolean isInField(int[] cellCoord) {
        return cellCoord[0] < field.length && cellCoord[0] >= 0 && cellCoord[1] < field[0].length && cellCoord[1] >= 0;
    }

    private static int[] getForwardCellCoord(int[] curCellPos, Direction direction) {
        int[] move = direction.getMove();
        return new int[]{curCellPos[0] + move[0], curCellPos[1] + move[1]};
    }

    public enum Cell {
        EMPTY,
        APPlE
    }

    public enum Direction {
        RIGHT(0, 1),
        DOWN(1, 0),
        LEFT(0, -1),
        UP(-1, 0);

        private final int[] move;

        Direction(final int... move) {
            this.move = move;
        }

        public int[] getMove() {
            return move;
        }
    }
}
