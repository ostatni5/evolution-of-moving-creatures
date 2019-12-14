package agh.ostatni5.eomc;

import java.util.Comparator;

public
class ComparatorEnergy implements Comparator<Creature> {
    @Override
    public int compare(Creature creature, Creature t1) {
        if(t1.equals(creature)) return 0;
        return t1.energy.value>creature.energy.value?1: -1 ;
    }
}
