package agh.ostatni5.eomc;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WorldMap implements IPositionChangeObserver {
    private MapVisualizer mapVisualizer = new MapVisualizer(this);
    private MyRandom random = new MyRandom();
    private Rectangle mapRec;
    private int startEnergy = 256;
    private int dayEnergyCost = 2;
    private int grassEnergy = 14;
    private int creatureCount2 = 0;
    public StatisticsMap stats = new StatisticsMap(this);
    public Savanna savanna;
    public Jungle jungle;
    int creatureID = 0;
    Vector2d mapStartVector = new Vector2d(0, 0);
    Vector2d jungleStartVector = new Vector2d(0, 0);
    HashMap<Vector2d, Grass> grassMap = new HashMap<>();
    HashMap<Vector2d, PriorityQueue<Creature>> creaturesMap = new HashMap<>();
    HashMap<Integer, Creature> aliveCreatures = new HashMap<>();

    WorldMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight) {
        jungleStartVector = mapStartVector.add(new Vector2d((mapWidth - jungleWidth) / 2, (mapHeight - jungleHeight) / 2));
        jungle = new Jungle(this, new Rectangle(jungleWidth, jungleHeight, jungleStartVector));
        savanna = new Savanna(this, jungle, new Rectangle(mapWidth, mapHeight, mapStartVector));
        mapRec = new Rectangle(mapWidth, mapHeight, mapStartVector);
    }

    WorldMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight, int creaturesCount, int grassCount) {
        this(mapWidth, mapHeight, jungleWidth, jungleHeight);
        generateInitialCreatures(creaturesCount);
        generateInitialGrass(grassCount);
    }

    WorldMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight, int creaturesCount, int grassCount, int startEnergy, int dayEnergyCost, int grassEnergy) {
        this(mapWidth, mapHeight, jungleWidth, jungleHeight, creaturesCount, grassCount);
        this.startEnergy = startEnergy;
        this.dayEnergyCost = dayEnergyCost;
        this.grassEnergy = grassEnergy;
    }

    public WorldMap(Options options) {
        this(options.values);
    }
    private WorldMap(int [] opt) {
        this(opt[0], opt[1], (int) (opt[0] / opt[2]), (int) (opt[0] / opt[2]), opt[3], opt[4], opt[5], opt[6], opt[7]);
    }


    public void nextDay() {
        creatureCount2 = 0;
        stats.zeroAvg();
        for (Object o : creaturesMap.entrySet().toArray()) {
            Map.Entry<Vector2d, PriorityQueue<Creature>> pair = (Map.Entry<Vector2d, PriorityQueue<Creature>>) o;
            Vector2d pos = pair.getKey();
            LinkedList<Creature> creatureLinkedList = new LinkedList<Creature>(Arrays.asList(pair.getValue().toArray(new Creature[0])));
            phaseEat(phaseDeath(creatureLinkedList),pos);
            phaseBreed(creatureLinkedList);
            phaseMove(creatureLinkedList);
        }
        phaseStats();
        jungle.growGrass();
        savanna.growGrass();
    }

    private LinkedList<Creature> phaseDeath(LinkedList<Creature> creatures){
        LinkedList<Creature> ableEat = new LinkedList<Creature>();
        AtomicInteger maxEnergy = new AtomicInteger(1);
        creatures.removeIf((creature -> {
            creature.makeBreedAble();
            if(creature.isDead())
            {
                creature.setDeath(stats.dayCount);
                remove(creature);
                aliveCreatures.remove(creature.getId().own);
                stats.decrementGenotype(creature.getGenotype());
                return true;
            }
            else
            {
                if (creature.getEnergy().value >= maxEnergy.get())
                {
                    maxEnergy.set(creature.getEnergy().value);
                    ableEat.add(creature);
                }
                return false;
            }

        }));
        return ableEat;
    }

    private void  phaseEat(LinkedList<Creature> ableEat,Vector2d pos)
    {
        Grass grass = grassAt(pos);
        if (grass != null) {
            ableEat.forEach((creature) -> {
                creature.getEnergy().gain(grassEnergy / ableEat.size());
            });
            remove(grass);
        }
    }

    private void phaseBreed(LinkedList<Creature> creatures)
    {
        if (creatures.size() >= 2) {
            int i = 0;
            Creature[] parents = new Creature[2];
            for (Creature c : creatures) {
                parents[i] = c;
                i++;
                if (i == 2) break;
            }
            Creature child = parents[0].breedingWith(parents[1], stats.dayCount);
            if (child != null)
                place(child);
        }
    }

    private void phaseMove(LinkedList<Creature> creatures)
    {
        for (Creature creature : creatures) {
            if (!creature.getMoved()) {
                creature.chooseRotation();
                creature.moveForward();
                creature.getEnergy().loss(dayEnergyCost);
            }
        }
    }

    private void phaseStats(){
        creaturesMap.values().forEach(creatures -> {
            for (Creature creature : creatures) {
                creature.setNotMoved();
                creature.incrementLifespan();
                stats.addCreatureStats(creature);
                creatureCount2++;
            }
        });
        stats.calculateStats();
    }

    private void remove(Grass grass) {
        grassMap.remove(grass.position);
        stats.grassCount--;
    }

    private boolean remove(Creature creature) {
        return remove(creature, creature.getPosition());
    }

    private boolean remove(Creature creature, Vector2d pos) {
        Vector2d tempPos = new Vector2d(pos);
        ComparatorEnergy comparatorEnergy = new ComparatorEnergy();
        if (!creaturesMap.get(tempPos).remove(creature)) {
            for (Creature creature1 : creaturesMap.get(tempPos)) {
                System.out.println(creature1.getId().own + " " + creature1.equals(creature) + "    " + comparatorEnergy.compare(creature1, creature));
                if (creature1.equals(creature)) {
                    System.out.println(creaturesMap.get(tempPos).remove(creature1));
                }
            }
            throw new NullPointerException("Gdzie me zwirze:" + creature.getId().own + "   " + tempPos + " " + creature.getPosition() + " siz " + creaturesMap.get(tempPos).size());
        }
        if (creaturesMap.get(tempPos).isEmpty())
            creaturesMap.remove(tempPos);
        stats.creatureCount--;
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
        stats.grassCount++;
        return true;
    }

    public boolean place(Creature creature) {
        Vector2d pos = creature.getPosition();
        if (creaturesAt(pos) == null) {
            creaturesMap.put(pos, new PriorityQueue<Creature>(new ComparatorEnergy()));
        }
        creaturesAt(pos).add(creature);
        if(aliveCreatures.put(creature.getId().own, creature)==null)
            stats.incrementGenotype(creature.getGenotype());
        stats.creatureCount++;
        return true;
    }

    public Vector2d findFreeNearForChild(Vector2d pos) {
        Vector2d[] possiblePos = new Vector2d[Rotation.values().length];
        for (int i = 0; i < Rotation.values().length; i++) {
            possiblePos[i] = correctPos(pos.add(Rotation.values()[i].getUnitVector()));
            if (visibleAt(possiblePos[i]) == null) return possiblePos[i];
        }
        return possiblePos[Rotation.getRandom().ordinal()];
    }

    public void generateInitialGrass(int a) {
        for (int i = 0; i < a; i++) {
            jungle.growGrass();
            savanna.growGrass();
        }
    }

    public void generateInitialCreatures(int a) {
        for (int i = 0; i < a; i++) {
            Creature creature = new Creature(this, random.randomPos(mapRec.corners[0], mapRec.corners[2]), startEnergy);
//            Creature creature = new Creature(this, new Vector2d(0, 0), startEnergy);
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

    public Vector2d[] getAllGrassesPos() {
        return grassMap.keySet().toArray(new Vector2d[0]);
    }

    public Creature[] getAllVisibleCreatures() {
        Object[] o = creaturesMap.values().toArray();
        Creature[] creatures = new Creature[o.length];
        for (int i = 0; i < o.length; i++) {
            creatures[i] = ((PriorityQueue<Creature>) o[i]).peek();
        }
        return creatures;
    }
}
