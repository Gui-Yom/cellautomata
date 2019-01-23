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

import com.limelion.glife.output.OutputAdaptater;
import com.limelion.glife.universe.UniverseSquare2D;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Represent a party of game of life, with its own universe and rules.
 */
public class GameOfLife {

    private UniverseSquare2D universe;
    private Rule rule;
    private Statistics stats;
    private Metadata meta;

    /**
     * Create a new GameOfLife with a default universe of 100*100 and the Conway's rule.
     */
    public GameOfLife() {

        this(100, Rule.Square2D.CONWAY);
    }

    /**
     * Create a new GameOfLife with a square universe and the specified rule.
     *
     * @param square
     *     the length of a side
     * @param rule
     *     the rule to use
     */
    public GameOfLife(int square, Rule rule) {

        this(square, square, rule);
    }

    /**
     * Create a new GameOfLife with a rectangular universe and the specified rule.
     *
     * @param width
     *     the width of the rectangle
     * @param height
     *     the height of the rectangle
     * @param rule
     *     the rule to use
     */
    public GameOfLife(int width, int height, Rule rule) {

        this(new boolean[width][height], rule);
    }

    /**
     * Create a new GameOfLife from cells data.
     *
     * @param boardData
     *     the cells data
     * @param rule
     *     the rule to use
     */
    public GameOfLife(boolean[][] boardData, Rule rule) {

        this.universe = new UniverseSquare2D(boardData);
        this.rule = rule;
        this.stats = new Statistics();
        this.meta = new Metadata().setWidth(universe.getWidth()).setHeight(universe.getHeight()).setRule(rule.toString());
    }

    /**
     * Create a new GameOfLife from a parsed GLD file.
     *
     * @param gld
     *     the parsed GLD file
     */
    public GameOfLife(Goldata gld) {

        this.universe = new UniverseSquare2D(gld.getData());
        this.stats = new Statistics();
        this.rule = Rule.parse(gld.getMetadata().getRule());
        this.meta = gld.getMetadata();
    }

    /**
     * @return the statistics about the current game
     */
    public Statistics getStats() {

        return stats.setGen(meta.getGen());
    }

    /**
     * Run a new step. A step is a new computation round over the universe with the given rule.
     */
    public void nextGen() {

        stats.startRecordTime();

        // TODO better algorithm
        boolean[][] newUniverse = universe.model();

        for (int i = 0; i < universe.getWidth(); i++)
            for (int j = 0; j < universe.getHeight(); j++) {

                int neighbours = universe.countNeighbours(i, j);

                if (neighbours > 0)
                    newUniverse[i][j] = rule.apply(universe.get(i, j), neighbours);
            }
        universe = new UniverseSquare2D(newUniverse);
        meta.incGen();
        stats.stopRecordTime();
    }

    /**
     * Run {@code n} steps.
     *
     * @param n
     *     the number of steps to run
     */
    public void nextGen(int n) {

        for (int i = 0; i < n; i++)
            nextGen();
    }

    public void record(List<OutputAdaptater> oal) throws IOException {

        stats.startRecordTime();
        for (OutputAdaptater oa : oal)
            oa.feed(universe.getCells());
        stats.stopRecordTime();
    }

    public void record(OutputAdaptater oa) throws IOException {

        record(Collections.singletonList(oa));
    }

    /**
     * Record the state after the next state.
     *
     * @param oal
     *     the output adaptater to use to record
     *
     * @throws IOException
     */
    public void recordNext(List<OutputAdaptater> oal) throws IOException {

        nextGen();
        record(oal);
    }

    public void recordNext(OutputAdaptater oa) throws IOException {

        nextGen();
        record(oa);
    }

    /**
     * Record {@code n} steps, including the current or not.
     *
     * @param oal
     *     the output adaptater to use to record
     * @param n
     *     the number of steps to record
     * @param doRecordCurrent
     *     whether to record the current step
     *
     * @throws IOException
     */
    public void recordGen(List<OutputAdaptater> oal, int n, boolean doRecordCurrent) throws IOException {

        if (doRecordCurrent)
            record(oal);
        for (int i = 0; i < n; i++)
            recordNext(oal);
    }

    public void recordGen(OutputAdaptater oa, int n, boolean doRecordCurrent) throws IOException {

        if (doRecordCurrent)
            record(oa);
        for (int i = 0; i < n; i++)
            recordNext(oa);
    }

    /**
     * @return this game metadata
     */
    public Metadata getMetadata() {

        return meta;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {

        universe.drawLine(x1, y1, x2, y2);
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

            return String.format("Compute time : %d ms. Ran %d steps on %d cells. Currently alive cells : %d.", elapsedTime, gen, cellCount(), getAliveCells());
        }
    }
}
