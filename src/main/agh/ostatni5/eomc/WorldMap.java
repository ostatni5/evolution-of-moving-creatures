package agh.ostatni5.eomc;

import java.util.*;

public class WorldMap {
    MapVisualizer mapVisualizer = new MapVisualizer(this);
    Random random = new Random();
    Rectangle mapRec;
    Rectangle savannaRec;
    Rectangle jungleRec;

    int maxEnergy=256;
    int initialEnergy=256;
    int dayEnergyCost;
    int startEnergy;
    int grassEnergy;

    Vector2d mapStartVector = new Vector2d(0, 0);
    Vector2d jungleStartVector = new Vector2d(0, 0);
    HashMap<Vector2d, Grass> grassMap = new HashMap<>();
    HashMap<Vector2d, TreeSet<Creature>> creaturesMap = new HashMap<>();

    WorldMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight) {
        jungleStartVector = mapStartVector.add(new Vector2d((mapWidth - jungleWidth) / 2, (mapHeight - jungleHeight) / 2));
        savannaRec = new Rectangle(mapWidth, mapHeight, mapStartVector);
        jungleRec = new Rectangle(jungleWidth, jungleHeight, jungleStartVector);
        mapRec = savannaRec;
        generateInitialCreatures(10);
        generateInitialGrass(10);
    }


    public IMapElement visibleAt(Vector2d v) {
        return grassMap.get(v) != null ? grassMap.get(v) : creaturesMap.get(v) != null ? (IMapElement) creaturesMap.get(v).first() : null;
    }

    public Grass grassAt(Vector2d v) {
        return grassMap.get(v);
    }

    public TreeSet<Creature> creaturesAt(Vector2d v) {
        return creaturesMap.get(v);
    }


    public boolean place(Grass grass) {
        if (visibleAt(grass.position) != null) return false;
        grassMap.put(grass.position, grass);
        return true;
    }
    public boolean place(Creature creature) {
        if (visibleAt(creature.position) != null) return false;
        if(creaturesMap.get(creature.position)==null);
        {
            creaturesMap.put(creature.position,new TreeSet<Creature>(new ComparatorEnergy()));
        }
        creaturesMap.get(creature.position).add(creature);
        return true;
    }

    private void growGrassSavanna() {
        Vector2d gPos = randomPosSavanna();
        for (int j = gPos.y; j <= savannaRec.corners[2].y; j++) {
            if (place(new Grass(new Vector2d(gPos.x, j)))) return;
        }
        for (int i = gPos.x + 1; i <= savannaRec.corners[2].x; i++) {
            for (int j = savannaRec.corners[0].y; j <= savannaRec.corners[2].y; j++) {
                Vector2d newGpos = new Vector2d(i, j);
                if (!jungleRec.isIn(newGpos))
                    if (place(new Grass(newGpos))) return;
            }
        }
        for (int i = savannaRec.corners[0].x; i <= savannaRec.corners[2].x; i++) {
            for (int j = savannaRec.corners[0].y; j <= savannaRec.corners[2].y; j++) {
                Vector2d newGpos = new Vector2d(i, j);
                if (newGpos.equals(gPos)) return;
                if (!jungleRec.isIn(newGpos))
                    if (place(new Grass(new Vector2d(i, j)))) return;
            }
        }
    }

    private void growGrassJungle() {
        Vector2d gPos = randomPosJungle();
        for (int j = gPos.y; j <= jungleRec.corners[2].y; j++) {
            if (place(new Grass(new Vector2d(gPos.x, j)))) return;
        }
        for (int i = gPos.x + 1; i <= jungleRec.corners[2].x; i++) {
            for (int j = jungleRec.corners[0].y; j <= jungleRec.corners[2].y; j++) {
                if (place(new Grass(new Vector2d(i, j)))) return;
            }
        }
        for (int i = jungleRec.corners[0].x; i <= jungleRec.corners[2].x; i++) {
            for (int j = jungleRec.corners[0].y; j <= jungleRec.corners[2].y; j++) {
                Vector2d newGpos = new Vector2d(i, j);
                if (newGpos.equals(gPos)) return;
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
    public void generateInitialCreatures(int a) {
        for (int i = 0; i < a; i++) {
            Creature creature = new Creature(this,randomPos(mapRec.corners[0],mapRec.corners[2]),maxEnergy,initialEnergy);
            place(creature);
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

    public Vector2d correctPos(Vector2d vector2d) {
        int x = vector2d.x < mapRec.corners[0].x ? mapRec.corners[1].x : vector2d.x > mapRec.corners[1].x ? mapRec.corners[0].x : vector2d.x;
        int y = vector2d.y < mapRec.corners[0].y ? mapRec.corners[2].y : vector2d.y > mapRec.corners[2].y ? mapRec.corners[0].y : vector2d.y;
        return new Vector2d(x, y);
    }

    public Biome biomeAt(Vector2d v) {
        return jungleRec.isIn(v) ? Biome.JUNGLE : Biome.SAVANNA;
    }

    @Override
    public String toString() {
        return mapVisualizer.draw(savannaRec.corners[0], savannaRec.corners[2]);
    }

}
