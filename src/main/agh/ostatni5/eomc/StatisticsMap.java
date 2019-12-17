package agh.ostatni5.eomc;

import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class Statistics {
    WorldMap worldMap;

    public Statistics(WorldMap worldMap) {
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

    public String toHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html>");
        stringBuilder.append("<table>");
        for (String s : toString().split("\n")) {
            String[] ss = s.split(":");
            stringBuilder.append("<tr>");
            for (String s1 : ss) {
                stringBuilder.append("<td>");
                stringBuilder.append(s1);
                stringBuilder.append("</td>");
            }
            stringBuilder.append("</tr>");
        }
        stringBuilder.append("</table>");
        stringBuilder.append("</html>");
        return stringBuilder.toString();
    }
}


