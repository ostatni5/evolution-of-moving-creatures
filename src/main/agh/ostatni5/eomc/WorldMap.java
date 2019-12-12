package agh.ostatni5.eomc;

import javafx.util.Pair;

import java.util.*;

public class WorldMap implements IPositionChangeObserver {
    MapVisualizer mapVisualizer = new MapVisualizer(this);
    MyRandom random = new MyRandom();
    Rectangle mapRec;
    Savanna savanna;
    Jungle jungle;

    int creatureID = 0;

    int startEnergy = 256;
    int dayEnergyCost = 2;
    int grassEnergy = 14;

    int grassCount = 0;
    int creatureCount = 0;
    int cc = 0;

    Vector2d mapStartVector = new Vector2d(0, 0);
    Vector2d jungleStartVector = new Vector2d(0, 0);
    HashMap<Vector2d, Grass> grassMap = new HashMap<>();
    HashMap<Vector2d, PriorityQueue<Creature>> creaturesMap = new HashMap<>();

    WorldMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight) {
        jungleStartVector = mapStartVector.add(new Vector2d((mapWidth - jungleWidth) / 2, (mapHeight - jungleHeight) / 2));
        jungle = new Jungle(this, new Rectangle(jungleWidth, jungleHeight, jungleStartVector));
        savanna = new Savanna(this, jungle, new Rectangle(mapWidth, mapHeight, mapStartVector));
        mapRec = new Rectangle(mapWidth, mapHeight, mapStartVector);
    }

    WorldMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight, int c, int g) {
        jungleStartVector = mapStartVector.add(new Vector2d((mapWidth - jungleWidth) / 2, (mapHeight - jungleHeight) / 2));
        jungle = new Jungle(this, new Rectangle(jungleWidth, jungleHeight, jungleStartVector));
        savanna = new Savanna(this, jungle, new Rectangle(mapWidth, mapHeight, mapStartVector));
        mapRec = new Rectangle(mapWidth, mapHeight, mapStartVector);
        generateInitialCreatures(c);
        generateInitialGrass(g);
    }

    public void nextDay() {
        cc = 0;
        for (Object o : creaturesMap.entrySet().toArray()) {
            Map.Entry<Vector2d, PriorityQueue<Creature>> pair = (Map.Entry<Vector2d, PriorityQueue<Creature>>) o;
            Vector2d pos = pair.getKey();
            Creature[] ts1 = pair.getValue().toArray(new Creature[0]);
            LinkedList<Creature> ts = new LinkedList<Creature>(Arrays.asList(ts1));
            LinkedList<Creature> ableEat = new LinkedList<Creature>();
            int maxEnergy = 1;
            boolean cont = true;
            //death
            for (Creature c : ts.toArray(new Creature[0])) {
                c.makeBreedAble();
                if (!pos.equals(c.getPosition())) throw new NumberFormatException(pos + " " + c.getPosition());
                if (c.isDead()) {
                    ts.remove(c);
                    System.out.println(pos + " " + creaturesAt(pos).contains(c));
                    remove(c);
                } else if (c.energy.value >= maxEnergy) {
                    maxEnergy = c.energy.value;
                    ableEat.add(c);
                }
            }

            //eating
            Grass grass = grassAt(pos);
            if (grass != null) {
                ableEat.forEach((c) -> {
                    c.energy.gain(grassEnergy / ableEat.size());
                });
                remove(grass);
            }
            //breeding
            if (ts.size() >= 2) {
                int i = 0;
                Creature[] creatures = new Creature[2];
                for (Creature c : ts) {
                    creatures[i] = c;
                    i++;
                    if (i == 2) break;
                }
                Creature child = creatures[0].breedingWith(creatures[1]);
                if (child != null)
                    place(child);
            }
            //moving
            for (Creature c : ts.toArray(new Creature[0])) {
                if (!c.moved) {

                    //if (c.isDead()) throw new NumberFormatException(pos + "DEAD " + c.getPosition());
                    System.out.print(c.getPosition() + ":" + cc + ":" + c.energy.value + ":" + c.rotation + " -- ");
                    c.chooseRotation();
                    c.moveForward();
                    System.out.print(c.getPosition() + ":" + cc + ":" + c.energy.value + ":" + c.rotation + " :" + c.ID);
                    System.out.println();
                    c.energy.loss(dayEnergyCost);
                }
            }

        }

        for (Object o : creaturesMap.entrySet().toArray()) {
            Map.Entry<Vector2d, PriorityQueue<Creature>> pair = (Map.Entry<Vector2d, PriorityQueue<Creature>>) o;
            Creature[] ts1 = pair.getValue().toArray(new Creature[0]);
            LinkedList<Creature> ts = new LinkedList<Creature>(Arrays.asList(ts1));
            for (Creature c : ts.toArray(new Creature[0])) {
                c.moved = false;
                cc++;
            }

        }

        jungle.growGrass();
        savanna.growGrass();
    }

    private void remove(Grass grass) {
        grassMap.remove(grass.position);
        grassCount--;
    }

    private boolean remove(Creature creature) {
        return remove(creature, creature.getPosition());
    }

    private boolean remove(Creature creature, Vector2d pos) {
        Vector2d tempPos = new Vector2d(pos);
        ComparatorEnergy comparatorEnergy = new ComparatorEnergy();
        if (!creaturesMap.get(tempPos).remove(creature)) {
            for (Creature c : creaturesMap.get(tempPos)) {
                System.out.println(c.ID + " " + c.equals(creature) + "    "+ comparatorEnergy.compare(c,creature));
                if (c.equals(creature)) {
                    System.out.println(creaturesMap.get(tempPos).remove(c));
                }
            }
            throw new NumberFormatException("Gdzie me zwirze:" + creature.ID + "   " + tempPos + " " + creature.getPosition() + " siz " + creaturesMap.get(tempPos).size());
        }
        if (creaturesMap.get(tempPos).isEmpty())
            creaturesMap.remove(tempPos);
        creatureCount--;
        return true;


    }


    public IMapElement visibleAt(Vector2d v) {
        return grassMap.get(v) != null ? grassMap.get(v) : creaturesMap.get(v) != null ? (IMapElement) creaturesMap.get(v).peek() : null;
    }

    public Grass grassAt(Vector2d v) {
        return grassMap.get(v);
    }

    public PriorityQueue<Creature> creaturesAt(Vector2d v) {
        return creaturesMap.get(v);
    }


    public boolean place(Grass grass) {
        if (visibleAt(grass.position) != null) return false;
        grassMap.put(grass.position, grass);
        grassCount++;
        return true;
    }

    public boolean place(Creature creature) {
        Vector2d pos = creature.getPosition();
        if (creaturesAt(pos) == null) {
            creaturesMap.put(pos, new PriorityQueue<Creature>(new ComparatorEnergy()));
        }
        creaturesAt(pos).add(creature);
        System.out.print("  P" + creaturesAt(pos).size() + ":" + creature.ID + "P  ");
        creatureCount++;
        return true;
    }


    public void generateInitialGrass(int a) {
        for (int i = 0; i < a; i++) {
            jungle.growGrass();
            savanna.growGrass();
        }
    }

    public void generateInitialCreatures(int a) {
        for (int i = 0; i < a; i++) {
//            Creature creature = new Creature(this, random.randomPos(mapRec.corners[0], mapRec.corners[2]), startEnergy);
            Creature creature = new Creature(this, new Vector2d(0, 0), startEnergy);
            place(creature);
        }
    }


    public Vector2d correctPos(Vector2d vector2d) {
        int x = vector2d.x < mapRec.corners[0].x ? mapRec.corners[1].x : vector2d.x > mapRec.corners[1].x ? mapRec.corners[0].x : vector2d.x;
        int y = vector2d.y < mapRec.corners[0].y ? mapRec.corners[2].y : vector2d.y > mapRec.corners[2].y ? mapRec.corners[0].y : vector2d.y;
        return new Vector2d(x, y);
    }

    public Biome biomeAt(Vector2d v) {
        return jungle.rectangle.isIn(v) ? Biome.JUNGLE : Biome.SAVANNA;
    }

    @Override
    public String toString() {
        return mapVisualizer.draw(mapRec.corners[0], mapRec.corners[2]);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Creature creature) {
        remove(creature, oldPosition);
        place(creature);
    }

}
