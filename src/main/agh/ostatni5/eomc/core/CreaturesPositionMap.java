package agh.ostatni5.eomc.core;

import java.util.HashMap;
import java.util.PriorityQueue;

public class CreaturesPositionMap extends HashMap<Vector2d, PriorityQueue<Creature>> {
    public CreaturesPositionMap(){
        super();
    }

    public void remove(Creature creature, Vector2d pos){
        Vector2d tempPos = new Vector2d(pos);
        get(tempPos).remove(creature);
        if (get(tempPos).isEmpty())
            remove(tempPos);
    }
    public void add(Creature creature){
        Vector2d pos = creature.getPosition();
        if (get(pos) == null) {
            put(pos, new PriorityQueue<>(new ComparatorEnergy()));
        }
        get(pos).add(creature);

    }
}
