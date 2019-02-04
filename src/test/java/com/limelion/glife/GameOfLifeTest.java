/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.glife;

import com.limelion.glife.input.Input;
import com.limelion.glife.output.*;
import com.limelion.glife.utils.Utils;
import com.vg.apng.APNG;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class GameOfLifeTest {

    @Test
    public void test() throws IOException {

        System.out.println("***** General testing *****");
        //HERE WE GO !
        GameOfLife gol = new GameOfLife(500, Rule.Square2D.CONWAY);

        gol.drawLine(250, 50, 250, 50);

        OutputParams op = new OutputParams(gol.getStateInfo().setCreator("Me").setComment("test"))
            .setDelay(250, TimeUnit.MILLISECONDS)
            .setRepeats(0);

        try (OutputAdapter outBMP = new OutputBMP(op, Utils.cleanCreate("output/testOutput.bmp"))) {
            gol.record(outBMP);
        }

        try (OutputAdapter outPNG = new OutputPNG(op.setBaseFileName("output/testOutput_gen@gen@.png"))) {
            gol.record(outPNG, 9, true);
        }

        OutputAdapter outGLD = new OutputGLD(op, Utils.cleanCreate("output/testOutput.gld"));
        gol.record(outGLD);
        outGLD.close();

        try (OutputAdapter outAPNG = new OutputAPNG(op, Utils.cleanCreate("output/testOutput.apng"));
             OutputAdapter outGIF = new OutputGIF(op, Utils.cleanCreate("output/testOutput.gif"))) {

            gol.record(Arrays.asList(outAPNG, outGIF), 50, false);
        }

        Utils.createhtmlplayer("output/playerAPNG.html", "testOutput.apng");
        Utils.createhtmlplayer("output/playerGIF.html", "testOutput.gif");

        System.out.println("Generated files.");
        System.out.println("Stats : " + gol.getStats());

        GameOfLife gol2 = new GameOfLife(Input.fromGLD(new File("output/testOutput.gld")));

        OutputAdapter outGLD2 = new OutputGLD(new OutputParams(gol2.getStateInfo()), Utils.cleanCreate("output/testOutput2.gld"));
        gol2.record(outGLD2);
        outGLD2.close();

        System.out.println("Generated second fileset.");
        System.out.println("Comparing ...");

        byte[] gld1 = Files.readAllBytes(new File("output/testOutput.gld").toPath());
        byte[] gld2 = Files.readAllBytes(new File("output/testOutput2.gld").toPath());

        Assert.assertArrayEquals(gld1, gld2);

        System.out.println("Files are similar. OK");
        System.out.println("***** End of general tests *****");
    }

    @Test
    public void benchmarks() {

        System.out.println("***** Blank benchmark (no output) *****");

        long startTime = System.currentTimeMillis();

        GameOfLife gol = new GameOfLife(1000, Rule.Square2D.CONWAY);
        gol.drawLine(50, 50, 950, 950);
        gol.nextGen(1000);

        System.out.printf("Elapsed time : %d ms%n", System.currentTimeMillis() - startTime);
        System.out.println("Blank benchmark : " + gol.getStats());
    }

    @Test
    public void benchGIF() throws IOException {

        System.out.println("***** GIF benchmark *****");

        GameOfLife gol = new GameOfLife(500, Rule.Square2D.CONWAY);
        gol.drawLine(250, 50, 250, 450);

        OutputAdapter oa = new OutputGIF(
            new OutputParams(gol.getStateInfo()).setRepeats(0).setDelay(50, TimeUnit.MILLISECONDS),
            Utils.cleanCreate("output/benchGIF.gif"));

        gol.record(oa, 500, false);
        oa.close();

        System.out.println("GIF benchmark : " + gol.getStats());
        Utils.createhtmlplayer("output/benchGIF.html", "benchGIF.gif");
    }

    @Test
    public void benchAPNG() throws IOException {

        System.out.println("***** APNG benchmark *****");

        GameOfLife gol = new GameOfLife(500, Rule.Square2D.CONWAY);
        gol.drawLine(250, 50, 250, 450);

        OutputAdapter oa = new OutputAPNG(
            new OutputParams(gol.getStateInfo()).setRepeats(APNG.INFINITE_LOOP).setDelay(100, TimeUnit.MILLISECONDS),
            Utils.cleanCreate("output/benchAPNG.apng"));

        gol.record(oa, 500, true);
        oa.close();

        System.out.println("APNG benchmark : " + gol.getStats());
        Utils.createhtmlplayer("output/benchAPNG.html", "benchAPNG.apng");
    }

    @Test
    public void benchPNG() throws IOException {

        System.out.println("***** APNG benchmark *****");

        GameOfLife gol = new GameOfLife(500, Rule.Square2D.CONWAY);
        gol.drawLine(250, 100, 250, 400);

        OutputAdapter oa = new OutputPNG(new OutputParams(gol.getStateInfo()).setBaseFileName("output/benchPNG_@gen@.png"));

        gol.record(oa, 49, true);
        oa.close();

        System.out.println("PNG benchmark : " + gol.getStats());
    }

    @Test
    public void smolTest() throws IOException {

        System.out.println("***** Smol test *****");

        GameOfLife gol = new GameOfLife(3, Rule.Square2D.CONWAY);
        gol.drawLine(1, 0, 1, 2);

        OutputAdapter oa = new OutputGIF(
            new OutputParams(gol.getStateInfo()).setRepeats(0).setDelay(250, TimeUnit.MILLISECONDS),
            Utils.cleanCreate("output/smolTest.gif"));

        gol.record(oa, 10, true);
        oa.close();

        System.out.println("Smol test : " + gol.getStats());
        Utils.createhtmlplayer("output/smolTest.html", "smolTest.gif");
    }
}
