/*
 * Copyright (c) 2018 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.glife.output;

import java.io.*;

public abstract class OutputAdaptater {

    protected OutputStream output = null;
    protected OutputInfo oi = null;

    protected boolean inited = false;
    protected boolean finished = false;

    public OutputAdaptater(OutputInfo oi, OutputStream output) {

        init(oi, output);
    }

    public OutputAdaptater(OutputInfo oi, File f) throws FileNotFoundException {

        this(oi, new FileOutputStream(f));
    }

    public OutputAdaptater(OutputInfo oi) {

        init(oi);
    }

    protected OutputAdaptater() {

    }

    public boolean isInited() {

        return inited;
    }

    public boolean isFinished() {

        return finished;
    }

    public OutputAdaptater init(OutputInfo oi, OutputStream output) {

        this.output = output;
        this.oi = oi;
        this.inited = true;
        return this;
    }

    public OutputAdaptater init(OutputInfo oi, File f) throws FileNotFoundException {

        return init(oi, new FileOutputStream(f));
    }

    public OutputAdaptater init(OutputInfo oi) {

        this.oi = oi;
        this.inited = true;
        return this;
    }

    public abstract OutputAdaptater feed(boolean[][] data) throws IOException;

    public void finish() throws IOException {

        if (output != null)
            output.close();
        this.finished = true;
    }

    public OutputStream getOutput() {

        return output;
    }

    public OutputInfo getInfo() {

        return oi;
    }
}
