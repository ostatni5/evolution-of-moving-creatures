package agh.ostatni5.eomc;

public class Jungle {
    WorldMap map;
    public Rectangle rectangle;
    MyRandom random = new MyRandom();
    Jungle(WorldMap map,Rectangle rectangle){
        this.map=map;
        this.rectangle = new Rectangle(rectangle);
    }

    public void growGrass() {
        Vector2d gPos = randomPosJungle();

        for (int j = gPos.y; j <= rectangle.corners[2].y; j++) {
            if (map.place(new Grass(new Vector2d(gPos.x, j)))) return;
            if (map.place(new Grass(randomPosJungle()))) return;
        }
        for (int i = gPos.x + 1; i <= rectangle.corners[2].x; i++) {
            for (int j = rectangle.corners[0].y; j <= rectangle.corners[2].y; j++) {
                if (map.place(new Grass(new Vector2d(i, j)))) return;
                if (map.place(new Grass(randomPosJungle()))) return;
            }
        }
        for (int i = rectangle.corners[0].x; i <= rectangle.corners[2].x; i++) {
            for (int j = rectangle.corners[0].y; j <= rectangle.corners[2].y; j++) {
                Vector2d newGpos = new Vector2d(i, j);
                if (newGpos.equals(gPos)) return;
                if (map.place(new Grass(new Vector2d(i, j)))) return;
                if (map.place(new Grass(randomPosJungle()))) return;
            }
        }
    }
    private Vector2d randomPosJungle() {
        return random.randomPos(rectangle.corners[0], rectangle.corners[2]);
    }
}
