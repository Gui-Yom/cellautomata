/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.gameoflife;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipException;

public class Goldata {

    public static byte[] MAGIC = new byte[] { 0x47, 0x4C, 0x44 };
    public static byte VERSION = 0x02;

    private Metadata metad;
    private boolean[][] data;

    private Goldata(Metadata m, boolean[][] data) {

        this.metad = m;
        this.data = data;
    }

    public static Goldata create(Metadata metad, boolean[][] data) {

        return new Goldata(metad, data);
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

                // Final data
                boolean[][] boardData = new boolean[width][height];

                for (int i = 0; i < width; i++)
                    for (int j = 0; j < height; j++)
                        boardData[i][j] = data[i * height + j] == 0;

                return Goldata.create(new Metadata().setWidth(width).setHeight(height), boardData);
            case 0x02:

                byte[] serialized = new byte[buf.getInt()];
                buf.get(serialized, 0, serialized.length);
                Metadata m = Metadata.deserialize(serialized);
                data = new byte[m.getWidth() * m.getHeight()];

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

                // Final data
                boardData = new boolean[m.getWidth()][m.getHeight()];

                for (int i = 0; i < m.getWidth(); i++)
                    for (int j = 0; j < m.getHeight(); j++)
                        boardData[i][j] = data[i * m.getHeight() + j] == 0;

                return Goldata.create(m, boardData);
            default:
                return null;
        }
    }

    public void write(OutputStream os) throws IOException {

        os.write(MAGIC);
        os.write(VERSION);
        byte[] serialized = metad.serialize();
        os.write(Utils.itoba(serialized.length));
        os.write(serialized);

        // Zip compress board data
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(Utils.bool_to_gray(Utils.align(data)));
        deflater.finish();

        byte[] buf = new byte[metad.getHeight() * metad.getWidth()];
        int comp = deflater.deflate(buf);
        deflater.end();

        if (comp == 0)
            throw new ZipException("Failed to compress data in GLD file.");

        // Finally write to file
        os.write(buf, 0, comp);
    }

    public boolean[][] getData() {

        return data;
    }

    public Metadata getMetadata() {

        return metad;
    }

}
