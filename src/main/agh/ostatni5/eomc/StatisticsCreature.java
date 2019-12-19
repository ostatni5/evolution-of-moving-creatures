package agh.ostatni5.eomc;

import java.util.HashMap;

public class StatisticsCreature extends AbstractStatistics {

    private Creature creature;
    private int startDate;

    public StatisticsCreature(Creature creature, int startDate) {
        this.creature = creature;
        this.startDate = startDate;
    }

    private int calculateAllOffspring() {
        HashMap<Integer, Creature> offSpring = new HashMap<>();
        getOffspring(creature, offSpring);
        return offSpring.size();
    }
    private int calculateDirectOffspring() {
        return (int) creature.getChildren().stream().filter(creatureChild -> ((Creature) creatureChild).getBirth() >= startDate).count();
    }

    private void getOffspring(Creature c, HashMap<Integer, Creature> offSpring) {
        c.getChildren().forEach((o) -> {
            Creature child = (Creature) o;
            if (offSpring.get(child.getId().own) == null && child.getBirth() >= startDate) {
                offSpring.put(child.getId().own, child);
                getOffspring(child, offSpring);
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Creature: ").append(creature.getId().own).append(":").append(creature.getPosition()).append("\n");
        stringBuilder.append("DirectOffSpring: ").append(calculateDirectOffspring()).append("\n");
        stringBuilder.append("AllOffSpring: ").append(calculateAllOffspring()).append("\n");
        if (creature.isDead())
            stringBuilder.append("Death: ").append(creature.getDeath()).append("\n");
        return stringBuilder.toString();
    }

    public Creature getCreature() {
        return creature;
    }

    public void setCreature(Creature creature) {
        this.creature = creature;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }


}
