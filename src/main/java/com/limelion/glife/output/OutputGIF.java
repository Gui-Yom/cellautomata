/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.glife.output;

import com.limelion.glife.utils.Utils;
import com.madgag.gif.fmsware.AnimatedGifEncoder;

import java.io.*;

public class OutputGIF extends OutputAdaptater {

    private AnimatedGifEncoder agife;

    @Override
    public OutputAdaptater init(OutputInfo oi, File output) throws FileNotFoundException {

        return init(oi, new FileOutputStream(output));
    }

    @Override
    public OutputAdaptater init(OutputInfo oi, OutputStream output) {

        super.init(oi, output);
        agife = new AnimatedGifEncoder();
        agife.setDelay(oi.getDelay());
        agife.setSize(oi.getMetadata().getWidth(), oi.getMetadata().getHeight());
        agife.setRepeat(oi.getRepeats());
        agife.start(output);
        return this;
    }

    @Override
    public OutputAdaptater feed(boolean[][] data) {

        if (isInited())
            agife.addFrame(Utils.createGrayImage(Utils.bool_to_gray(Utils.align(data)), oi.getMetadata().getWidth(), oi.getMetadata().getHeight()));
        else
            throw new IllegalStateException("Please init the OutputAdaptater first !");
        return this;
    }

    @Override
    public void finish() throws IOException {

        agife.finish();
        super.finish();
    }
}
