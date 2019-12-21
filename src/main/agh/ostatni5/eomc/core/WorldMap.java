package agh.ostatni5.eomc.core;

import agh.ostatni5.eomc.my.MyRandom;
import agh.ostatni5.eomc.stats.StatisticsMap;
import agh.ostatni5.eomc.utilities.MapVisualizer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WorldMap implements IPositionChangeObserver {
    private MapVisualizer mapVisualizer = new MapVisualizer(this);
    private MyRandom random = new MyRandom();
    private Rectangle mapRec;
    private int startEnergy = 256;
    private int dayEnergyCost = 2;
    private int grassEnergy = 14;
    private int creatureCountTest = 0;
    private CreaturesPositionMap creaturesPositionMap = new CreaturesPositionMap();
    private HashMap<Vector2d, Grass> grassMap = new HashMap<>();
    private HashMap<Integer, Creature> aliveCreatures = new HashMap<>();
    public StatisticsMap stats = new StatisticsMap(this);
    public Savanna savanna;
    public Jungle jungle;
    public int creatureID = 0;

    public WorldMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight) {
        Vector2d mapStartVector = new Vector2d(0, 0);
        Vector2d jungleStartVector = mapStartVector.add(new Vector2d((mapWidth - jungleWidth) / 2, (mapHeight - jungleHeight) / 2));
        jungle = new Jungle(this, new Rectangle(jungleWidth, jungleHeight, jungleStartVector));
        savanna = new Savanna(this, jungle, new Rectangle(mapWidth, mapHeight, mapStartVector));
        mapRec = new Rectangle(mapWidth, mapHeight, mapStartVector);
    }

    public WorldMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight, int creaturesCount, int grassCount) {
        this(mapWidth, mapHeight, jungleWidth, jungleHeight);
        generateInitialCreatures(creaturesCount);
        generateInitialGrass(grassCount);
    }

    public WorldMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight, int creaturesCount, int grassCount, int startEnergy, int dayEnergyCost, int grassEnergy) {
        this(mapWidth, mapHeight, jungleWidth, jungleHeight, creaturesCount, grassCount);
        this.startEnergy = startEnergy;
        this.dayEnergyCost = dayEnergyCost;
        this.grassEnergy = grassEnergy;
    }

    public WorldMap(Options options) {
        this(options.values);
    }

    private WorldMap(int[] opt) {
        this(opt[0], opt[1], Math.max((int) (opt[0] / opt[2]),1), Math.max((int) (opt[1] / opt[2]),1), opt[3], opt[4], opt[5], opt[6], opt[7]);
    }
    public void nextDay() {
        creatureCountTest = 0;
        stats.zeroAvg();
        for (Object o : creaturesPositionMap.entrySet().toArray()) {
            Map.Entry<Vector2d, PriorityQueue<Creature>> pair = (Map.Entry<Vector2d, PriorityQueue<Creature>>) o;
            Vector2d pos = pair.getKey();
            LinkedList<Creature> creatureLinkedList = new LinkedList<Creature>(Arrays.asList(pair.getValue().toArray(new Creature[0])));
            phaseEat(phaseDeath(creatureLinkedList), pos);
            phaseBreed(pair.getValue());
            phaseMove(creatureLinkedList);
        }
        phaseStats();
        growGrass();
    }

    private LinkedList<Creature> phaseDeath(LinkedList<Creature> creatures) {
        LinkedList<Creature> ableEat = new LinkedList<Creature>();
        AtomicInteger maxEnergy = new AtomicInteger(1);
        creatures.removeIf((creature -> {
            creature.makeBreedAble();
            if (creature.isDead()) {
                killCreature(creature);
                return true;
            } else {
                if (creature.getEnergy().value >= maxEnergy.get() && !creature.getMoved()) {
                    maxEnergy.set(creature.getEnergy().value);
                    ableEat.add(creature);
                }
                return false;
            }
        }));
        return ableEat;
    }

    private void phaseEat(LinkedList<Creature> ableEat, Vector2d pos) {
        Grass grass = grassAt(pos);
        if (grass != null) {
            ableEat.forEach((creature) -> {
                creature.getEnergy().gain(grassEnergy / ableEat.size());
            });
            remove(grass);
        }
    }

    private void phaseBreed(PriorityQueue<Creature> creatures) {
        if (creatures.size() >= 2) {
            Creature[] parents = new Creature[2];
            parents[0] = creatures.poll();
            parents[1] = creatures.poll();
            Creature child = parents[0].breedingWith(parents[1], stats.dayCount);
            if (child != null)
                place(child);
            creatures.add(parents[0]);
            creatures.add(parents[1]);
        }
    }

    private void phaseMove(LinkedList<Creature> creatures) {
        for (Creature creature : creatures) {
            if (!creature.getMoved()) {
                creature.chooseRotation();
                creature.moveForward();
                creature.getEnergy().loss(dayEnergyCost);
            }
        }
    }

    private void phaseStats() {
        creaturesPositionMap.values().forEach(creatures -> {
            for (Creature creature : creatures) {
                creature.setNotMoved();
                creature.incrementLifespan();
                stats.addCreatureStats(creature);
                creatureCountTest++;
            }
        });
        stats.calculateStats();
    }

    private void remove(Grass grass) {
        grassMap.remove(grass.getPosition());
        stats.grassCount--;
    }

    private void remove(Creature creature) {
        remove(creature, creature.getPosition());
    }

    private void remove(Creature creature, Vector2d pos) {
        creaturesPositionMap.remove(creature, pos);
        stats.creatureCount--;
    }

    private void killCreature(Creature creature) {
        creature.setDeath(stats.dayCount);
        remove(creature);
        aliveCreatures.remove(creature.getId().own);
        stats.addDead(creature);
    }


    public IMapElement visibleAt(Vector2d v) {
        return grassMap.get(v) != null ? grassMap.get(v) : creaturesPositionMap.get(v) != null ? (IMapElement) creaturesPositionMap.get(v).peek() : null;
    }

    public Grass grassAt(Vector2d v) {
        return grassMap.get(v);
    }

    public PriorityQueue<Creature> creaturesAt(Vector2d v) {
        return creaturesPositionMap.get(v);
    }


    public boolean place(Grass grass) {
        if (visibleAt(grass.getPosition()) != null) return false;
        grassMap.put(grass.getPosition(), grass);
        stats.grassCount++;
        return true;
    }

    public boolean place(Creature creature) {
        creaturesPositionMap.add(creature);
        if (aliveCreatures.put(creature.getId().own, creature) == null)
            stats.getGenotypeCount().increment(creature.getGenotype());
        stats.creatureCount++;
        return true;
    }

    public Vector2d findFreePosForChildNear(Vector2d pos) {
        Vector2d[] possiblePos = new Vector2d[Rotation.values().length];
        for (int i = 0; i < Rotation.values().length; i++) {
            possiblePos[i] = correctPos(pos.add(Rotation.values()[i].getUnitVector()));
            if (visibleAt(possiblePos[i]) == null) return possiblePos[i];
        }
        return possiblePos[Rotation.getRandom().ordinal()];
    }

    public void generateInitialGrass(int a) {
        for (int i = 0; i < a; i++) {
            growGrass();
        }
    }

    public void generateInitialCreatures(int a) {
        for (int i = 0; i < a; i++) {
            Creature creature = new Creature(this, random.randomPos(mapRec.corners[0], mapRec.corners[2]), startEnergy);
            place(creature);
        }
    }

    private void growGrass() {
        jungle.growGrass();
        savanna.growGrass();
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

    public Vector2d[] getAllGrassesPos() {
        return grassMap.keySet().toArray(new Vector2d[0]);
    }

    public Creature[] getAllVisibleCreatures() {
        Object[] o = creaturesPositionMap.values().toArray();
        Creature[] creatures = new Creature[o.length];
        for (int i = 0; i < o.length; i++) {
            creatures[i] = ((PriorityQueue<Creature>) o[i]).peek();
        }
        return creatures;
    }

    public HashMap<Integer, Creature> getAliveCreatures() {
        return aliveCreatures;
    }
}
