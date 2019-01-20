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

/**
 * Specify the format to encode the output. Get the encoder implementation with {@code OutputType.getImpl()}.
 */
public enum OutputType {
    PNG,
    BMP,
    APNG,
    GIF,
    GLD;

    public String getFileExtension() {
        return "." + this.name().toLowerCase();
    }

    /**
     * @return if the format is an animated one. WARNING : basically, png and apng are the same but here we differentiate them.
     */
    public boolean isAnimated() {
        switch (this) {

            case GIF:
            case APNG:
                return true;

            case PNG:
            case BMP:
            case GLD:
            default:
                return false;
        }
    }

    /**
     * @return an initialized encoder implementation fo the given format.
     */
    public OutputAdaptater getImpl() {
        switch (this) {
            case PNG:
                return new OutputPNG();
            case BMP:
                return new OutputBMP();
            case APNG:
                return new OutputAPNG();
            case GIF:
                return new OutputGIF();
            case GLD:
                return new OutputGLD();
            default:
                return null;
        }
    }
}
