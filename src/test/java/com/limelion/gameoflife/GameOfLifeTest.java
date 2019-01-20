/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.gameoflife;

import com.limelion.gameoflife.input.InputGLD;
import com.limelion.gameoflife.output.*;
import com.limelion.gameoflife.rules.ConwayRule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class GameOfLifeTest {

    @Test
    public void test() throws IOException {

        //HERE WE GO !
        GameOfLife gol = new GameOfLife(500, new ConwayRule());

        //gol.getBoard().drawLine(20, 20, 20, 26);
        //gol.getBoard().drawLine(500, 500, 500, 800);
        gol.getBoard().drawLine(50, 250, 450, 250);

        EncodingInfo ei = new EncodingInfo()
            .setHeight(gol.getBoard().getHeight())
            .setWidth(gol.getBoard().getWidth())
            .setDelay(500, TimeUnit.MILLISECONDS)
            .setNumRepeats(0);

        //OutputAdaptater outAPNG = OutputType.APNG.getImpl().init(Utils.cleanCreate("output/output.apng"), ei);

        //OutputAdaptater outGIF = OutputType.GIF.getImpl().init(Utils.cleanCreate("output/output.gif"), ei);

        OutputAdaptater outPNG = OutputType.PNG.getImpl().init(Utils.cleanCreate("output/output.png"), ei);

        //OutputAdaptater outBMP = OutputType.BMP.getImpl().init(Utils.cleanCreate("output/output.bmp"), ei);

        OutputAdaptater outGLD = OutputType.GLD.getImpl().init(Utils.cleanCreate("output/output.gld"), ei);

        //gol.recordSteps(outAPNG, 200, true);
        //gol.recordSteps(outGIF, 20, true);
        gol.recordNextState(outPNG);
        //gol.recordNextState(outBMP);
        gol.recordCurrentState(outGLD);

        //outAPNG.finish();
        //outGIF.finish();
        outPNG.finish();
        //outBMP.finish();
        outGLD.finish();

        //gol.createhtmlplayer("output/playerAPNG.html", "output.apng");
        //gol.createhtmlplayer("output/playerGIF.html", "output.gif");
        //gol.createhtmlplayer("output/playerPNG.html", "output.png");
        //gol.createhtmlplayer("output/playerBMP.html", "output.bmp");

        System.out.println("Generated files.");
        System.out.println(gol.getStats());

        GameOfLife gol2 = new GameOfLife(new InputGLD(new File("output/output.gld")), new ConwayRule());

        OutputAdaptater outPNG2 = new OutputPNG().init(Utils.cleanCreate("output/output2.png"), ei);
        OutputAdaptater outGLD2 = new OutputGLD().init(Utils.cleanCreate("output/output2.gld"), ei);

        gol.recordCurrentState(outPNG2);
        gol.recordCurrentState(outGLD2);

        outPNG2.finish();
        outGLD2.finish();

        System.out.println("Generated second fileset.");

        byte[] gld1 = Files.readAllBytes(new File("output/output.gld").toPath());
        byte[] gld2 = Files.readAllBytes(new File("output/output2.gld").toPath());

        assert Arrays.equals(gld1, gld2);

        System.out.println("Files are similar. OK");
    }
}
