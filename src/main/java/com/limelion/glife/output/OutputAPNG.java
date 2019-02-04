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
import com.vg.apng.APNG;
import com.vg.apng.Gray;

import java.io.*;
import java.util.ArrayList;

/**
 * Allows encoding multiple generations into a single APNG file.
 */
public class OutputAPNG extends OutputAdapter {

    private ArrayList<Gray> grays;

    public OutputAPNG(OutputParams op, OutputStream output) {

        super(op, output);
        grays = new ArrayList<>();
    }

    public OutputAPNG(OutputParams op, File output) throws FileNotFoundException {

        super(op, new FileOutputStream(output));
        grays = new ArrayList<>();
    }

    @Override
    public OutputAdapter feed(byte[] data) {

        grays.add(new Gray(op.getStateInfo().getWidth(), op.getStateInfo().getHeight(), Utils.copy(data), op.getDelay()));
        return this;
    }

    @Override
    public void close() throws IOException {

        APNG.write(grays.toArray(new Gray[grays.size()]), getOutput(), op.getRepeats());
        super.close();
    }
}
