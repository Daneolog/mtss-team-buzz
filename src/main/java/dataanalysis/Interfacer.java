package dataanalysis;

// Imports
import corelogic.Bus;
import corelogic.Route;
import corelogic.Stop;
import java.util.List;
import java.util.ArrayList;

/**
 * Hub that interfaces with Core Logic (Maybe also Database), Analyzer, and
 * File Writer.
 */
public class Interfacer {

    private Writer simulationFile;
    private List<Bus> buses;
    private List<Stop> stops;
    private List<Route> routes;
    private List<Integer> stopEffectiveness;
    private String fileName;

    private double effectiveness;

    /***
     * Initiates FileWriter and Analyzer objects.
     * Initiates new references to Core Logic objects. (not linked w/Core Logic)
     *
     */
    public Interfacer() {
        fileName = "simulation.DOT"; // Default file name
        stops = new ArrayList<>();
        routes = new ArrayList<>();
        buses = new ArrayList<>();
        simulationFile = new Writer(fileName, routes);
        stopEffectiveness = new ArrayList<>();
    }

    /***
     * Initiates FileWriter and Analyzer objects.
     * Initiates references to Core Logic's ArrayList objects.
     *
     * @param buses List of all buses in corelogic.SimulationManager.
     * @param stops List of all stops in corelogic.SimulationManager.
     * @param routes List of all routes in corelogic.SimulationManager.
     * @param fileName String for the name of the .DOT file to be created.
     */
    public Interfacer(List<Bus> buses, List<Stop> stops, List<Route> routes,
                      String fileName) {
        this.fileName = fileName;
        this.buses = buses;
        this.stops = stops;
        this.routes = routes;
        simulationFile = new Writer(fileName, routes);
        stopEffectiveness = new ArrayList<>();
        stopEffectiveness.add(Integer.valueOf(0));
    }

    public Writer getSimulationFile() {
        return simulationFile;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public double getEffectiveness() {
        return effectiveness;
    }

    public void dummySimulationInit(List<Bus> buses, List<Stop> stops, List<Route> routes) {
        this.buses = buses;
        this.stops = stops;
        this.routes = routes;
    }

    /***
     * Updates effectiveness of the route in a "snapshot" approach
     *
     */
    public void updateEffectiveness() {
        double totalCost = 0;
        for (Bus bus: buses) {
            for (Stop stop:bus.getRoute().getStops()) {
                totalCost += stop.getTotalDisembarkRate()/bus.getSpeed();
            }
        }

        this.effectiveness = (buses.size() > 0 ? totalCost / buses.size(): 0);
    }

    public void createGraph() {
        simulationFile.exportGraph(routes);
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
