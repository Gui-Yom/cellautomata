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

import com.limelion.gameoflife.input.InputAdaptater;
import com.limelion.gameoflife.output.OutputAdaptater;
import com.limelion.gameoflife.rules.ConwayRule;
import com.limelion.gameoflife.rules.Rule;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * The main class.
 */
public class GameOfLife {

    private Board board;
    private Rule rule;
    private Statistics stats;

    public static byte[] MAGIC = new byte[] { 0x47, 0x4C, 0x44 };
    public static byte VERSION = 0x01;

    public GameOfLife() {

        this(100, new ConwayRule());
    }

    public GameOfLife(int square, Rule rule) {

        this.board = new Board(square);
        this.rule = rule;
        this.stats = new Statistics(board);
    }

    public GameOfLife(int width, int height, Rule rule) {

        this.board = new Board(width, height);
        this.rule = rule;
        this.stats = new Statistics(board);
    }

    public GameOfLife(boolean[][] boardData, Rule rule) {

        this.board = new Board(boardData);
        this.rule = rule;
        this.stats = new Statistics(board);
    }

    public GameOfLife(InputAdaptater ia, Rule rule) {

        this(ia.getBoardData(), rule);
    }

    public Statistics getStats() {

        return stats;
    }

    public Board getBoard() {

        return board;
    }

    /**
     * Run a new step. A step is a new computation round over the board with the given rule.
     */
    public void nextStep() {

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
        stats.incSteps();
        stats.stopRecordTime();
    }

    /**
     * Run {@code n} steps.
     *
     * @param n
     *     the number of steps to run
     */
    public void nextStep(int n) {

        for (int i = 0; i < n; i++)
            nextStep();
    }

    /**
     * Record the current state.
     *
     * @param oa
     *     the output adaptater to use to record
     *
     * @throws IOException
     */
    public void recordCurrentState(OutputAdaptater oa) throws IOException {

        stats.startRecordTime();
        oa.feed(Utils.bool_to_gray(Utils.align(getBoard().getCells())));
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
    public void recordNextState(OutputAdaptater oa) throws IOException {

        nextStep();
        recordCurrentState(oa);
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
    public void recordSteps(OutputAdaptater oa, int n, boolean doRecordCurrent) throws IOException {

        if (oa.getType().isAnimated()) {
            if (doRecordCurrent)
                recordCurrentState(oa);
            for (int i = 0; i < n; i++)
                recordNextState(oa);
        } else {
            if (doRecordCurrent)
                recordCurrentState(oa);
            else
                recordNextState(oa);
        }
    }

    public void createhtmlplayer(String path, String animationPath) throws IOException {

        PrintWriter pw = new PrintWriter(Utils.cleanCreate(path));
        pw.printf("<!DOCTYPE html>%n<html>%n  <head>%n    <title>%s</title>%n  </head>%n  <body>%n    <img src=\"%s\" style=\"border:3px solid black\">%n  </body>%n</html>%n", animationPath, animationPath);
        pw.close();
    }
}
