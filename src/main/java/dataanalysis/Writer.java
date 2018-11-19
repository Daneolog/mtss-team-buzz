package dataanalysis;

// Imports
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import corelogic.Bus;
import corelogic.Route;
import corelogic.Stop;

public class Writer {

    private FileWriter writer = null;
    private FileReader reader = null;
    List<Route> routes = null;
    String file = null;

    /**
     * Writes graph of stops to GraphViz format
     * @param fileName Desired name for output DOT file
     */
    public Writer(String fileName, List<Route> routes) {
        try {
            writer = new FileWriter(fileName);
            reader = new FileReader(fileName);
            file =  "digraph SimData {\n";
            this.routes = routes;
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            System.out.println("Error reported");
        }
    }

    public void exportGraph(List<Route> routes) {
        try {
            for (int i = 0; i < routes.size(); i++) {
                List<Stop> stops = routes.get(i).getStops();
                for (int j = 0; j < stops.size() - 1; j++) {
                    file += stops.get(j).getName()
                            .replace(" ", "_") + " -> "
                            + stops.get(j+1).getName().replace(" ", "_") + "\n";
                }
                file += stops.get(stops.size()-1).getName().replace(" ", "_") +
                        " -> " + stops.get(0).getName().replace(" ", "_") +
                        "\n";
            }
            file += "}";
            writer.append(file);
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.println("Error reported");
        }
    }
}