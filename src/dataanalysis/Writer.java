package dataanalysis;

// Imports
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import corelogic.Stop;

public class Writer {

    FileWriter writer = null;
    FileReader reader = null;
    int offset = 0;

    public FileWriter(String fileName) {
        try {
            writer = new FileWriter(fileName);
            reader = new FileReader(fileName);
            String start = "digraph G {\n}";
            writer.write(start);
            offset += start.length();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public addNode(Stop stop) {
        writer.write( + " -> " + + "\n");
    }
}