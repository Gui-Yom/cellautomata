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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Allow encoding multiple generations into a single GIF file.
 */
public class OutputGIF extends OutputAdapter {

    private AnimatedGifEncoder agife;

    public OutputGIF(OutputParams op, OutputStream output) {

        super(op, output);
        agife = new AnimatedGifEncoder();
        agife.setDelay(op.getDelay());
        agife.setSize(op.getStateInfo().getWidth(), op.getStateInfo().getHeight());
        agife.setRepeat(op.getRepeats());
        agife.setQuality(128);
        agife.start(output);
    }

    public OutputGIF(OutputParams op, File f) throws FileNotFoundException {

        super(op, f);
        agife = new AnimatedGifEncoder();
        agife.setDelay(op.getDelay());
        agife.setSize(op.getStateInfo().getWidth(), op.getStateInfo().getHeight());
        agife.setRepeat(op.getRepeats());
        agife.setQuality(128);
        agife.start(output);
    }

    @Override
    public OutputAdapter feed(byte[] data) {

        agife.addFrame(Utils.createGrayImage(data, op.getStateInfo().getWidth(), op.getStateInfo().getHeight()));
        return this;
    }

    @Override
    public void close() throws IOException {

        agife.finish();
        super.close();
    }
}
