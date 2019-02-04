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

/**
 * Represent a way to record a generation.
 */
public abstract class OutputAdapter implements Closeable {

    protected OutputStream output = null;
    protected OutputParams op = null;

    protected boolean isClosed = false;

    public OutputAdapter(OutputParams op, OutputStream output) {

        if (output == null)
            throw new IllegalArgumentException("OutputStream cannot be null");

        this.op = op;
        this.output = output;
    }

    public OutputAdapter(OutputParams op, File f) throws FileNotFoundException {

        this(op, new FileOutputStream(f));
    }

    public OutputAdapter(OutputParams op) {

        this.op = op;
        // The output stream will be built in the feed(byte[]) implementation
        // making it possible to handle multiple files
    }

    public boolean isClosed() {

        return isClosed;
    }

    public abstract OutputAdapter feed(byte[] data) throws IOException;

    @Override
    public void close() throws IOException {

        if (output != null)
            output.close();
        this.isClosed = true;
    }

    public OutputStream getOutput() {

        return output;
    }

    public OutputParams getOutputInfo() {

        return op;
    }
}
