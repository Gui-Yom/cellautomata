/*
 * Copyright (c) 2019 Gui-Yôm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.limelion.gameoflife.input;

import com.limelion.gameoflife.GameOfLife;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class InputGLD implements InputAdaptater {

    private ByteBuffer buf;

    public InputGLD(File f) throws IOException {

        buf = ByteBuffer.wrap(Files.readAllBytes(f.toPath())).order(ByteOrder.BIG_ENDIAN);
    }

    @Override
    public boolean[][] getBoardData() {

        for (int i = 0; i < GameOfLife.MAGIC.length; i++)
            if (buf.get() != GameOfLife.MAGIC[i])
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
                    assert decomp == data.length;
                }

                // Final data
                boolean[][] boardData = new boolean[width][height];

                for (int i = 0; i < width; i++)
                    for (int j = 0; j < height; j++)
                        boardData[i][j] = data[i * height + j] == 0;

                return boardData;
            default:
                return null;
        }
    }
}
