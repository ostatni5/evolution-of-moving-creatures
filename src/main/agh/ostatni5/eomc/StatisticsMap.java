package agh.ostatni5.eomc;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class StatisticsMap extends AbstractStatistics {
    WorldMap worldMap;
    int energyAvg = 0;
    int lifespanAvg = 0;
    float childrenAvg = 0;
    int dayCount = 0;
    int grassCount = 0;
    int creatureCount = 0;
    private HashMap<Genotype, LongAdder> genotypeCount = new HashMap<>();
    private Map.Entry<Genotype, LongAdder> maxGenotype =null;

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
        maxGenotype= getDominantGenotype();
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
        if (creatureCount > 0) {
            energyAvg /= creatureCount;
            childrenAvg /= creatureCount;
        } else zeroAvg();
    }

    public void incrementGenotype(Genotype genotype) {
        if(genotypeCount.get(genotype)==null)
        {
            genotypeCount.put(genotype,new LongAdder());
        }
        genotypeCount.get(genotype).increment();
    }

    public void decrementGenotype(Genotype genotype) {
        genotypeCount.get(genotype).decrement();
    }

    private Map.Entry<Genotype, LongAdder> getDominantGenotype() {
        int max = 0;
        Map.Entry<Genotype, LongAdder> maxGenotype = null;
        for (Map.Entry<Genotype, LongAdder> genotypeLongAdderEntry : genotypeCount.entrySet()) {
            if (genotypeLongAdderEntry.getValue().intValue() >= max) {
                max = genotypeLongAdderEntry.getValue().intValue();
                maxGenotype = genotypeLongAdderEntry;
            }
        }

        return maxGenotype;
    }

    public Creature[] getAllWithDominantGenotype()
    {
        return worldMap.aliveCreatures.values().stream().filter(creature -> creature.getGenotype().equals(maxGenotype.getKey())).toArray(Creature[]::new);
    }

}

