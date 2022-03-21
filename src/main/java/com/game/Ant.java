package com.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Ant {
    // Количество состояний фиксированное
    private static final int STATES_COUNT = 8;

    // количество возможных входов - яблоко или нет
    // биты на начальное состояние + количество состояний * ((биты на новое состояние + биты на действие) * количество возможных входов)
    // 3 + 8 * ((3 + 2) * 2) = 83
    private static final int CHROMOSOME_LENGTH = 83;

    private static final int BITS_IN_STATE_C = 3;
    private static final int BITS_IN_ACTION_C = 2;

    // Интовый массив, задающий хромосому муравья
    // [Номер начального состояния]
    // для 0го сотояния:
    //      [номер нового состояния 0-количество состояний, когда впереди нет яблока][номер действия 0-3, когда впереди есть яблоко]
    //          [номер нового состояния, когда впереди есть яблоко][номер действия, когда впереди есть яблоко]...
    private final boolean[] chromosome;

    private final State[] states = new State[STATES_COUNT];
    private State curState;
    private int chromosomePos = 0;

    public Ant(boolean[] chromosome) {
        if (chromosome.length != CHROMOSOME_LENGTH) {
            throw new RuntimeException("Incorrect chromosome length!");
        }

        this.chromosome = chromosome;

        // На нулевой позиции хромосомы номер начального состояния, поэтому не учитываем
        // Сместились к первому стейту
        chromosomePos = BITS_IN_STATE_C;
        for (int i = 0; i < states.length; i++) {
            states[i] = new State(i);
            readReaction(states[i], GameField.Cell.EMPTY);
            readReaction(states[i], GameField.Cell.APPlE);
        }

        chromosomePos = 0;
        curState = states[castToStateNum(chromosome, chromosomePos)];
    }


    public void mutate(int dnaInd, boolean changedDna) {
        chromosome[dnaInd] = changedDna;
    }

    public boolean[] getChromosome() {
        return chromosome;
    }

    public boolean[] getChromosomeCopy() {
        return Arrays.copyOf(chromosome, chromosome.length);
    }

    /**
     * Переводит конечный автомат в следующее состояние, в зависимости от входного действия и отдает действие,
     * которое предпринял муравей.
     *
     * @param inAct входное действие (то, что находится перед муравьем)
     * @return действие, которое предпринял муравей
     */
    public Action getAction(GameField.Cell inAct) {
        Action nextAction = curState.getNextAction(inAct);
        curState = states[curState.getNextStateNum(inAct)];
        return nextAction;
    }

    /**
     * Читает реакцию на клетку спереди из битового массива.
     * Новое состояние и действие, если впереди такая клетка
     *
     * @param state       - состояние автомата, для которого читаем реакцию
     * @param forwardCell - клетка спереди
     */
    private void readReaction(State state, GameField.Cell forwardCell) {
        state.addNextState(forwardCell, castToStateNum(chromosome, chromosomePos));
        chromosomePos += BITS_IN_STATE_C;
        state.addAction(forwardCell, castToAction(chromosome, chromosomePos));
        chromosomePos += BITS_IN_ACTION_C;
    }

    /**
     * Переводит первые 2 бита массива, начиная от pos, в действие.
     * Действий всего 4, поэтому 2 битов достаточно
     *
     * @param bitCode - двоичное число (массив бит)
     * @return действие
     */
    private static Action castToAction(boolean[] bitCode, int pos) {
        return !bitCode[pos] && !bitCode[pos + 1] ? Action.MOVE_FORWARD : !bitCode[pos] ? Action.TURN_LEFT : !bitCode[pos + 1] ? Action.TURN_RIGHT : Action.NO;
    }

    /**
     * Переводит первые 3 бита массива, начиная от pos, в интовое число.
     *
     * @param bitCode - двоичное число (массив бит)
     * @return номер состояния
     */
    private static int castToStateNum(boolean[] bitCode, int pos) {
        int res = 0;
        if (bitCode[pos]) {
            res += 4;
        }
        if (bitCode[pos + 1]) {
            res += 2;
        }
        if (bitCode[pos + 2]) {
            res += 1;
        }

        return res;
    }

    /**
     * Действия муравья.
     */
    public enum Action {
        MOVE_FORWARD,
        TURN_LEFT,
        TURN_RIGHT,
        NO
    }

    private static class State {
        private final int num;
        private final Map<GameField.Cell, Integer> nextStateNum = new HashMap<>();
        private final Map<GameField.Cell, Action> nextAction = new HashMap<>();

        State(int num) {
            this.num = num;
        }

        public void addNextState(GameField.Cell forwCell, int nextStateNum) {
            this.nextStateNum.put(forwCell, nextStateNum);
        }

        public void addAction(GameField.Cell forwCell, Action action) {
            nextAction.put(forwCell, action);
        }


        public int getNextStateNum(GameField.Cell forwardCell) {
            return nextStateNum.get(forwardCell);
        }

        public Action getNextAction(GameField.Cell forwardCell) {
            return nextAction.get(forwardCell);
        }

        public int getNum() {
            return num;
        }
    }

}
