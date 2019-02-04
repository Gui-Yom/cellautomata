/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.glife;

import com.limelion.glife.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipException;

/**
 * Represents the file format to save the current game instance.
 * It holds enough information to completely save the computation state and restore it later.
 */
public class Goldata {

    public static byte[] MAGIC = new byte[] { 0x47, 0x4C, 0x44 };
    public static byte VERSION = 0x03;

    private StateInfo sinf;
    private byte[] data;

    private Goldata(StateInfo sinf, byte[] data) {

        this.sinf = sinf;
        this.data = data;
    }

    public static Goldata create(StateInfo sinf, byte[] data) {

        return new Goldata(sinf, data);
    }

    public static Goldata parse(InputStream is) throws IOException {

        ByteBuffer buf = ByteBuffer.wrap(Utils.readAllBytes(is)).order(ByteOrder.BIG_ENDIAN);

        for (int i = 0; i < MAGIC.length; i++)
            if (buf.get() != MAGIC[i])
                return null;

        switch (buf.get()) {
            case 0x01:
                int width = buf.getInt();
                int height = buf.getInt();
                byte[] data = new byte[width * height];

                // First pass
                Inflater inflater = new Inflater();
                inflater.setInput(buf.array(), buf.position(), buf.array().length - buf.position());

                int decomp = 0;
                try {
                    decomp = inflater.inflate(data);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                } finally {
                    inflater.end();
                }

                if (decomp != data.length)
                    throw new ZipException("Failed to decompress data correctly.");

                return Goldata.create(new StateInfo().setWidth(width).setHeight(height), data);

            case 0x03:
            case 0x02:

                byte[] serialized = new byte[buf.getInt()];
                buf.get(serialized, 0, serialized.length);
                StateInfo sinf = StateInfo.deserialize(serialized);
                data = new byte[sinf.getWidth() * sinf.getHeight()];

                // First pass
                inflater = new Inflater();
                inflater.setInput(buf.array(), buf.position(), buf.array().length - buf.position());

                decomp = 0;
                try {
                    decomp = inflater.inflate(data);
                } catch (DataFormatException e) {
                    e.printStackTrace();
                } finally {
                    inflater.end();
                }

                if (decomp != data.length)
                    throw new ZipException("Failed to decompress data correctly.");

                return Goldata.create(sinf, data);

            default:
                return null;
        }
    }

    public void write(OutputStream os) throws IOException {

        os.write(MAGIC);
        os.write(VERSION);
        byte[] serialized = sinf.serialize();
        os.write(Utils.itoba(serialized.length));
        os.write(serialized);

        // Zip compress board data
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        byte[] buf = new byte[sinf.getHeight() * sinf.getWidth()];
        int comp = deflater.deflate(buf);
        deflater.end();

        if (comp == 0)
            throw new ZipException("Failed to compress data in GLD file.");

        // Finally write to file
        os.write(buf, 0, comp);
    }

    public byte[] getData() {

        return data;
    }

    public StateInfo getStateInfo() {

        return sinf;
    }

}
