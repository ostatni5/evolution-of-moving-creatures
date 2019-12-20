package agh.ostatni5.eomc;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class StatisticsMap extends AbstractStatistics {
    private WorldMap worldMap;
    public int energyAvg = 0;
    public int lifespanAvg = 0;
    public float childrenAvg = 0;
    public int dayCount = 0;
    public int grassCount = 0;
    public int creatureCount = 0;
    public int deadCount =0;
    private GenotypeMap genotypeCount = new GenotypeMap();
    private GenotypeMap.Entry maxGenotype =null;

    public StatisticsMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Day: ").append(dayCount).append("\n");
        stringBuilder.append("CreaturesAll: ").append(worldMap.creatureID).append("\n");
        stringBuilder.append("CreaturesAlive: ").append(creatureCount).append("\n");
        stringBuilder.append("GrassCount: ").append(grassCount).append("\n");
        maxGenotype= genotypeCount.getDominant();
        if (maxGenotype != null) {
            stringBuilder.append("DominantGenotype: ").append(maxGenotype.getValue()).append("\n");
            stringBuilder.append(maxGenotype.getKey()).append("\n");
        }
        stringBuilder.append("AvgEnergy: ").append(energyAvg).append("\n");
        stringBuilder.append("AvgLifespan: ").append(lifespanAvg).append("\n");
        stringBuilder.append("AvgChildren: ").append(String.format("%.2f",childrenAvg)).append("\n");
        return stringBuilder.toString();
    }

    public void zeroAvg() {
        energyAvg = 0;
        childrenAvg = 0;
    }

    public void addCreatureStats(Creature creature) {
        energyAvg += creature.getEnergy().value;
        childrenAvg += creature.getChildren().size();
    }

    public void calculateStats() {
        dayCount++;
        if (creatureCount > 0) {
            energyAvg /= creatureCount;
            childrenAvg /= creatureCount;
        } else zeroAvg();
    }

    public void addDead(Creature creature)
    {
        getGenotypeCount().decrement(creature.getGenotype());
        lifespanAvg = ((lifespanAvg * deadCount ) + creature.getLifespan())/++deadCount;
    }



    public Creature[] getAllWithDominantGenotype()
    {
        return worldMap.getAliveCreatures().values().stream().filter(creature -> creature.getGenotype().equals(maxGenotype.getKey())).toArray(Creature[]::new);
    }

    public GenotypeMap cloneGenotypeCount() {
        return (GenotypeMap) genotypeCount.clone();
    }

    public GenotypeMap getGenotypeCount() {
        return genotypeCount;
    }
}

