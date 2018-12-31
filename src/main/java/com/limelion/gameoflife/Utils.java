/*
 * Copyright (c) 2018 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.gameoflife;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class Utils {

    /**
     * Convert an array of booleans to an array of bytes.<br>
     * true -&gt; 0 (black)<br>
     * false -&gt; 255 (white)
     * @param arr an array of booleans
     * @return an array of bytes
     */
    public static byte[] bool_to_gray(boolean[] arr) {

        byte[] out = new byte[arr.length];

        for (int i = 0; i < arr.length; i++)
            out[i] = arr[i] ? 0 : (byte) 255;

        return out;
    }

    public static boolean[] align(boolean[][] arr) {

        boolean[] out = new boolean[arr.length * arr[0].length];
        int pos = 0;

        for (int i = 0; i < arr.length; i++) {
            System.arraycopy(arr[i], 0, out, pos, arr[i].length);
            pos += arr[i].length;
        }

        return out;
    }

    public static int[] gray_to_rgb(byte[] arr) {

        int[] out = new int[arr.length * 3];

        for (int i = 0; i < arr.length; i++) {
            out[i] = arr[i];
            i++;
            out[i] = arr[i-1];
            i++;
            out[i] = arr[i-2];
        }

        return out;
    }

    public static File cleanCreate(String path) throws IOException {
        File f = new File(path);
        if (!f.exists())
            f.mkdirs();
        f.delete();
        f.createNewFile();
        return f;
    }

    public static boolean checkCoords(int x, int y, int width, int height) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public static BufferedImage createGrayImage(byte[] buffer, int width, int height) {
        ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                                                new int[] {8},
                                                false,
                                                true,
                                                Transparency.OPAQUE,
                                                DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm,
                                 Raster.createWritableRaster(
                                   cm.createCompatibleSampleModel(width, height),
                                   new DataBufferByte(buffer, buffer.length),
                                   null),
                                 false,
                                 null);
    }

}
