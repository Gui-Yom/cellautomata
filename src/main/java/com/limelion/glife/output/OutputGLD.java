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

import com.limelion.glife.Goldata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Allows encoding a complete Game of Life state in a GLD file.
 * The GLD format holds all the information to completely save the current compute state and restore it later.
 */
public class OutputGLD extends OutputAdapter {

    public OutputGLD(OutputParams op, OutputStream output) {

        super(op, output);
    }

    public OutputGLD(OutputParams op, File f) throws FileNotFoundException {

        super(op, f);
    }

    public OutputGLD(OutputParams op) {

        super(op);
    }

    @Override
    public OutputAdapter feed(byte[] data) throws IOException {

        Goldata.create(op.getStateInfo(), data).write(output);
        return this;
    }
}
