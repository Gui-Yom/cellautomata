package com.limelion.gameoflife;

/**
 * The board where magic happens.   <br>
 * +--------------------------&gt; x   <br>
 * |                                <br>
 * |                                <br>
 * | XXXXX                          <br>
 * |                                <br>
 * |                                <br>
 * |                                <br>
 * |                                <br>
 * \/                               <br>
 * y                                <br>
 *                                  <br>
 */
public class Board {

    /**
     * Designates the relative position of the neighbours cells.
     */
    public static int[][] neighbours = {{0, 1}, {1, 1}, {1, 0}, {1, - 1}, {0, - 1}, {- 1, - 1}, {- 1, 0}, {- 1, 1}};
    // cells[width][height]
    // cells[x][y]
    private boolean[][] cells;

    public Board() {
        this(100);
    }

    /**
     * Creates a new square board.
     * @param square
     */
    public Board(int square) {
        this(square, square);
    }

    /**
     * Create a new board by specifying its width and height.
     * @param height
     * @param width
     */
    public Board(int width, int height) {
        this(new boolean[width][height]);
    }

    /**
     * Creates a new board from existing data.
     * @param seed an array
     */
    public Board(boolean[][] seed) {
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

    public long getSurface() {
        return getHeight() * getWidth();
    }

    public boolean[][] copyShape() {
        return new boolean[getWidth()][getHeight()];
    }

    public boolean getCell(int x, int y) {
        if (!Utils.checkCoords(x, y, getWidth(), getHeight()))
            throw new IllegalArgumentException("Bad coordinates specified !");
        return cells[x][y];
    }

    public int getWidthCenter() {
        return getWidth() / 2;
    }

    public int getHeightCenter() {
        return getHeight() / 2;
    }

    public int countNeighbours(int x, int y) {
        if (!Utils.checkCoords(x, y, getWidth(), getHeight()))
            throw new IllegalArgumentException("Bad coordinates specified !");

        int count = 0;

        for (int i = 0; i < neighbours.length; i++) {

            int px = x + neighbours[i][0];
            int py = y + neighbours[i][1];

            if (px > 0 && px < (getWidth() - 1) && py > 0 && py < (getHeight() - 1))
                if (getCell(px, py))
                    count++;
        }
        // count <= 8
        return count;
    }

    public void setCell(int x, int y, boolean value) {
        if (!Utils.checkCoords(x, y, getWidth(), getHeight()))
            throw new IllegalArgumentException("Bad coordinates specified !");
        cells[x][y] = value;
    }

    public void switchCell(int x, int y) {
        setCell(x, y, !getCell(x, y));
    }

    public void killCell(int x, int y) {
        setCell(x, y, false);
    }

    public void birthCell(int x, int y) {
        setCell(x, y, true);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {

        if (!Utils.checkCoords(x1, y1, getWidth(), getHeight()) && !Utils.checkCoords(x2, y2, getWidth(), getHeight()))
            throw new IllegalArgumentException("Bad coordinates specified !");

        // We can only draw a straight line, either vertical or horizontal
        if (x1 != x2 || y1 != y2) {
            if (x1 == x2) {
                if (y1 > y2) {
                    int a = y1;
                    y1 = y2;
                    y2 = a;
                }
                for (int i = y1; i <= y2; i++) {
                    birthCell(x1, i);
                }
            } else if (y1 == y2) {
                if (x1 > x2) {
                    int a = x1;
                    x1 = x2;
                    x2 = a;
                }
                for (int i = x1; i <= x2; i++) {
                    birthCell(y1, i);
                }
            }
        } else throw new IllegalArgumentException("Specified line is not straight !");
    }

    public void forEachCell(TriConsumerVoid<Boolean, Integer, Integer> action) {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                action.func(getCell(i, j), i, j);
            }
        }
    }
}
