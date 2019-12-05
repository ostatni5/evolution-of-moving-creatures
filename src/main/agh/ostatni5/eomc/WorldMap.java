package agh.ostatni5.eomc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class WorldMap {
    MapVisualizer mapVisualizer = new MapVisualizer(this);
    Random random = new Random();
    Rectangle savannaRec;
    Rectangle jungleRec;
    int elementsJungle;
    int elementsSavanna;
    int maxElementsJungle;
    int maxElementsSavanna;
    Vector2d mapStartVector = new Vector2d(0, 0);
    Vector2d jungleStartVector = new Vector2d(0, 0);
    HashMap<Vector2d, Grass> grassMap = new HashMap<>();
    HashMap<Vector2d, LinkedList<Creature>> creaturesMap = new HashMap<>();

    WorldMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight) {
        jungleStartVector = mapStartVector.add(new Vector2d((mapWidth - jungleWidth) / 2, (mapHeight - jungleHeight) / 2));
        savannaRec = new Rectangle(mapWidth, mapHeight, mapStartVector);
        jungleRec = new Rectangle(jungleWidth, jungleHeight, jungleStartVector);
        maxElementsJungle = jungleHeight * jungleWidth;
        maxElementsSavanna = mapHeight * mapWidth - maxElementsJungle;
        generateInitialGrass(10);
    }


    public IMapElement objectAt(Vector2d v) {
        return grassMap.get(v);
    }

    public boolean place(Grass grass) {
        if (objectAt(grass.position) != null) return false;
        grassMap.put(grass.position, grass);
        return true;
    }

    private void growGrassSavanna() {
        Vector2d gPos = randomPosSavanna();
        for (int i = gPos.x; i <= savannaRec.corners[2].x; i++) {
            for (int j = gPos.y; j <= savannaRec.corners[2].y; j++) {
                Vector2d newGpos = new Vector2d(i, j);
                if (!jungleRec.isIn(newGpos))
                    if (place(new Grass(newGpos))) return;
            }
        }
        for (int i = savannaRec.corners[0].x; i <= savannaRec.corners[2].x; i++) {
            for (int j = savannaRec.corners[0].y; j <= savannaRec.corners[2].y; j++) {
                Vector2d newGpos = new Vector2d(i, j);
                if (!newGpos.equals(gPos))
                    if (!jungleRec.isIn(newGpos))
                        if (place(new Grass(new Vector2d(i, j)))) return;
            }
        }
    }

    private void growGrassJungle() {
        Vector2d gPos = randomPosJungle();
        for (int i = gPos.x; i <= jungleRec.corners[2].x; i++) {
            for (int j = gPos.y; j <= jungleRec.corners[2].y; j++) {
                if (place(new Grass(new Vector2d(i, j)))) return;
            }
        }
        for (int i = jungleRec.corners[0].x; i <= jungleRec.corners[2].x; i++) {
            for (int j = jungleRec.corners[0].y; j <= jungleRec.corners[2].y; j++) {
                Vector2d newGpos = new Vector2d(i, j);
                if (!newGpos.equals(gPos))
                    if (place(new Grass(new Vector2d(i, j)))) return;
            }
        }
    }

    public void generateInitialGrass(int a) {
        for (int i = 0; i < a; i++) {
            growGrassJungle();
            growGrassSavanna();
        }
    }

    private Vector2d randomPosJungle() {
        return randomPos(jungleRec.corners[0], jungleRec.corners[2]);
    }

    private Vector2d randomPosSavanna() {
        Vector2d pos = randomPos(savannaRec.corners[0], savannaRec.corners[2]);
        while (jungleRec.isIn(pos)) pos = randomPos(savannaRec.corners[0], savannaRec.corners[2]);
        return pos;
    }

    private Vector2d randomPos(Vector2d lowerV, Vector2d higherV) {
        Vector2d area = higherV.subtract(lowerV);
        int x = random.nextInt(area.x + 1);
        int y = random.nextInt(area.y + 1);
        Vector2d randPos = new Vector2d(x, y);
        randPos = randPos.add(lowerV);
        return randPos;
    }

    public Biome biomeAt(Vector2d v) {
        return jungleRec.isIn(v) ? Biome.JUNGLE : Biome.SAVANNA;
    }

    @Override
    public String toString() {
        return mapVisualizer.draw(savannaRec.corners[0], savannaRec.corners[2]);
    }

    //    private void growGrassSavanna(){
//        if(elementsSavanna==maxElementsSavanna) return;
//        Grass grass = new Grass(randomPosSavanna());
//        while (!place(grass))
//        {
//            grass = new Grass(randomPosSavanna());
//        }
//        elementsSavanna++;
//    }
//
//    private void growGrassJungle(){
//        if(elementsJungle==maxElementsJungle) return;
//        Grass grass = new Grass(randomPosJungle());
//        while (!place(grass))
//        {
//            grass = new Grass(randomPosJungle());
//        }
//        elementsJungle++;
//    }
}
