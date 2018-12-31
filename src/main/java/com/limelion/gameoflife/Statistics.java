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

public class Statistics {

    private int elapsedSteps = 0;
    private Board board;
    private long startTime;

    public Statistics(Board b) {
        this.startTime = System.currentTimeMillis();
        this.board = b;
    }

    public int getElapsedSteps() {
        return elapsedSteps;
    }

    public long getTotalCells() {
        return board.getSurface();
    }

    public long getAliveCells() {
        long[] aliveCount = {0};
        board.forEachCell((value, x, y) -> {
            if (value)
                aliveCount[0]++;
        });
        return aliveCount[0];
    }

    public long getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return String.format("Elapsed time : %d ms. Ran %d steps on %d cells. Currently alive cells : %d.", System.currentTimeMillis() - startTime, elapsedSteps, getTotalCells(), getAliveCells());
    }

    public void incSteps() {
        elapsedSteps++;
    }
}
