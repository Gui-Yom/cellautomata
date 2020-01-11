/*
 * Copyright (c) 2018 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package guiyom.cellautomata;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represent a cellular automata rule.
 */
@FunctionalInterface
public interface Rule extends Serializable {

    static Rule parse(String desc) {
        // Descriptor is like : <birth conditions>/<survive conditions>
        boolean isBirth = true;
        final Set<Integer> birth = new HashSet<>(9);
        final Set<Integer> survive = new HashSet<>(9);
        for (char c : desc.toCharArray()) {
            if (c == '/')
                isBirth = false;
            else {
                int value = Integer.parseInt(String.valueOf(c));
                if (isBirth)
                    birth.add(value);
                else
                    survive.add(value);
            }
        }
        return (cell, nCount) -> cell == CellAutomata.DEAD ? birth.contains(nCount) : survive.contains(nCount);
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
    enum Square2D {

        /**
         * A chaotic rule that is by far the most well-known and well-studied. It exhibits highly complex behavior.<br>
         * Rule descriptor : B3/S23
         */
        CONWAY("3/23", (cell, nCount) -> cell == CellAutomata.DEAD ? nCount == 3 : (nCount == 2 || nCount == 3)),
        /**
         * A stable rule that is symmetric under on-off reversal. Many patterns exhibiting highly complex behavior have
         * been found for it.<br> Rule descriptor : B3678/S34678
         */
        DAYNIGHT("3678/34678", (cell, nCount) -> cell == CellAutomata.DEAD ? (nCount == 3 || (nCount >= 6 && nCount <= 8)) : (nCount >= 3 && nCount != 5)),
        /**
         * A chaotic rule very similar to Conway's Life that is of interest because it has a simple replicator.<br> Rule
         * descriptor : B36/S23
         */
        HIGHLIFE("36/23", (cell, nCount) -> cell == CellAutomata.DEAD ? (nCount == 3 || nCount == 6) : (nCount == 2 || nCount == 3)),
        /**
         * An exploding rule in which only cells with no neighbors survive. It has many spaceships, puffers, and
         * oscillators, some of infinitely extensible size and period.<br> Rule descriptor : B2/S0
         */
        LIVEFREEORDIE("2/0", (cell, nCount) -> cell == CellAutomata.DEAD ? nCount == 2 : nCount == 0),
        /**
         * An expanding rule that crystalizes to form maze-like designs.<br> Rule descriptor : B3/S12345
         */
        MAZE("3/12345", (cell, nCount) -> cell == CellAutomata.DEAD ? nCount == 3 : (nCount >= 1 && nCount <= 5)),
        /**
         * An expanding rule that produces complex flakes. It also has important ladder patterns.<br> Rule descriptor :
         * B3/S0123456789
         */
        NODEATH("3/0123456789", (cell, nCount) -> cell == CellAutomata.ALIVE || nCount == 3),
        /**
         * A chaotic rule that strongly resembles regular Life, with many exciting natural technologies.<br> Rule
         * descriptor : B38/S23
         */
        PEDESTRIAN("38/23", (cell, nCount) -> cell == CellAutomata.DEAD ? (nCount == 3 || nCount == 8) : (nCount == 2 || nCount == 3)),
        /**
         * A rule in which every pattern is a replicator.<br> Rule descriptor : B1357/S1357
         */
        REPLICATOR("1357/1357", (cell, nCount) -> cell == CellAutomata.DEAD ? (nCount == 1 || nCount == 3 || nCount == 5 || nCount == 7) : (nCount == 1 || nCount == 3 || nCount == 5 || nCount == 7)),
        /**
         * An exploding rule in which every cell dies in every generation. It has many simple orthogonal spaceships,
         * though it is in general difficult to create patterns that don't explode.<br> Rule descriptor : B2/S
         */
        SEEDS("2/", (cell, nCount) -> cell == CellAutomata.DEAD && nCount == 2),
        /**
         * A chaotic rule with many simple still lifes, oscillators and spaceships. Its name comes from the fact that it
         * sends patterns made up of 2x2 blocks to patterns made up of 2x2 blocks.<br> Rule descriptor : B36/S125
         */
        TWOXTWO("36/125", (cell, nCount) -> cell == CellAutomata.DEAD ? (nCount == 3 || nCount == 6) : (nCount == 1 || nCount == 2 || nCount == 5));

        private String desc;
        private Rule rule;

        Square2D(String desc, Rule rule) {

            this.desc = desc;
            this.rule = rule;
        }

        public Rule getRule() {
            return rule;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {

            return name() + ": " + desc;
        }
    }
}
