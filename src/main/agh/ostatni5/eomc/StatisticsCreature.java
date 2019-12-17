package agh.ostatni5.eomc;

import java.util.HashMap;

public class StatisticsCreature extends AbstractStatistics {
    Creature creature;
    int startDate;
    int delay;
    int showDate;

    public StatisticsCreature(Creature creature, int startDate , int delay) {
        this.creature = creature;
        this.startDate = startDate;
        this.delay = delay;
        this.showDate = startDate+delay;
    }

    private int calculateAllOffspring() {
        HashMap<Integer, Creature> offSpring = new HashMap<>();
        getOffspring(creature, offSpring);
        return offSpring.size();
    }

    private void getOffspring(Creature c, HashMap<Integer, Creature> offSpring) {
        c.children.forEach((o) -> {
            Creature child = (Creature) o;
            if (offSpring.get(child.ID) == null) {
                offSpring.put(child.ID, child);
                getOffspring(child, offSpring);
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DirectOffSpring: ").append(creature.children.size()).append("\n");
        stringBuilder.append("AllOffSpring: ").append(calculateAllOffspring()).append("\n");
        if (creature.isDead())
            stringBuilder.append("Death: ").append(creature.death).append("\n");
        return stringBuilder.toString();
    }


}
