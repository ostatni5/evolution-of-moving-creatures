package agh.ostatni5.eomc;

public class Savanna {
    WorldMap map;
    Rectangle rectangle;
    Jungle jungle;
    MyRandom random = new MyRandom();

    Savanna(WorldMap map, Jungle jungle, Rectangle rectangle) {
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
        }
        for (int i = gPos.x + 1; i <= rectangle.corners[2].x; i++) {
            for (int j = rectangle.corners[0].y; j <= rectangle.corners[2].y; j++) {
                Vector2d newGpos = new Vector2d(i, j);
                if (!jungle.rectangle.isIn(newGpos))
                    if (map.place(new Grass(newGpos))) return;
            }
        }
        for (int i = rectangle.corners[0].x; i <= rectangle.corners[2].x; i++) {
            for (int j = rectangle.corners[0].y; j <= rectangle.corners[2].y; j++) {
                Vector2d newGpos = new Vector2d(i, j);
                if (newGpos.equals(gPos)) return;
                if (!jungle.rectangle.isIn(newGpos))
                    if (map.place(new Grass(newGpos))) return;
            }
        }

    }


    private Vector2d randomPosSavanna() {
        Vector2d pos = random.randomPos(rectangle.corners[0], rectangle.corners[2]);
        while (jungle.rectangle.isIn(pos)) pos = random.randomPos(rectangle.corners[0], rectangle.corners[2]);
        return pos;
    }
}
