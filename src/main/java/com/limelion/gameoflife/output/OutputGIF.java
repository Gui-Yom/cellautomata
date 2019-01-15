/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.gameoflife.output;

import com.limelion.gameoflife.Utils;
import com.madgag.gif.fmsware.AnimatedGifEncoder;

import java.io.*;

public class OutputGIF extends OutputAdaptater {

    private AnimatedGifEncoder agife;

    public OutputGIF() {

        super();
        agife = new AnimatedGifEncoder();
    }

    @Override
    public OutputAdaptater init(File output, EncodingInfo ei) throws FileNotFoundException {

        init(new FileOutputStream(output), ei);
        return this;
    }

    @Override
    public OutputAdaptater init(OutputStream output, EncodingInfo ei) {

        super.init(output, ei);
        agife.setDelay(ei.getDelay());
        agife.setSize(ei.getWidth(), ei.getHeight());
        agife.setRepeat(ei.getNumRepeats());
        agife.start(output);
        return this;
    }

    @Override
    public OutputAdaptater feed(byte[] data) {

        if (isInited())
            agife.addFrame(Utils.createGrayImage(data, ei.getWidth(), ei.getHeight()));
        else
            throw new IllegalStateException("Please init the OutputAdaptater first !");
        return this;
    }

    @Override
    public void finish() throws IOException {

        agife.finish();
        super.finish();
    }

    @Override
    public OutputType getType() {

        return OutputType.GIF;
    }
}
