/*
 * Copyright (c) 2018 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.gameoflife.output;

import com.limelion.gameoflife.Utils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Allows encoding the GameOfLife states into BMP files.
 */
public class OutputBMP extends OutputAdaptater {

    private int numFiles = 0;

    @Override
    public OutputAdaptater feed(boolean[][] data) throws IOException {

        if (isInited())
            if (output != null)
                ImageIO.write(
                    Utils.createGrayImage(
                        Utils.bool_to_gray(Utils.align(data)),
                        oi.getMetadata().getWidth(),
                        oi.getMetadata().getHeight()),
                    "bmp",
                    output);
            else {

                String fname = oi.getBaseFileName();
                fname = fname.replace("@gen@", String.valueOf(oi.getMetadata().getGen()));
                fname = fname.replace("@num@", String.valueOf(numFiles++));

                ImageIO.write(
                    Utils.createGrayImage(
                        Utils.bool_to_gray(Utils.align(data)),
                        oi.getMetadata().getWidth(),
                        oi.getMetadata().getHeight()),
                    "bmp",
                    new File(fname));
            }
        else
            throw new IllegalStateException("Please init the OutputAdaptater first !");

        return this;
    }
}
