/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.glife;

import com.limelion.glife.output.OutputAdapter;
import com.limelion.glife.universe.Universe2D;
import com.limelion.glife.utils.Utils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

/**
 * Represent a party of game of life, with its own universe and rules.
 */
public class GameOfLife {

    // BLACK OVER WHITE
    public static final byte ALIVE = 0;
    public static final byte DEAD = (byte) 0xFF;

    private Universe2D universe;
    private Statistics stats;
    private StateInfo sinf;

    /**
     * Create a new GameOfLife with a default universe of 100*100 and the Conway's rule. The universe borders are not
     * bounded.
     */
    public GameOfLife() {

        this(100, Rule.Square2D.CONWAY);
    }

    /**
     * Create a new GameOfLife with a square universe and the specified rule.
     *
     * @param square the square parameter
     * @param rule   the rule to use
     */
    public GameOfLife(int square, Rule rule) {

        this(square, square, rule);
    }

    /**
     * Create a new GameOfLife with a square universe and the specified rule.
     *
     * @param square the square parameter
     * @param rule   the rule to use
     * @param bound  specify if the universe borders should be bounded
     */
    public GameOfLife(int square, Rule rule, boolean bound) {

        this(square, square, rule, bound);
    }

    /**
     * Create a new GameOfLife with a rectangular universe and the specified rule.
     *
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @param rule   the rule to use
     */
    public GameOfLife(int width, int height, Rule rule) {

        this(width, height, rule, false);
    }

    /**
     * Create a new GameOfLife with a rectangular universe and the specified rule.
     *
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @param rule   the rule to use
     * @param bound  specify if the universe borders should be bound
     */
    public GameOfLife(int width, int height, Rule rule, boolean bound) {

        this(Utils.fill(new byte[width * height], DEAD), width, height, rule, bound);
    }

    /**
     * Create a new Game of Life from a base board. The universe borders are not bounded.
     *
     * @param boardData the initial data to use
     * @param width     the width of the universe
     * @param height    the height of the universe
     * @param rule      the rule to use
     */
    public GameOfLife(byte[] boardData, int width, int height, Rule rule) {

        this(boardData, width, height, rule, false);
    }

    /**
     * Create a new Game of Life from a base board.
     *
     * @param boardData the initial data to use
     * @param width     the width of the universe
     * @param height    the height of the universe
     * @param rule      the rule to use
     * @param bound     specify if the universe borders should be bound
     */
    public GameOfLife(byte[] boardData, int width, int height, Rule rule, boolean bound) {

        this.universe = new Universe2D(boardData, width, height, rule, bound);
        this.stats = new Statistics();
        this.sinf = new StateInfo().setWidth(universe.getWidth()).setHeight(universe.getHeight()).setRule(rule.toString()).setBound(bound);
    }

    /**
     * Create a new Game of Life from a parsed GLD file.
     *
     * @param gld the parsed GLD file
     */
    public GameOfLife(Goldata gld) {

        this.universe = new Universe2D(
            gld.getData(),
            gld.getStateInfo().getWidth(),
            gld.getStateInfo().getHeight(),
            Rule.parse(gld.getStateInfo().getRule()),
            gld.getStateInfo().isBound());

        this.stats = new Statistics();
        this.sinf = gld.getStateInfo();
    }

    /**
     * @return the statistics about the current game. Those statistics are only valid for the current generation.
     */
    public Statistics getStats() {

        return stats.setGen(sinf.getGen());
    }

    /**
     * Evolve to the next generation.
     */
    public void nextGen() {

        stats.startRecordTime();
        universe.nextGen();
        sinf.incGen();
        stats.stopRecordTime();
    }

    /**
     * Evolve for {@code n} generations.
     *
     * @param n the number of generation to evolve
     */
    public void nextGen(int n) {

        for (int i = 0; i < n; i++)
            nextGen();
    }

    /**
     * Record the current generation.
     *
     * @param oal the list of OutputAdapter to use.
     * @throws IOException if an OutputAdapter throws one.
     */
    public void record(List<OutputAdapter> oal) throws IOException {

        stats.startRecordTime();
        for (OutputAdapter oa : oal)
            oa.feed(universe.getCells());
        stats.stopRecordTime();
    }

    /**
     * Record the current generation.
     *
     * @param oa the OutputAdapter to use
     * @throws IOException if the OutputAdapter throws one
     */
    public void record(OutputAdapter oa) throws IOException {

        record(Collections.singletonList(oa));
    }

    /**
     * Record the next generation.
     *
     * @param oal the list of OutputAdapter to use.
     * @throws IOException if an OutputAdapter throws one.
     */
    public void recordNext(List<OutputAdapter> oal) throws IOException {

        nextGen();
        record(oal);
    }

    /**
     * Record the next generation.
     *
     * @param oa the OutputAdapter to use
     * @throws IOException if the OutputAdapter throws one
     */
    public void recordNext(OutputAdapter oa) throws IOException {

        nextGen();
        record(oa);
    }

    /**
     * Record {@code n} generation, plus the current one (or not).
     *
     * @param oal             the list of OutputAdapter to use
     * @param n               the number of generation to record
     * @param doRecordCurrent whether to record the current step
     * @throws IOException if the OutputAdapter throws one
     */
    public void record(List<OutputAdapter> oal, int n, boolean doRecordCurrent) throws IOException {

        if (doRecordCurrent)
            record(oal);
        for (int i = 0; i < n; i++)
            recordNext(oal);
    }

    /**
     * Record {@code n} generation, plus the current one (or not).
     *
     * @param oa              the OutputAdapter to use
     * @param n               the number of generation to record
     * @param doRecordCurrent whether to record the current step
     * @throws IOException if the OutputAdapter throws one
     */
    public void record(OutputAdapter oa, int n, boolean doRecordCurrent) throws IOException {

        if (doRecordCurrent)
            record(oa);
        for (int i = 0; i < n; i++)
            recordNext(oa);
    }

    /**
     * @return this Game of Life StateInfo.
     */
    public StateInfo getStateInfo() {

        return new StateInfo(sinf);
    }

    /**
     * Draw a straight line between two points.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void drawLine(int x1, int y1, int x2, int y2) {

        universe.drawLine(x1, y1, x2, y2);
    }

    /**
     * Make the cell at the given coordinates alive.
     */
    public void setCell(int x, int y) {

        universe.set(x, y, ALIVE);
    }

    /**
     * @return the current working universe
     */
    public Universe2D getUniverse() {

        return universe;
    }

    /**
     * @return a copy of the universe data
     */
    public byte[] dataCopy() {

        return Utils.copy(universe.getCells());
    }

    /**
     * Set the creator for this Game of Life instance
     *
     * @param creator the creator to put in records
     */
    public void setCreator(String creator) {

        sinf.setCreator(creator);
    }

    /**
     * Set the comment for this Game of Life instance
     *
     * @param comment the comment to put in records
     */
    public void setComment(String comment) {

        sinf.setComment(comment);
    }

    /**
     * Holds statistics about the current game instance.
     */
    public class Statistics {

        private int gen = 0;

        private long elapsedTime = 0;
        private long startTime = 0;

        public int getGen() {

            return gen;
        }

        Statistics setGen(int n) {

            gen = n;
            return this;
        }

        public long cellCount() {

            return universe.cellCount();
        }

        public long getAliveCells() {

            return universe.alive();
        }

        public long getElapsedTime() {

            return elapsedTime;
        }

        void startRecordTime() {

            startTime = System.currentTimeMillis();
        }

        void stopRecordTime() {

            elapsedTime += System.currentTimeMillis() - startTime;
        }

        @Override
        public String toString() {

            double speed = gen * 1000 / (double) elapsedTime;
            return String.format("Alive cells : %d. Compute time : %d ms. Ran %d steps on %d cells. Speed : %s gen/s. Performance : %s",
                getAliveCells(),
                elapsedTime,
                gen,
                cellCount(),
                new DecimalFormat("#.##").format(speed),
                new DecimalFormat("#.##").format(gen * cellCount() / Math.pow((double) elapsedTime, 2)));
        }
    }
}
