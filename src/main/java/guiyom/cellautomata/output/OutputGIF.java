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

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import guiyom.cellautomata.AutomataState;
import guiyom.cellautomata.utils.Utils;

import java.io.Closeable;
import java.io.OutputStream;

/**
 * Allow encoding multiple generations into a single GIF file.
 */
public class OutputGIF implements Closeable, AutomataOutput {

    private AnimatedGifEncoder agife;
    private OutputStream os;

    public OutputGIF(OutputStream output, int delay, int repeats) {

        agife = new AnimatedGifEncoder();
        agife.setDelay(delay);
        agife.setRepeat(repeats);
        agife.setQuality(256);
        agife.start(output);
    }

    @Override
    public void feed(AutomataState state) {

        agife.addFrame(Utils.createGrayImage(state.getCells(), state.getWidth(), state.getHeight()));
    }

    public void close() {

        agife.finish();
    }
}
