/*
 * Copyright (c) 2018 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.gameoflife;

import com.limelion.gameoflife.output.EncodingInfo;
import com.limelion.gameoflife.output.OutputAdaptater;
import com.limelion.gameoflife.output.OutputType;
import com.limelion.gameoflife.rules.ConwayRule;
import com.limelion.gameoflife.rules.Rule;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.vg.apng.APNG;
import com.vg.apng.Gray;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * The main class.
 */
public class GameOfLife {

    private Board board;
    private Rule rule;
    private Statistics stats;

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

    public static void main(String[] args) throws IOException {
        //HERE WE GO !
        GameOfLife gol = new GameOfLife();

        gol.getBoard().drawLine(20, 20, 20, 26);

        OutputAdaptater out = OutputType.APNG.getImpl().init(Utils.cleanCreate("output/output.png"),
                                                             new EncodingInfo()
                                                               .setHeight(gol.getBoard().getHeight())
                                                               .setWidth(gol.getBoard().getWidth())
                                                               .setDelay(500, TimeUnit.MILLISECONDS)
                                                               .setNumRepeats(0));

        gol.recordSteps(out, 20, true);

        out.finish();

        gol.createhtmlplayer("output/player.html", "output.png");

        System.out.println(gol.getStats());
    }

    public Board getBoard() {
        return board;
    }

    public Statistics getStats() {
        return stats;
    }

    /**
     * Run a new step. A step is a new computation round over the board with the given rule.
     */
    public void nextStep() {

        // Here we MUST make a copy of the array.
        boolean[][] newBoard = getBoard().copyShape();

        for (int i = 0; i < getBoard().getWidth(); i++)
            for (int j = 0; j < getBoard().getHeight(); j++) {
                newBoard[i][j] = rule.apply(getBoard().getCell(i, j), board.countNeighbours(i, j));
            }
        board = new Board(newBoard);
        stats.incSteps();
    }

    /**
     * Run {@code n} steps.
     * @param n the number of steps to run
     */
    public void nextStep(int n) {
        for (int i = 0; i < n; i++)
            nextStep();
    }

    /**
     * Record the current state.
     * @param oa the output adaptater to use to record
     * @throws IOException
     */
    public void recordCurrentState(OutputAdaptater oa) throws IOException {
        oa.feed(Utils.bool_to_gray(Utils.align(getBoard().getCells())));
    }

    /**
     * Record the state after the next state.
     * @param oa the output adaptater to use to record
     * @throws IOException
     */
    public void recordNextState(OutputAdaptater oa) throws IOException {
        nextStep();
        recordCurrentState(oa);
    }

    /**
     * Record {@code n} steps, including the current or not.
     * @param oa the output adaptater to use to record
     * @param n the number of steps to record
     * @param doRecordCurrent whether to record the current step
     * @throws IOException
     */
    public void recordSteps(OutputAdaptater oa, int n, boolean doRecordCurrent) throws IOException {
        if (oa.getType().isAnimated()) {
            if (doRecordCurrent)
                recordCurrentState(oa);
            for (int i = 0; i < n; i++)
                recordNextState(oa);
        }
    }

    public void createGIF(int steps, String path) throws IOException {
        AnimatedGifEncoder agife = new AnimatedGifEncoder();
        agife.setDelay(400);
        agife.setSize(getBoard().getWidth(), getBoard().getHeight());
        agife.setRepeat(0);
        agife.start(path);

        for (int i = 0; i < steps; i++) {
            agife.addFrame(Utils.createGrayImage(Utils.bool_to_gray(Utils.align(getBoard().getCells())),
                                                 getBoard().getWidth(),
                                                 getBoard().getHeight()));
            nextStep();
        }

        agife.finish();
    }

    public void createhtmlplayer(String path, String animationPath) throws IOException {
        PrintWriter pw = new PrintWriter(Utils.cleanCreate(path));
        pw.printf("<!DOCTYPE html>%n<html>%n  <head>%n    <title>%s</title>%n  </head>%n  <body>%n    <img src=\"%s\" style=\"border:3px solid black\">%n  </body>%n</html>%n", animationPath, animationPath);
        pw.close();
    }
}
