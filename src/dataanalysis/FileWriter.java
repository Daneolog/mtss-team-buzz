package dataanalysis;

// Imports
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriter {

    FileWriter writer = null;
    FileReader reader = null;

    public FileWriter() {
        try {
            writer = new FileWriter("simulation.DOT");
            reader = new FileReader("simulation.DOT");
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}