package com.limelion.gameoflife;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import java.awt.*;

public class MenuScreen extends GameScreen {

    public MenuScreen() {
        super("MENU");
    }

    @Override
    public void render(final Graphics2D g) {
        super.render(g);
        g.setFont(GameOfLife.baseFont);
        g.setColor(Color.RED);
        Game.getRenderEngine().renderText(g, "Conway's Game Of Life", 50, 50);
    }
}
