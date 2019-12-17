package agh.ostatni5.eomc;

public class StatisticsMap extends AbstractStatistics {
    WorldMap worldMap;

    public StatisticsMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Day: ").append(worldMap.dayCount).append("\n");
        stringBuilder.append("CreaturesAll: ").append(worldMap.creatureID).append("\n");
        stringBuilder.append("CreaturesAlive: ").append(worldMap.creatureCount).append("\n");
        stringBuilder.append("GrassCount: ").append(worldMap.grassCount).append("\n");
        stringBuilder.append("DominantGen: ").append(MyArrays.getIndexOfMax(worldMap.genCount)).append("\n");
        stringBuilder.append("AvgEnergy: ").append(worldMap.energyAvg).append("\n");
        stringBuilder.append("AvgLifespan: ").append(worldMap.lifespanAvg).append("\n");
        stringBuilder.append("AvgChildren: ").append(worldMap.childrenAvg).append("\n");
        return stringBuilder.toString();
    }

}


