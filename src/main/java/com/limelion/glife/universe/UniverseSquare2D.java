package com.limelion.glife.universe;

import com.limelion.glife.utils.TriConsumerVoid;

/**
 * Represents a classic 2D Square universe.
 */
public class UniverseSquare2D {

    /**
     * Designates the relative position of the neighbours cells.
     */
    public static int[][] neighbours = { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 } };
    // cells[x: width][y: height]
    private boolean[][] cells;

    public UniverseSquare2D() {

        this(100);
    }

    /**
     * Creates a new square board.
     *
     * @param square
     */
    public UniverseSquare2D(int square) {

        this(square, square);
    }

    /**
     * Create a new board by specifying its width and height.
     *
     * @param height
     * @param width
     */
    public UniverseSquare2D(int width, int height) {

        this(new boolean[width][height]);
    }

    /**
     * Creates a new board from existing data.
     *
     * @param seed
     *     an array
     */
    public UniverseSquare2D(boolean[][] seed) {

        this.cells = seed;
    }

    public int getWidth() {

        return cells.length;
    }

    public int getHeight() {

        return cells[0].length;
    }

    public boolean[][] getCells() {

        return cells;
    }

    public long cellCount() {

        return getHeight() * getWidth();
    }

    public long alive() {

        final long[] count = { 0 };

        forEachCell((value, x, y) -> {
            if (value)
                count[0]++;
        });

        return count[0];
    }

    public boolean[][] model() {

        return new boolean[getWidth()][getHeight()];
    }

    public boolean get(int x, int y) {

        if (x < 0 || x > cells.length || y < 0 || y > cells[0].length)
            return false;

        return cells[x][y];
    }

    public void set(int x, int y, boolean value) {

        if (x < 0 || x > cells.length || y < 0 || y > cells[0].length)
            return;

        cells[x][y] = value;
    }

    public int countNeighbours(int x, int y) {

        int count = 0;

        for (int i = 0; i < neighbours.length; i++) {

            int px = x + neighbours[i][0];
            int py = y + neighbours[i][1];

            if (px > 0 && px < (getWidth() - 1) && py > 0 && py < (getHeight() - 1))
                if (get(px, py))
                    count++;
        }
        // count <= 8
        return count;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {

        // We can only draw a straight line, either vertical or horizontal
        if (x1 != x2 || y1 != y2) {
            if (x1 == x2) {
                if (y1 > y2) {
                    int a = y1;
                    y1 = y2;
                    y2 = a;
                }
                for (int i = y1; i <= y2; i++) {
                    set(x1, i, true);
                }
            } else if (y1 == y2) {
                if (x1 > x2) {
                    int a = x1;
                    x1 = x2;
                    x2 = a;
                }
                for (int i = x1; i <= x2; i++) {
                    set(y1, i, true);
                }
            }
        } else throw new IllegalArgumentException("Specified line is not straight !");
    }

    public void forEachCell(TriConsumerVoid<Boolean, Integer, Integer> action) {

        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                action.func(get(i, j), i, j);
            }
        }
    }
}
