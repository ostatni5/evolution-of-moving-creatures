package agh.ostatni5.eomc.stats;

import agh.ostatni5.eomc.core.GenotypeMap;

public class HistoryRecord extends AbstractStatistics {
    public int energyAvg = 0;
    public int lifespanAvg = 0;
    public float childrenAvg = 0;
    public int dayCount = 0;
    public int grassCount = 0;
    public int creatureCount = 0;
    public int deadCount = 0;
    public GenotypeMap genotypeCount = new GenotypeMap();

    HistoryRecord() {
    }

    private HistoryRecord(int energyAvg, int lifespanAvg, float childrenAvg, int dayCount, int grassCount, int creatureCount, int deadCount, GenotypeMap genotypeCount) {
        this.energyAvg = energyAvg;
        this.lifespanAvg = lifespanAvg;
        this.childrenAvg = childrenAvg;
        this.dayCount = dayCount;
        this.grassCount = grassCount;
        this.creatureCount = creatureCount;
        this.deadCount = deadCount;
        this.genotypeCount = genotypeCount;
    }

    public HistoryRecord(StatisticsMap stats) {
        this(stats.energyAvg, stats.lifespanAvg, stats.childrenAvg, stats.dayCount, stats.grassCount, stats.creatureCount, stats.deadCount, stats.cloneGenotypeCount());
    }

    public void add(HistoryRecord historyRecord) {
        add(historyRecord.energyAvg, historyRecord.lifespanAvg, historyRecord.childrenAvg, historyRecord.dayCount, historyRecord.grassCount, historyRecord.creatureCount, historyRecord.deadCount, historyRecord.genotypeCount);
    }

    private void add(int energyAvg, int lifespanAvg, float childrenAvg, int dayCount, int grassCount, int creatureCount, int deadCount, GenotypeMap genotypeCount) {
        this.dayCount = Math.max(dayCount, this.dayCount);
        this.energyAvg += energyAvg;
        this.lifespanAvg += lifespanAvg;
        this.childrenAvg += childrenAvg;
        this.grassCount += grassCount;
        this.creatureCount += creatureCount;
        this.deadCount += deadCount;
        this.genotypeCount.addAll(genotypeCount);
    }

    public void calculateStatsByDay() {
        if (dayCount == 0) return;
        this.energyAvg /= dayCount;
        this.lifespanAvg /= dayCount;
        this.childrenAvg /= dayCount;
        this.grassCount /= dayCount;
        this.creatureCount /= dayCount;
    }

    @Override
    public String toString() {
        GenotypeMap.Entry dominantGenotype = genotypeCount.getDominant();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AllDays: ").append(dayCount).append("\n");
        stringBuilder.append("AVGCreaturesAlive: ").append(creatureCount).append("\n");
        stringBuilder.append("AVGrassCount: ").append(grassCount).append("\n");
        stringBuilder.append("DominantGenotype: ").append("\n");
        if (dominantGenotype != null)
            stringBuilder.append(dominantGenotype.getKey()).append("\n");
        stringBuilder.append("AVGAvgEnergy: ").append(energyAvg).append("\n");
        stringBuilder.append("AVGAvgLifespan: ").append(lifespanAvg).append("\n");
        stringBuilder.append("AVGAvgChildren: ").append(String.format("%.2f", childrenAvg)).append("\n");
        return stringBuilder.toString();
    }
}
