package agh.ostatni5.eomc;

import de.gurkenlabs.litiengine.Game;

public class Simulation {
    public static void main(String[] args) {
        WorldMap worldMap = new WorldMap(20,10,2,2);
        System.out.print(worldMap.toString());
//        Game.init();
//        Game.start();
    }
}
