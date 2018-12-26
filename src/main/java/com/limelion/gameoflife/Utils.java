package com.limelion.gameoflife;

import java.io.File;
import java.io.IOException;

public class Utils {

    public static byte[] batoba(boolean[] arr) {

        byte[] out = new byte[arr.length];

        for (int i = 0; i < arr.length; i++)
            out[i] = arr[i] ? (byte) 255 : 0;

        return out;
    }

    public static boolean[] align(boolean[][] arr) {

        // Cast is assumed to work
        boolean[] out = new boolean[arr.length * arr[0].length];
        int pos = 0;

        for (int i = 0; i < arr.length; i++) {
            System.arraycopy(arr[i], 0, out, pos, arr[i].length);
            pos += arr[i].length;
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

}
