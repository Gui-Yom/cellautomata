/*
 * Copyright (c) 2018 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.glife.utils;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * An utility class containing some utility methods.
 */
public class Utils {

    /**
     * Cleanly create a File from a path. e.g. checks if it exists and/or if the parent directory exists.
     * @param path where to create the new file
     * @return the File just created
     * @throws IOException if the file manipulations failed
     */
    public static File cleanCreate(String path) throws IOException {

        File f = new File(path);
        if (!f.exists())
            f.mkdirs();
        f.delete();
        f.createNewFile();
        return f;
    }

    public static BufferedImage createGrayImage(byte[] buffer, int width, int height) {

        ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                                                new int[] { 1 },
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

    /**
     * Serialize an int to a new byte array
     * @param i the int to serialize
     * @return a new byte array containing the serialized int
     */
    public static byte[] itoba(int i) {

        return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(i).array();
    }

    public static int batoi(byte[] arr, int index) {

        return ByteBuffer.wrap(arr).order(ByteOrder.BIG_ENDIAN).getInt(index);
    }

    /**
     * @param s the String to serialize
     * @return the serialized String
     */
    public static byte[] strtoba(String s) {

        byte[] strdata = s.getBytes(StandardCharsets.UTF_8);
        return ByteBuffer.allocate(4 + strdata.length).order(ByteOrder.BIG_ENDIAN).putInt(strdata.length).put(strdata).array();
    }

    /**
     * Read all bytes from the given InputStream.
     * @param is the InputStream to read
     * @return an array containing all bytes.
     * @throws IOException if it failed reading the stream
     */
    public static byte[] readAllBytes(InputStream is) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            baos.write(buffer, 0, len);
        }
        return baos.toByteArray();
    }

    /**
     * Creates a little player (html file). Useful for GIF or APNG files that can only be read in a browser.
     * @param path the path where the player need to be created
     * @param animationPath the relative path to the animation from the player
     * @throws IOException if there was an error creating the file
     */
    public static void createhtmlplayer(String path, String animationPath) throws IOException {

        PrintWriter pw = new PrintWriter(Utils.cleanCreate(path));
        pw.printf("<!DOCTYPE html>%n<html>%n  <head>%n    <title>%s</title>%n  </head>%n  <body>%n    <img src=\"%s\" style=\"border:1px solid red\">%n  </body>%n</html>%n", animationPath, animationPath);
        pw.close();
    }

    /**
     * Fills the given array with the specified byte.
     * @param arr the array to fill
     * @param b the byte to fill the array with
     * @return the filled array (same ref)
     */
    public static byte[] fill(byte[] arr, byte b) {
        for (int i = 0; i < arr.length; i++)
            arr[i] = b;
        return arr;
    }

    public static int[] batoia(byte[] arr) {
        int[] out = new int[arr.length];
        for (int i = 0; i < out.length; i++)
            out[i] = arr[i];
        return out;
    }

    public static byte[] align(byte[][] arr) {
        int length = 0;
        for (int i = 0; i < arr.length; i++)
            length += arr[i].length;
        byte[] out = new byte[length];
        for (int i = 0; i < arr.length; i++)
            System.arraycopy(arr[i], 0, out, length -= arr[i].length, arr[i].length);
        return out;
    }

    /**
     * @param arr the array to copy
     * @return a new similar array
     */
    public static byte[] copy(byte[] arr) {

        byte[] out = new byte[arr.length];
        System.arraycopy(arr, 0, out, 0, arr.length);
        return out;
    }
}
