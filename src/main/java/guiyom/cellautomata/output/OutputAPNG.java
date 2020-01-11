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

import com.vg.apng.APNG;
import com.vg.apng.Gray;
import guiyom.cellautomata.AutomataState;
import guiyom.cellautomata.utils.Utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Allows encoding multiple generations into a single APNG file.
 */
public class OutputAPNG implements AutomataOutput, Closeable {

    private ArrayList<Gray> grays;
    private OutputStream output;
    private int repeats;
    private int delay;

    public OutputAPNG(OutputStream output, int delay, int repeats) {
        grays = new ArrayList<>();
        this.output = output;
        this.delay = delay;
        this.repeats = repeats;
    }

    @Override
    public void feed(AutomataState state) {
        grays.add(new Gray(state.getWidth(), state.getHeight(), Utils.copy(state.getCells()), delay));
    }

    @Override
    public void close() throws IOException {
        APNG.write(grays.toArray(new Gray[0]), output, repeats);
    }
}
