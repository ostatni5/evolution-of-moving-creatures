package agh.ostatni5.eomc.core;

import agh.ostatni5.eomc.my.MyRandom;

public class Savanna {
    private WorldMap map;
    public Rectangle rectangle;
    public Jungle jungle;
    public Savanna(WorldMap map, Jungle jungle, Rectangle rectangle) {
        this.map = map;
        this.jungle = jungle;
        this.rectangle = new Rectangle(rectangle);
    }

    public void growGrass() {
        Vector2d gPos = randomPosSavanna();
        for (int j = gPos.y; j <= rectangle.corners[2].y; j++) {
            Vector2d newGpos = new Vector2d(gPos.x, j);
            if (!jungle.rectangle.isIn(newGpos))
                if (map.place(new Grass(newGpos))) return;
            Vector2d newGposRand = randomPosSavanna();
            if (!jungle.rectangle.isIn(newGposRand))
                if (map.place(new Grass(newGposRand))) return;
        }
        for (int i = gPos.x + 1; i <= rectangle.corners[2].x; i++) {
            for (int j = rectangle.corners[0].y; j <= rectangle.corners[2].y; j++) {
                Vector2d newGpos = new Vector2d(i, j);
                if (!jungle.rectangle.isIn(newGpos))
                    if (map.place(new Grass(newGpos))) return;
                Vector2d newGposRand = randomPosSavanna();
                if (!jungle.rectangle.isIn(newGposRand))
                    if (map.place(new Grass(newGposRand))) return;
            }
        }
        for (int i = rectangle.corners[0].x; i <= rectangle.corners[2].x; i++) {
            for (int j = rectangle.corners[0].y; j <= rectangle.corners[2].y; j++) {
                Vector2d newGpos = new Vector2d(i, j);
                if (newGpos.equals(gPos)) return;
                if (!jungle.rectangle.isIn(newGpos))
                    if (map.place(new Grass(newGpos))) return;
                Vector2d newGposRand = randomPosSavanna();
                if (!jungle.rectangle.isIn(newGposRand))
                    if (map.place(new Grass(newGposRand))) return;
            }
        }

    }


    private Vector2d randomPosSavanna() {
        Vector2d pos = rectangle.randomIn();
        while (jungle.rectangle.isIn(pos)) pos = rectangle.randomIn();
        return pos;
    }
}
