package com.limelion.gameoflife;

import de.gurkenlabs.litiengine.Game;

public class GameOfLife {

    public static void main(String[] args) {
        //HERE WE GO !
        Game.init();

        Game.getInfo().setVersion("0.1.0-dev");
        Game.getInfo().setName("Game Of Life");

        Game.start();
        Game.addGameTerminatedListener(() -> {
            System.out.println("Shutting down ...");
        });
    }

}
