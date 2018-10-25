package dataanalysis;

// Imports
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {

    FileWriter writer = null;
    FileReader reader = null;

    public FileWriter(String fileName) {
        try {
            writer = new FileWriter(fileName);
            reader = new FileReader(fileName);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}