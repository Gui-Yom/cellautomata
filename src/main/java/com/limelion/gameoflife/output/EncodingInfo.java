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

import java.util.concurrent.TimeUnit;

public class EncodingInfo {

    private int delay;
    private int width;
    private int height;
    private int numRepeats;
    private int numFrames;

    public EncodingInfo() {
        delay = 0;
    }

    public int getWidth() {
        return width;
    }

    public EncodingInfo setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public EncodingInfo setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getNumRepeats() {
        return numRepeats;
    }

    public EncodingInfo setNumRepeats(int numRepeats) {
        this.numRepeats = numRepeats;
        return this;
    }

    public int getNumFrames() {
        return numFrames;
    }

    public EncodingInfo setNumFrames(int numFrames) {
        this.numFrames = numFrames;
        return this;
    }

    public EncodingInfo setDelay(int time, TimeUnit timeUnit) {
        delay = (int) TimeUnit.MILLISECONDS.convert(time, timeUnit);
        return this;
    }

    public int getDelay() {
        return delay;
    }

}
