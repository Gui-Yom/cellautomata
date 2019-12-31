/*
 * Copyright (c) 2018 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.glife;

/**
 * Represent a cellular automata rule.
 */
@FunctionalInterface
public interface Rule {

    static Rule parse(String s) {

        if (s.equals(Square2D.CONWAY.desc) || s.equals("conway"))
            return Square2D.CONWAY;
        if (s.equals(Square2D.DAYNIGHT.desc) || s.equals("daynight"))
            return Square2D.DAYNIGHT;
        if (s.equals(Square2D.HIGHLIFE.desc) || s.equals("highlife"))
            return Square2D.HIGHLIFE;
        if (s.equals(Square2D.LIVEFREEORDIE.desc) || s.equals("livefreeordie"))
            return Square2D.LIVEFREEORDIE;
        if (s.equals(Square2D.NODEATH.desc) || s.equals("nodeath"))
            return Square2D.NODEATH;
        if (s.equals(Square2D.PEDESTRIAN.desc) || s.equals("pedestrian"))
            return Square2D.PEDESTRIAN;
        if (s.equals(Square2D.REPLICATOR.desc) || s.equals("replicator"))
            return Square2D.REPLICATOR;
        if (s.equals(Square2D.SEEDS.desc) || s.equals("seeds"))
            return Square2D.SEEDS;
        if (s.equals(Square2D.TWOXTWO.desc) || s.equals("twoxtwo"))
            return Square2D.TWOXTWO;
        else
            return null;
    }

    /**
     * Given the current state, get the state for the next generation.
     *
     * @param cell   the current state of the cell
     * @param nCount the neighbours count
     * @return the next state of the cell
     */
    boolean apply(byte cell, int nCount);

    /**
     * Implementations of the most common 2D (square) cellular automata rules.
     */
    enum Square2D implements Rule {

        /**
         * A chaotic rule that is by far the most well-known and well-studied. It exhibits highly complex behavior.<br>
         * Rule descriptor : B3/S23
         */
        CONWAY("B3/S23"),
        /**
         * A stable rule that is symmetric under on-off reversal. Many patterns exhibiting highly complex behavior have
         * been found for it.<br> Rule descriptor : B3678/S34678
         */
        DAYNIGHT("B3678/S34678"),
        /**
         * A chaotic rule very similar to Conway's Life that is of interest because it has a simple replicator.<br> Rule
         * descriptor : B36/S23
         */
        HIGHLIFE("B36/S23"),
        /**
         * An exploding rule in which only cells with no neighbors survive. It has many spaceships, puffers, and
         * oscillators, some of infinitely extensible size and period.<br> Rule descriptor : B2/S0
         */
        LIVEFREEORDIE("B2/S0"),
        /**
         * An expanding rule that crystalizes to form maze-like designs.<br> Rule descriptor : B3/S12345
         */
        MAZE("B3/S12345"),
        /**
         * An expanding rule that produces complex flakes. It also has important ladder patterns.<br> Rule descriptor :
         * B3/S0123456789
         */
        NODEATH("B3/S0123456789"),
        /**
         * A chaotic rule that strongly resembles regular Life, with many exciting natural technologies.<br> Rule
         * descriptor : B38/S23
         */
        PEDESTRIAN("B38/S23"),
        /**
         * A rule in which every pattern is a replicator.<br> Rule descriptor : B1357/S1357
         */
        REPLICATOR("B1357/S1357"),
        /**
         * An exploding rule in which every cell dies in every generation. It has many simple orthogonal spaceships,
         * though it is in general difficult to create patterns that don't explode.<br> Rule descriptor : B2/S
         */
        SEEDS("B2/S"),
        /**
         * A chaotic rule with many simple still lifes, oscillators and spaceships. Its name comes from the fact that it
         * sends patterns made up of 2x2 blocks to patterns made up of 2x2 blocks.<br> Rule descriptor : B36/S125
         */
        TWOXTWO("B36/S125");

        private String desc;

        Square2D(String desc) {

            this.desc = desc;
        }

        @Override
        public boolean apply(byte value, int nCount) {

            switch (this) {
                case CONWAY:
                    return value == GameOfLife.DEAD ? nCount == 3 : (nCount == 2 || nCount == 3);
                case DAYNIGHT:
                    return value == GameOfLife.DEAD ? (nCount == 3 || (nCount >= 6 && nCount <= 8)) : (nCount >= 3 && nCount != 5);
                case HIGHLIFE:
                    return value == GameOfLife.DEAD ? (nCount == 3 || nCount == 6) : (nCount == 2 || nCount == 3);
                case LIVEFREEORDIE:
                    return value == GameOfLife.DEAD ? nCount == 2 : nCount == 0;
                case MAZE:
                    return value == GameOfLife.DEAD ? nCount == 3 : (nCount >= 1 && nCount <= 5);
                case NODEATH:
                    return value == GameOfLife.ALIVE || nCount == 3;
                case PEDESTRIAN:
                    return value == GameOfLife.DEAD ? (nCount == 3 || nCount == 8) : (nCount == 2 || nCount == 3);
                case REPLICATOR:
                    return value == GameOfLife.DEAD ? (nCount == 1 || nCount == 3 || nCount == 5 || nCount == 7) : (nCount == 1 || nCount == 3 || nCount == 5 || nCount == 7);
                case SEEDS:
                    return value == GameOfLife.DEAD && nCount == 2;
                case TWOXTWO:
                    return value == GameOfLife.DEAD ? (nCount == 3 || nCount == 6) : (nCount == 1 || nCount == 2 || nCount == 5);
                default:
                    return false;
            }
        }

        @Override
        public String toString() {

            return desc;
        }
    }
}
