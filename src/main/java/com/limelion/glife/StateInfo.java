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
import java.time.Instant;

/**
 * A convenience class to exchange data
 */
public class StateInfo {

    // This order is kept at serialization
    private int width;
    private int height;
    private int gen;
    private String rule;
    private boolean bound;
    private String creator;
    private String comment;

    public StateInfo() {

        width = 0;
        height = 0;
        rule = "";
        gen = 0;
        bound = false;
        creator = "";
        comment = "";
    }

    public StateInfo(StateInfo sinf) {

        width = sinf.getWidth();
        height = sinf.getHeight();
        rule = sinf.getRule();
        gen = sinf.getGen();
        bound = sinf.isBound();
        creator = sinf.getCreator();
        comment = sinf.getComment();
    }

    public static byte[] serialize(StateInfo m) {

        byte[] ruleDesc = Utils.strtoba(m.rule);
        byte[] creator = Utils.strtoba(m.creator);
        byte[] comment = Utils.strtoba(m.comment);
        // Total length : 4 + 4 + 4 + (4 + str) + 1 + 8 + (4 + str) + (4 + str) >= 25
        return ByteBuffer.allocate(4 + 4 + 4 + 4 + ruleDesc.length + 1 + 4 + creator.length + 4 + comment.length).order(ByteOrder.BIG_ENDIAN)
                         .putInt(m.width)
                         .putInt(m.height)
                         .putInt(m.gen)
                         .put(ruleDesc)
                         .put((byte) (m.bound ? 1 : 0))
                         .put(creator)
                         .put(comment)
                         .array();
    }

    public static StateInfo deserialize(byte[] serialized) {

        if (serialized != null && serialized.length >= 25) {
            ByteBuffer buf = ByteBuffer.wrap(serialized).order(ByteOrder.BIG_ENDIAN);
            StateInfo sinf = new StateInfo()
                .setWidth(buf.getInt())
                .setHeight(buf.getInt())
                .setGen(buf.getInt());

            byte[] rule = new byte[buf.getInt()];
            buf.get(rule, 0, rule.length);
            sinf.setRule(new String(rule, StandardCharsets.UTF_8));

            sinf.setBound(buf.get() == 1);

            byte[] creator = new byte[buf.getInt()];
            buf.get(creator, 0, creator.length);
            sinf.setCreator(new String(creator, StandardCharsets.UTF_8));

            byte[] comment = new byte[buf.getInt()];
            buf.get(comment, 0, comment.length);
            sinf.setComment(new String(comment, StandardCharsets.UTF_8));

            return sinf;
        }
        // Unable to deserialize
        return null;
    }

    public boolean isBound() {

        return bound;
    }

    public StateInfo setBound(boolean bound) {

        this.bound = bound;
        return this;
    }

    public String getCreator() {

        return creator;
    }

    public StateInfo setCreator(String creator) {

        this.creator = creator;
        return this;
    }

    public String getComment() {

        return comment;
    }

    public StateInfo setComment(String comment) {

        this.comment = comment;
        return this;
    }

    public int getWidth() {

        return width;
    }

    public StateInfo setWidth(int width) {

        this.width = width;
        return this;
    }

    public int getHeight() {

        return height;
    }

    public StateInfo setHeight(int height) {

        this.height = height;
        return this;
    }

    public int getGen() {

        return gen;
    }

    public StateInfo setGen(int gen) {

        this.gen = gen;
        return this;
    }

    public StateInfo incGen() {

        gen++;
        return this;
    }

    public String getRule() {

        return rule;
    }

    public StateInfo setRule(String rule) {

        this.rule = rule;
        return this;
    }

    public byte[] serialize() {

        return serialize(this);
    }

}
