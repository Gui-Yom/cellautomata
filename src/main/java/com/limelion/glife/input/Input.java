/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.glife.input;

import com.limelion.glife.GameOfLife;
import com.limelion.glife.Goldata;
import com.limelion.glife.universe.Universe2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;

/**
 * A convenience class to regroup the available input methods.
 */
public abstract class Input {

    /**
     * Read a GLD from the specified stream
     *
     * @param input
     *     the InputStream to read from
     *
     * @return the parsed GLD
     *
     * @throws IOException
     *     if there was an error while reading the stream
     * @see Goldata
     */
    public static Goldata fromGLD(InputStream input) throws IOException {

        return Goldata.parse(input);
    }

    /**
     * Read a GLD file
     *
     * @param f
     *     the file to read the GLD from
     *
     * @return the parsed GLD
     *
     * @throws IOException
     *     if there was an error while reading the file
     * @see Goldata
     */
    public static Goldata fromGLD(File f) throws IOException {

        return Goldata.parse(new FileInputStream(f));
    }

    /**
     * Work with either PNG or BMP
     *
     * @param is
     *     the InputStream to read the image from
     *
     * @return the cell data contained in the image
     *
     * @throws IOException
     *     if there was an error while reading the stream
     */
    public static byte[] fromImage(InputStream is) throws IOException {

        return fromImage(ImageIO.read(is));
    }

    /**
     * Work with either PNG or BMP
     *
     * @param f
     *     the File to read the image from
     *
     * @return the cell data contained in the image
     *
     * @throws IOException
     *     if there was an error while reading the file
     */
    public static byte[] fromImage(File f) throws IOException {

        return fromImage(new FileInputStream(f));
    }

    /**
     * Read an image from
     * @param buf
     * @return
     * @throws IOException
     */
    public static byte[] fromImage(byte[] buf) throws IOException {

        return fromImage(new ByteArrayInputStream(buf));
    }

    public static byte[] fromImage(BufferedImage bi) {

        Raster raster = bi.getData();
        int width = raster.getWidth();
        int height = raster.getHeight();
        byte[] data = new byte[width * height];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                data[i * width + j] = raster.getSample(i, j, 0) == 0 ? GameOfLife.ALIVE : GameOfLife.DEAD;

        return data;
    }
}
