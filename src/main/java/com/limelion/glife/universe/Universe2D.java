package com.limelion.glife.universe;

import com.limelion.glife.GameOfLife;
import com.limelion.glife.Rule;
import com.limelion.glife.utils.TriConsumer;

/**
 * Represents a classic 2D Square universe.
 */
public class Universe2D {

    private final int width;
    private final int height;
    // We keep to arrays in memory so that we do not need to reallocate one each time
    private byte[] read;
    private byte[] write;
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

        this.read = seed;
        this.write = model();

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

        return read;
    }

    public long cellCount() {

        return width * height;
    }

    // TODO parallelize
    public long alive() {

        long count = 0;

        for (int i = 0; i < read.length; ++i)
            if (read[i] == GameOfLife.ALIVE)
                ++count;
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

        return read[x * width + y];
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

        write[x * width + y] = value;
    }

    public void set(int index, byte value) {

        set(index % width, index / height, value);
    }

    public void nextGen() {
        // TODO parallelize task
        for (int i = 0; i < width; ++i)
            for (int j = 0; j < height; ++j)
                set(i, j, rule.apply(get(i, j), countNeighbours(i, j)) ? GameOfLife.ALIVE : GameOfLife.DEAD);
        swapBuffers();
    }

    /**
     * Moore neighbourhood
     *
     * @param x
     * @param y
     * @return the number of alive neighbours
     */
    public int countNeighbours(int x, int y) {

        int count = 0;

        for (int i = -1; i <= 1; ++i)
            for (int j = -1; j <= 1; ++j)
                if (i != 0 || j != 0)
                    if (get(x + i, y + j) == GameOfLife.ALIVE)
                        ++count;

        // count <= 8
        return count;
    }

    private void swapBuffers() {

        byte[] temp = read;
        read = write;
        write = temp;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {

        int distance = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));

        for (int i = 0; i <= distance; ++i) {
            double t = distance == 0 ? 0.0 : (double) i / distance;
            read[(int) (Math.round(x1 + t * (x2 - x1)) * width + Math.round(y1 + t * (y2 - y1)))] = GameOfLife.ALIVE;
        }
    }

    public void forEachCell(TriConsumer<Byte, Integer, Integer> action) {

        for (int i = 0; i < getWidth(); ++i)
            for (int j = 0; j < getHeight(); ++j)
                action.func(get(i, j), i, j);
    }

    public Rule getRule() {

        return rule;
    }
}
