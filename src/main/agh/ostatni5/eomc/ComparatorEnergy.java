package agh.ostatni5.eomc;

import java.util.Comparator;

public
class ComparatorEnergy implements Comparator<Creature> {
    @Override
    public int compare(Creature creature, Creature t1) {
        return creature.getEnergy() - t1.getEnergy();
    }
}
