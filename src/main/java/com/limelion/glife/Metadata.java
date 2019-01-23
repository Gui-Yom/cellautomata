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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * A convenience class to exchange data
 */
public class Metadata {

    private int width;
    private int height;
    private int gen;
    private String rule;

    public Metadata() {

        width = 0;
        height = 0;
        rule = "";
        gen = 0;
    }

    public static byte[] serialize(Metadata m) {

        byte[] str = Utils.strtoba(m.rule);
        // Total length : 16 + rule descriptor
        return ByteBuffer.allocate(4 + str.length + 4 + 4).order(ByteOrder.BIG_ENDIAN)
                         .putInt(m.gen)
                         .put(str)
                         .putInt(m.width)
                         .putInt(m.height)
                         .array();
    }

    public static Metadata deserialize(byte[] serialized) {

        if (serialized != null && serialized.length >= 16) {
            ByteBuffer buf = ByteBuffer.wrap(serialized).order(ByteOrder.BIG_ENDIAN);
            Metadata m = new Metadata().setGen(buf.getInt());

            byte[] strdata = new byte[buf.getInt()];
            buf.get(strdata, 0, strdata.length);
            String s = new String(strdata, StandardCharsets.UTF_8);

            return m.setRule(s).setWidth(buf.getInt()).setHeight(buf.getInt());
        }
        // Unable to deserialize
        return null;
    }

    public int getWidth() {

        return width;
    }

    Metadata setWidth(int width) {

        this.width = width;
        return this;
    }

    public int getHeight() {

        return height;
    }

    Metadata setHeight(int height) {

        this.height = height;
        return this;
    }

    public int getGen() {

        return gen;
    }

    Metadata setGen(int gen) {

        this.gen = gen;
        return this;
    }

    Metadata incGen() {

        gen++;
        return this;
    }

    public String getRule() {

        return rule;
    }

    Metadata setRule(String rule) {

        this.rule = rule;
        return this;
    }

    public byte[] serialize() {

        return serialize(this);
    }

}
