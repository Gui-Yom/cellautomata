/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package guiyom.cellautomata.output;

import guiyom.cellautomata.AutomataState;
import guiyom.cellautomata.utils.Utils;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Allows encoding the GameOfLife states into PNG files.
 */
public class OutputPNG implements AutomataOutput {

    private int numFiles = 0;
    private OutputStream output;
    private String baseFileName;

    public OutputPNG(OutputStream output) {

        this.output = output;
    }

    public OutputPNG(String baseFileName) {

        this.baseFileName = baseFileName;
    }

    @Override
    public void feed(AutomataState state) {
        try {
            if (output != null) {

                ImageIO.write(
                    Utils.createGrayImage(
                        state.getCells(),
                        state.getWidth(),
                        state.getHeight()),
                    "png",
                    output);

            } else {

                String fname = baseFileName.replace("@gen@", String.valueOf(state.getGen()));
                fname = fname.replace("@num@", String.valueOf(numFiles++));

                ImageIO.write(
                    Utils.createGrayImage(
                        state.getCells(),
                        state.getWidth(),
                        state.getHeight()),
                    "png",
                    Utils.cleanCreate(fname));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
