package com.limelion.glife.universe;

import com.limelion.glife.GameOfLife;
import com.limelion.glife.Rule;
import com.limelion.glife.utils.TriConsumerVoid;

/**
 * Represents a classic 2D Square universe.
 */
public class Universe2D {

    // We keep to arrays in memory so that we do not need to reallocate one each time
    private final byte[] cells;
    private final byte[] cells2;

    private final int width;
    private final int height;
    // Tells if we're in an even or odd generation (for array swapping)
    // if (even) : read from cells write to cells2
    // if (!even) : read from cells2 write to cells
    private boolean even = true;
    // The right side is bound to the left, same with bottom and top
    private boolean bound = false;

    private Rule rule;

    /*
    Resolving coordinates from array index :
    x = i % width
    y = i / height
    Resolving array index from coordinates :
    i = x * width + y

    000
    100
    000

    000 100 000
     */

    public Universe2D(byte[] seed, int width, int height, Rule r, boolean bound) {

        if (seed.length != width * height)
            throw new IllegalArgumentException("Given array and dimensions are incompatibles");

        this.width = width;
        this.height = height;

        this.cells = seed;
        this.cells2 = model();

        this.rule = r;

        this.bound = bound;
    }

    public int getWidth() {

        return width;
    }

    public int getHeight() {

        return height;
    }

    public boolean isBound() {

        return bound;
    }

    public byte[] getCells() {

        return even ? cells : cells2;
    }

    public long cellCount() {

        return width * height;
    }

    public long alive() {

        long count = 0;

        if (even) {
            for (int i = 0; i < cells.length; i++)
                if (cells[i] == GameOfLife.ALIVE)
                    count++;
        } else {
            for (int i = 0; i < cells2.length; i++)
                if (cells2[i] == GameOfLife.ALIVE)
                    count++;
        }
        return count;
    }

    public byte[] model() {

        return new byte[height * width];
    }

    public byte get(int x, int y) {

        if (bound) {
            x = x % width;
            y = y % height;
        }

        if (x < 0 || x >= width || y < 0 || y >= height)
            return GameOfLife.DEAD;

        return even ? cells[x * width + y] : cells2[x * width + y];
    }

    public byte get(int index) {

        return get(index % width, index / height);
    }

    public void set(int x, int y, byte value) {

        if (bound) {
            x = x % width;
            y = y % height;
        }

        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("Coordinates not in bounds !");

        if (even)
            cells2[x * width + y] = value;
        else
            cells[x * width + y] = value;
    }

    public void set(int index, byte value) {

        set(index % width, index / height, value);
    }

    public void nextGen() {
        // TODO nice algo
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                set(i, j, rule.apply(get(i, j), countNeighbours(i, j)) ? GameOfLife.ALIVE : GameOfLife.DEAD);
        even = !even;
    }

    /**
     * Moore neighbourhood
     *
     * @param x
     * @param y
     *
     * @return the number of alive neighbours
     */
    public int countNeighbours(int x, int y) {

        int count = 0;

        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                if (i != 0 || j != 0)
                    if (get(x + i, y + j) == GameOfLife.ALIVE)
                        count++;

        // count <= 8
        return count;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {

        int distance = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));

        for (int i = 0; i <= distance; i++) {
            double t = distance == 0 ? 0.0 : (double) i / distance;
            if (even)
                cells[(int) (Math.round(x1 + t * (x2 - x1)) * width + Math.round(y1 + t * (y2 - y1)))] = GameOfLife.ALIVE;
            else
                cells2[(int) (Math.round(x1 + t * (x2 - x1)) * width + Math.round(y1 + t * (y2 - y1)))] = GameOfLife.ALIVE;
        }
    }

    public void forEachCell(TriConsumerVoid<Byte, Integer, Integer> action) {

        for (int i = 0; i < getWidth(); i++)
            for (int j = 0; j < getHeight(); j++)
                action.func(get(i, j), i, j);
    }

    public Rule getRule() {

        return rule;
    }
}
