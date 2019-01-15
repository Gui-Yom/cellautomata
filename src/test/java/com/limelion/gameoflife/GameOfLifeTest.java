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

import com.limelion.gameoflife.output.EncodingInfo;
import com.limelion.gameoflife.output.OutputAdaptater;
import com.limelion.gameoflife.output.OutputType;
import com.limelion.gameoflife.rules.ConwayRule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GameOfLifeTest {

    @Test
    public void test() throws IOException {
        //HERE WE GO !
        GameOfLife gol = new GameOfLife(1000, new ConwayRule());

        //gol.getBoard().drawLine(20, 20, 20, 26);
        //gol.getBoard().drawLine(500, 500, 500, 800);
        gol.getBoard().drawLine(400, 500, 600, 500);

        EncodingInfo ei = new EncodingInfo()
            .setHeight(gol.getBoard().getHeight())
            .setWidth(gol.getBoard().getWidth())
            .setDelay(50, TimeUnit.MILLISECONDS)
            .setNumRepeats(0);

        //OutputAdaptater outAPNG = OutputType.APNG.getImpl().init(Utils.cleanCreate("output/output.apng"), ei);

        OutputAdaptater outGIF = OutputType.GIF.getImpl().init(Utils.cleanCreate("output/output.gif"), ei);

        //OutputAdaptater outPNG = OutputType.PNG.getImpl().init(Utils.cleanCreate("output/output.png"), ei);

        //OutputAdaptater outBMP = OutputType.BMP.getImpl().init(Utils.cleanCreate("output/output.bmp"), ei);

        //gol.recordSteps(outAPNG, 200, true);
        gol.recordSteps(outGIF, 1000, true);
        //gol.recordNextState(outPNG);
        //gol.recordNextState(outBMP);

        //outAPNG.finish();
        outGIF.finish();
        //outPNG.finish();
        //outBMP.finish();

        //gol.createhtmlplayer("output/playerAPNG.html", "output.apng");
        gol.createhtmlplayer("output/playerGIF.html", "output.gif");
        //gol.createhtmlplayer("output/playerPNG.html", "output.png");
        //gol.createhtmlplayer("output/playerBMP.html", "output.bmp");

        System.out.println(gol.getStats());
    }

}
