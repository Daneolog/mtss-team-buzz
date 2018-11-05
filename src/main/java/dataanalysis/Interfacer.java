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
    private Analyzer simulationAnalyzer;
    private List<Stop> stops;
    private List<Route> routes;

    /***
     * Initiates FileWriter and Analyzer objects.
     * Initiates new references to Core Logic's stop/route ArrayLists.
     *
     */
    public Interfacer() {
        simulationFile = new Writer("simulation.DOT");
        simulationAnalyzer = new Analyzer();
        stops = new ArrayList();
        routes = new ArrayList();
    }

    public Writer getSimulationFile() {
        return simulationFile;
    }

    public Analyzer getSimulationAnalyzer() {
        return simulationAnalyzer;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public static void addStop(Stop stop) throws IllegalArgumentException {
        if (stop == null) {
            throw new IllegalArgumentException("Stop may not be null.");
        }
        // Poll Analyzer
        // Update FileWriter
    }

    public static void addBus(Bus bus) {
        // Poll Analyzer
        // Update FileWriter
    }

    public static void addRoute(Route route) {
        // Poll Analyzer
        // Update FileWriter
    }

    public static void extendRoute(Route route, Stop stop) {

    }
    
}
