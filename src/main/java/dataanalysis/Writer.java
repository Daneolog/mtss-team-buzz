package dataanalysis;

// Imports
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import corelogic.Stop;

public class Writer {

    private FileWriter writer = null;
    private FileReader reader = null;
    private int offset = 0;

    /**
     * Writes graph of stops to GraphViz format
     * @param fileName Desired name for output DOT file
     */
    public Writer(String fileName) {
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

    /*public addNode(Stop stop) {
        writer.write(" -> " + "\n");
    }*/
}