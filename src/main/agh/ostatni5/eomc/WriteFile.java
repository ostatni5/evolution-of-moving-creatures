package agh.ostatni5.eomc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
    private String path;
    public WriteFile( String _path) {
        path = _path;
    }
    public void writeToFile( String textLine, Boolean append ) throws IOException {
        FileWriter writer = new FileWriter(path,append);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(textLine);
        bufferedWriter.newLine();
        bufferedWriter.close();
        writer.close();
    }
}
