package agh.ostatni5.eomc;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;

public class History {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    LinkedList<HistoryRecord> historyRecords = new LinkedList<HistoryRecord>();
    HistoryRecord sumOfAllRecords = new HistoryRecord();

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
