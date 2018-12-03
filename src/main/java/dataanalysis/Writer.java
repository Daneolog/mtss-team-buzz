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
            for (int i : routes.keySet()) {
                stops = routes.get(i).getStops();

                for (int j = 0; j < stops.size() - 1; j++) {
                    String name1 = fix(stops.get(j).getName());
                    String name2 = fix(stops.get(j + 1).getName());
                    file += name1 + " [label = \"" + name1 + ": "
                            + stopEffectiveness.getOrDefault(stops.get(j), 0.0)
                            + "\"]\n";
                    file += name1 + " -> " + name2 + "[label=\"" + i + "\", "
                            + "color=" + colors[i % colors.length] + "]\n";
                }
                Stop lastStop = stops.get(stops.size() - 1);
                file += fix(lastStop.getName()) + " [label = \""
                        + fix(stops.get(stops.size() - 1).getName())
                        + ": " + stopEffectiveness.getOrDefault(lastStop, 0.0)
                        + "\"]\n";
                // Connects last stop to first stop in route
                if (!routes.get(i).isLinear()) {
                    file += fix(stops.get(stops.size() - 1).getName()) +
                            " -> " + fix(stops.get(0).getName()) +
                            "[label=\"" + i + "\", color=" + colors[i % colors.length] + "]\n";
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
    private String fix(String input) {
        return input.replace(" ", "_").replace("/", "_").replace("-", "_")
                .replace("(", "_").replace(")", "_").replace("*", "_")
                .replace("@", "_at_").replace("&", "_and_");
    }
}