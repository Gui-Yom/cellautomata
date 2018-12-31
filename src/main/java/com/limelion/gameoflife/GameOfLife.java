package com.limelion.gameoflife;

import com.limelion.gameoflife.rules.ConwayRule;
import com.limelion.gameoflife.rules.Rule;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.vg.apng.APNG;
import com.vg.apng.Gray;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.io.PrintWriter;

public class GameOfLife {

    public static Font baseFont = new Font("Segoe UI", Font.PLAIN, 16);

    private Board board;
    private Rule rule;

    public GameOfLife() {
        board = new Board(50);
        rule = new ConwayRule();
    }

    public static void main(String[] args) throws IOException {
        //HERE WE GO !
        long initDuration = System.currentTimeMillis();

        GameOfLife gol = new GameOfLife();

        System.out.printf("Init duration : %d ms %n", System.currentTimeMillis() - initDuration);

        gol.getBoard().drawLine(20, 20, 20, 26);

        //ImageIO.write(gol.getImage(), "png", Utils.cleanCreate("initial.png"));

        gol.createGIF(50, "output.gif");
        gol.createhtmlplayer("player.html", "output.gif");

        System.out.printf("Total computation time : %d ms %n", System.currentTimeMillis() - initDuration);

    }

    public Board getBoard() {
        return board;
    }

    public void nextStep() {
        boolean[][] newBoard = getBoard().copyShape();
        for (int i = 0; i < getBoard().getWidth(); i++)
            for (int j = 0; j < getBoard().getHeight(); j++) {
                newBoard[i][j] = rule.apply(getBoard().getCell(i, j), board.countNeighbours(i, j));
            }
        board = new Board(newBoard);
    }

    public void nextStep(int steps) {
        for (int i = 0; i < steps; i++)
            nextStep();
    }

    public void nextStep(int steps, OutputAdaptater recorder) {

    }

    public BufferedImage getImage() {
        byte[] buffer = Utils.bool_to_gray(Utils.align(getBoard().getCells()));
        ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY),
                                                new int[] {8},
                                                false,
                                                true,
                                                Transparency.OPAQUE,
                                                DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm,
                                 Raster.createWritableRaster(
                                   cm.createCompatibleSampleModel(getBoard().getWidth(), getBoard().getHeight()),
                                   new DataBufferByte(buffer, buffer.length),
                                   null),
                                 false,
                                 null);
    }

    public void createAPNG(int steps, String path) throws IOException {

        if (steps <= 0) {
            throw new IllegalArgumentException("The number of steps to record must be strictly positive !");
        }

        Gray[] pngs = new Gray[steps];

        for (int i = 0; i < steps; i++) {
            pngs[i] = new Gray(getBoard().getWidth(),
                               getBoard().getHeight(),
                               Utils.bool_to_gray(Utils.align(getBoard().getCells())));
            nextStep();
        }

        APNG.write(pngs, Utils.cleanCreate(path));
    }

    public void createGIF(int steps, String path) throws IOException {
        AnimatedGifEncoder agife = new AnimatedGifEncoder();
        agife.setDelay(400);
        agife.setSize(getBoard().getWidth(), getBoard().getHeight());
        agife.setRepeat(0);
        agife.start(path);

        for (int i = 0; i < steps; i++) {
            agife.addFrame(getImage());
            nextStep();
        }

        agife.finish();
    }

    public void createhtmlplayer(String path, String animationPath) throws IOException {
        PrintWriter pw = new PrintWriter(Utils.cleanCreate(path));
        pw.printf("<!DOCTYPE html>%n<html>%n  <head>%n    <title>%s</title>%n  </head>%n  <body>%n    <img src=\"%s\">%n  </body>%n</html>%n", animationPath, animationPath);
        pw.close();
    }
}
