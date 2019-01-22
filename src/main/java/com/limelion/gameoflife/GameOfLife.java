/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.gameoflife;

import com.limelion.gameoflife.output.OutputAdaptater;
import com.limelion.gameoflife.rules.ConwayRule;
import com.limelion.gameoflife.rules.Rule;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Represent a party of game of life, with its own universe and rules.
 */
public class GameOfLife {

    private Board board;
    private Rule rule;
    private Statistics stats;
    private Metadata meta;

    /**
     * Create a new GameOfLife with a default universe of 100*100 and the Conway's rule.
     */
    public GameOfLife() {

        this(100, new ConwayRule());
    }

    /**
     * Create a new GameOfLife with a square universe and the specified rule.
     * @param square the length of a side
     * @param rule the rule to use
     */
    public GameOfLife(int square, Rule rule) {

        this(square, square, rule);
    }

    /**
     * Create a new GameOfLife with a rectangular universe and the specified rule.
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param rule the rule to use
     */
    public GameOfLife(int width, int height, Rule rule) {

        this.board = new Board(width, height);
        this.rule = rule;
        this.stats = new Statistics(board);
        this.meta = new Metadata().setWidth(width).setHeight(height).setRule(rule.toString());
    }

    /**
     * Create a new GameOfLife from cells data.
     * @param boardData the cells data
     * @param rule the rule to use
     */
    public GameOfLife(boolean[][] boardData, Rule rule) {

        this.board = new Board(boardData);
        this.rule = rule;
        this.stats = new Statistics(board);
        this.meta = new Metadata().setWidth(board.getWidth()).setHeight(board.getHeight()).setRule(rule.toString());
    }

    /**
     * Create a new GameOfLife from a parsed GLD file.
     * @param gld the parsed GLD file
     */
    public GameOfLife(Goldata gld) {

        this.board = new Board(gld.getData());
        this.stats = new Statistics(board);
        stats.incGen(gld.getMetadata().getGen());
        this.rule = Utils.parse(gld.getMetadata().getRule());
        this.meta = new Metadata().setWidth(board.getWidth()).setHeight(board.getHeight()).setRule(rule.toString());
    }

    /**
     * @return the statistics about the current game
     */
    public Statistics getStats() {

        return stats;
    }

    /**
     * @return the universe
     */
    public Board getBoard() {

        return board;
    }

    /**
     * Run a new step. A step is a new computation round over the board with the given rule.
     */
    public void nextGen() {

        stats.startRecordTime();

        // Here we MUST make a copy of the array.
        boolean[][] newBoard = getBoard().copyShape();

        for (int i = 0; i < getBoard().getWidth(); i++)
            for (int j = 0; j < getBoard().getHeight(); j++) {

                int neighbours = board.countNeighbours(i, j);

                if (neighbours > 0)
                    newBoard[i][j] = rule.apply(getBoard().getCell(i, j), neighbours);
            }
        board = new Board(newBoard);
        stats.incGen();
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

    /**
     * Record the current state.
     *
     * @param oa
     *     the output adaptater to use to record
     *
     * @throws IOException
     */
    public void recordCurrent(OutputAdaptater oa) throws IOException {

        stats.startRecordTime();
        oa.feed(getBoard().getCells());
        stats.stopRecordTime();
    }

    /**
     * Record the state after the next state.
     *
     * @param oa
     *     the output adaptater to use to record
     *
     * @throws IOException
     */
    public void recordNext(OutputAdaptater oa) throws IOException {

        nextGen();
        recordCurrent(oa);
    }

    /**
     * Record {@code n} steps, including the current or not.
     *
     * @param oa
     *     the output adaptater to use to record
     * @param n
     *     the number of steps to record
     * @param doRecordCurrent
     *     whether to record the current step
     *
     * @throws IOException
     */
    public void recordGen(OutputAdaptater oa, int n, boolean doRecordCurrent) throws IOException {

        if (doRecordCurrent)
            recordCurrent(oa);
        for (int i = 0; i < n; i++)
            recordNext(oa);
    }

    public void createhtmlplayer(String path, String animationPath) throws IOException {

        PrintWriter pw = new PrintWriter(Utils.cleanCreate(path));
        pw.printf("<!DOCTYPE html>%n<html>%n  <head>%n    <title>%s</title>%n  </head>%n  <body>%n    <img src=\"%s\" style=\"border:3px solid black\">%n  </body>%n</html>%n", animationPath, animationPath);
        pw.close();
    }

    /**
     * @return this game metadata
     */
    public Metadata getMetadata() {

        return meta.setGen(stats.getGen());
    }
}
