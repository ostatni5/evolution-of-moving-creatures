package agh.ostatni5.eomc.stats;

import agh.ostatni5.eomc.utilities.WriteFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;

public class History {
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private LinkedList<HistoryRecord> historyRecords = new LinkedList<HistoryRecord>();
    private HistoryRecord sumOfAllRecords = new HistoryRecord();

    public History() {
    }

    public void addRecord(StatisticsMap statisticsMap) {
        HistoryRecord historyRecord = new HistoryRecord(statisticsMap);
        historyRecords.add(historyRecord);
        sumOfAllRecords.add(historyRecord);
    }

    private void calculateSummary() {
        sumOfAllRecords.calculateStatsByDay();
    }

    public void toFile(){
        calculateSummary();
        WriteFile writeFile = new WriteFile("logs/log.txt");
        try {
            writeFile.writeToFile(timestamp.toString(),true);
            writeFile.writeToFile(sumOfAllRecords.toString(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
