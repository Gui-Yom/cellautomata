package com.limelion.gameoflife;

import java.io.File;
import java.io.IOException;

public class Utils {

    /**
     * Convert an array of booleans to an array of bytes.<br>
     * true -> 0 (black)<br>
     * false -> 255 (white)
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
        if (f.exists())
            f.delete();
        f.createNewFile();
        return f;
    }

    public static boolean checkCoords(int x, int y, int width, int height) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

}
