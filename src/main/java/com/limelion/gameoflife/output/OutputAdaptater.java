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

import java.io.*;

public abstract class OutputAdaptater {

    protected OutputStream output = null;
    protected EncodingInfo ei = null;
    protected boolean inited = false;
    protected boolean finished = false;

    public OutputAdaptater(OutputStream output, EncodingInfo ei) {

        init(output, ei);
    }

    public OutputAdaptater(File output, EncodingInfo ei) throws FileNotFoundException {

        this(new FileOutputStream(output), ei);
    }

    OutputAdaptater() {

    }

    public boolean isInited() {

        return inited;
    }

    public boolean isFinished() {

        return finished;
    }

    public OutputAdaptater init(OutputStream output, EncodingInfo ei) {

        this.output = output;
        this.ei = ei;
        this.inited = true;
        return this;
    }

    public OutputAdaptater init(File output, EncodingInfo ei) throws FileNotFoundException {

        return init(new FileOutputStream(output), ei);
    }

    public abstract OutputAdaptater feed(byte[] data) throws IOException;

    public void finish() throws IOException {

        output.close();
        this.finished = true;
    }

    public OutputStream getOutput() {

        return output;
    }

    public EncodingInfo getEncodingInfo() {

        return ei;
    }

    public abstract OutputType getType();
}
