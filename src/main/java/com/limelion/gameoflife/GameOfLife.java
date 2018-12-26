package com.limelion.gameoflife;

import com.limelion.gameoflife.rules.ConwayRule;
import com.limelion.gameoflife.rules.Rule;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

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
        /*
        Game.init();

        Game.getInfo().setVersion("0.1.0-dev");
        Game.getInfo().setName("Game Of Life");

        Game.getScreenManager().setResolution(Resolution.Ratio16x9.RES_1280x720);
        Game.getScreenManager().addScreen(new MenuScreen());
        Game.getScreenManager().displayScreen("MENU");

        Game.addGameTerminatedListener(() -> {
            System.out.println("Shutting down ...");
        });

        Resources.setEncoding(Resources.ENCODING_UTF_8);

        GuiProperties.setDefaultFont(baseFont);

        Game.getConfiguration().input().setGamepadSupport(false);

        System.out.println("Starting game loop");

        Game.start();
        */

        GameOfLife gol = new GameOfLife();

        System.out.printf("Init duration : %d ms %n", System.currentTimeMillis() - initDuration);

        gol.getBoard().switchCell(10, 10);
        gol.getBoard().switchCell(10, 11);
        gol.getBoard().switchCell(10, 12);

        gol.nextStep();
        ImageIO.write(gol.getImage(), "png", Utils.cleanCreate("output.png"));

        gol.nextStep();
        ImageIO.write(gol.getImage(), "png", Utils.cleanCreate("output2.png"));

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

    public BufferedImage getImage() {
        byte[] buffer = Utils.batoba(Utils.align(getBoard().getCells()));
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
}
