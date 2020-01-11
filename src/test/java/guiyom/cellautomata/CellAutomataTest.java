/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package guiyom.cellautomata;

import guiyom.cellautomata.output.*;
import guiyom.cellautomata.utils.Utils;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class CellAutomataTest {

    @Test
    public void test() throws IOException {

        System.out.println("***** General testing *****");
        //HERE WE GO !
        CellAutomata gol = new CellAutomata(500, Rule.Square2D.CONWAY.getRule());

        gol.drawLine(250, 50, 250, 50);

        AutomataOutput outBMP = new OutputBMP(new FileOutputStream(Utils.cleanCreate("output/testOutput.bmp")));
        gol.record(outBMP);

        AutomataOutput outPNG = new OutputPNG("output/testOutput_gen@gen@.png");
        gol.record(outPNG, 9, true);

        try (OutputAPNG outAPNG = new OutputAPNG(new FileOutputStream(Utils.cleanCreate("output/testOutput.apng")), 250, 0);
             OutputGIF outGIF = new OutputGIF(new FileOutputStream(Utils.cleanCreate("output/testOutput.gif")), 250, 0)) {

            gol.record(Arrays.asList(outAPNG, outGIF), 50, false);
        }

        Utils.createhtmlplayer("output/playerAPNG.html", "testOutput.apng");
        Utils.createhtmlplayer("output/playerGIF.html", "testOutput.gif");

        System.out.println("Generated files.");
        System.out.println("Stats : " + gol.getStats());
        System.out.println("***** End of general tests *****");
    }

    @Test
    public void benchmarks() {

        System.out.println("***** Blank benchmark (no output) *****");

        long startTime = System.currentTimeMillis();

        CellAutomata gol = new CellAutomata(1000, Rule.Square2D.CONWAY.getRule());
        gol.drawLine(50, 50, 950, 950);
        gol.nextGen(1000);

        System.out.printf("Elapsed time : %d ms%n", System.currentTimeMillis() - startTime);
        System.out.println("Blank benchmark : " + gol.getStats());
    }

    @Test
    public void benchGIF() throws IOException {

        System.out.println("***** GIF benchmark *****");

        CellAutomata gol = new CellAutomata(500, Rule.Square2D.CONWAY.getRule());
        gol.drawLine(250, 50, 250, 450);

        OutputGIF oa = new OutputGIF(new FileOutputStream(Utils.cleanCreate("output/benchGIF.gif")), 250, 0);

        gol.record(oa, 500, false);
        oa.close();

        System.out.println("GIF benchmark : " + gol.getStats());
        Utils.createhtmlplayer("output/benchGIF.html", "benchGIF.gif");
    }

    @Test
    public void benchAPNG() throws IOException {

        System.out.println("***** APNG benchmark *****");

        CellAutomata gol = new CellAutomata(500, Rule.Square2D.CONWAY.getRule());
        gol.drawLine(250, 50, 250, 450);

        OutputAPNG oa = new OutputAPNG(new FileOutputStream(Utils.cleanCreate("output/benchAPNG.apng")), 250, 0);

        gol.record(oa, 500, true);
        oa.close();

        System.out.println("APNG benchmark : " + gol.getStats());
        Utils.createhtmlplayer("output/benchAPNG.html", "benchAPNG.apng");
    }

    @Test
    public void benchPNG() throws IOException {

        System.out.println("***** APNG benchmark *****");

        CellAutomata gol = new CellAutomata(500, Rule.Square2D.CONWAY.getRule());
        gol.drawLine(250, 100, 250, 400);

        AutomataOutput oa = new OutputPNG("output/benchPNG_@gen@.png");

        gol.record(oa, 49, true);

        System.out.println("PNG benchmark : " + gol.getStats());
    }

    @Test
    public void smolTest() throws IOException {

        System.out.println("***** Smol test *****");

        CellAutomata gol = new CellAutomata(3, Rule.Square2D.CONWAY.getRule());
        gol.drawLine(1, 0, 1, 2);

        OutputGIF oa = new OutputGIF(new FileOutputStream(Utils.cleanCreate("output/smolTest.gif")), 1000, 0);

        gol.record(oa, 10, true);
        oa.close();

        System.out.println("Smol test : " + gol.getStats());
        Utils.createhtmlplayer("output/smolTest.html", "smolTest.gif");
    }
}
