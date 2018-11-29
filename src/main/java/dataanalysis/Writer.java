package dataanalysis;

// Imports
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import corelogic.Route;
import corelogic.Stop;

public class Writer {

    private FileWriter writer = null;
    private FileReader reader = null;
    HashMap<Integer, Route> routes = null;
    String file = null;
    String[] colors = {"brown", "black", "palegreen4", "violetred4"};

    /**
     * Writes graph of stops to GraphViz format
     * @param fileName Desired name for output DOT file
     */
    public Writer(String fileName, HashMap<Integer, Route> routes) {
        try {
            writer = new FileWriter(fileName);
            reader = new FileReader(fileName);
            file =  "digraph SimData {\nrankdir=LR\ncolorscheme=X11\n";
            this.routes = routes;
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            System.out.println("Error reported");
        }
    }

    public void exportGraph(HashMap<Integer, Route> routes, HashMap<Stop,
            Double> stopEffectiveness) {
        if (routes == null) {
            System.out.println("Error null routes");
            return;
        }
        try {
            List<Stop> stops;
            for (int i = 0; i < routes.size(); i++) {
                stops = routes.get(i).getStops();

                for (int j = 0; j < stops.size() - 1; j++) {
                    String name1 = stops.get(j).getName().replace(" ", "_");
                    String name2 = stops.get(j + 1).getName().replace(" ", "_");
                    file += name1 + " [label = \"" + name1 + ": "
                            + stopEffectiveness.getOrDefault(stops.get(j), 0.0)
                            + "\"]\n";
                    file += name1 + " -> " + name2 + "[label=\"" + i + "\", "
                            + "color=" + colors[i] + "]\n";
                }
                Stop lastStop = stops.get(stops.size() - 1);
                file += lastStop.getName().replace(" ", "_") + " [label = \""
                        + stops.get(stops.size() - 1).getName().replace(" ", "_")
                        + ": " + stopEffectiveness.getOrDefault(lastStop, 0.0)
                        + "\"]\n";
                // Connects last stop to first stop in route
                if (!routes.get(i).isLinear()) {
                    file += stops.get(stops.size() - 1).getName().replace(" ", "_") +
                            " -> " + stops.get(0).getName().replace(" ", "_") +
                            "[label=\"" + i + "\", color=" + colors[i] + "]\n";
                }
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