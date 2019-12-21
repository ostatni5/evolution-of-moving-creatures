package agh.ostatni5.eomc.utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
    private String path;
    public WriteFile( String _path) {
        path = _path.replaceAll(" ", "_");
    }
    public void writeToFile( String textLine, Boolean append ) throws IOException {
        FileWriter writer = new FileWriter(path,append);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.newLine();
        String[] words = textLine.split("\n");
        for (String word: words) {
            bufferedWriter.write(word);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        writer.close();
    }
}
