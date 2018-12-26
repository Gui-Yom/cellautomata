package com.limelion.gameoflife;

public class Board {

    public static int[][] neighbours = {{0, 1}, {1, 1}, {1, 0}, {1, - 1}, {0, - 1}, {- 1, - 1}, {- 1, 0}, {- 1, 1}};
    // cells[width][height]
    private boolean[][] cells;

    public Board() {
        this(100);
    }

    public Board(int square) {
        this(square, square);
    }

    public Board(int height, int width) {
        this(new boolean[width][height]);
    }

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
        return cells[x][y];
    }

    public int getWidthCenter() {
        return getWidth() / 2;
    }

    public int getHeightCenter() {
        return getHeight() / 2;
    }

    public int countNeighbours(int x, int y) {

        int count = 0;

        for (int i = 0; i < neighbours.length; i++) {

            int px = x + neighbours[i][0];
            int py = y + neighbours[i][1];

            if (px > 0 && px < (getWidth() - 1) && py > 0 && py < (getHeight() - 1))
                if (getCell(px, py))
                    count++;
        }
        // count > 8
        return count;
    }

    public void setCell(int x, int y, boolean value) {
        cells[x][y] = value;
    }

    public void switchCell(int x, int y) {
        cells[x][y] = !cells[x][y];
    }
}
