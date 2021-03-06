package dataanalysis;

// Imports
import corelogic.Bus;
import corelogic.Route;
import corelogic.Stop;

import java.util.HashMap;

/**
 * Hub that interfaces with Core Logic (Maybe also Database), Analyzer, and
 * File Writer.
 */
public class Interfacer {

    private Writer simulationFile;
    private HashMap<Integer, Bus> buses;
    private HashMap<Integer, Stop> stops;
    private HashMap<Integer, Route> routes;
    private HashMap<Stop, Double> stopEffectiveness;

    private double effectiveness;

    /***
     * Initiates FileWriter and Analyzer objects.
     * Initiates references to Core Logic's ArrayList objects.
     *
     * @param buses List of all buses in corelogic.SimulationManager.
     * @param stops List of all stops in corelogic.SimulationManager.
     * @param routes List of all routes in corelogic.SimulationManager.
     * @param fileName String for the name of the .DOT file to be created.
     */
    public Interfacer(HashMap<Integer, Bus> buses, HashMap<Integer, Stop> stops,
                      HashMap<Integer, Route> routes, String fileName) {
        simulationFile = new Writer(fileName, routes);
        stopEffectiveness = new HashMap<>();
        updateEffectiveness(buses, stops, routes);
    }

    public HashMap<Integer, Stop> getStops() {
        return stops;
    }

    public HashMap<Integer, Route> getRoutes() {
        return routes;
    }

    public double getEffectiveness() {
        return effectiveness;
    }

    public void dummySimulationInit(HashMap<Integer, Bus> buses,
                                    HashMap<Integer, Stop> stops,
                                    HashMap<Integer, Route> routes) {
        this.buses = buses;
        this.stops = stops;
        this.routes = routes;
    }

    /***
     * Updates effectiveness of the route in a "snapshot" approach
     *
     */
    public void updateEffectiveness(HashMap<Integer, Bus> buses,
                                    HashMap<Integer, Stop> stops,
                                    HashMap<Integer, Route> routes) {
        this.buses = buses;
        this.stops = stops;
        this.routes = routes;
        double totalCost = 0;
        for (Stop stop:this.stops.values()) {
            this.stopEffectiveness.put(stop,
                    Double.valueOf(stop.passengerQueue.size()));
            System.out.print(stop.passengerQueue.size() + ":");
        }

        this.effectiveness = (buses.size() > 0 ? totalCost / buses.size(): 0);
    }

    public void createGraph() {
        simulationFile.exportGraph(routes, stopEffectiveness);
    }
//
//    public void addBus(Bus bus) throws IllegalArgumentException {
//        if (bus == null) {
//            throw new IllegalArgumentException("Buss may not be null.");
//        }
//        // Poll Analyzer
//        // Update FileWriter
//    }
//
//    public void addRoute(Route route) {
//        // Poll Analyzer
//        // Update FileWriter
//    }
//
//    public void extendRoute(Route route, Stop stop) {
//
//    }
}
