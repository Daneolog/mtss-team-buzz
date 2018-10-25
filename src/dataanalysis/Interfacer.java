package dataanalysis;

// Imports
import src.corelogic.Bus;
import src.corelogic.Route;
import src.corelogic.SimulationManager;
import src.corelogic.Stop;
import java.util.List;
import java.util.ArrayList;

/**
 * Hub that interfaces with Core Logic (Maybe also Database), Analyzer, and
 * File Writer.
 */
public class Interfacer {

    private Writer simulationFile = null;
    private Analyzer simulationAnalyzer = null;
    private List<Stop> stops = null;
    private List<Route> routes = null;

    /***
     * Initiates FileWriter and Analyzer objects.
     * Initiates new references to Core Logic's stop/route ArrayLists.
     *
     * @param s Core Logic's hub class. Used to access Lists of stops and
     *          routes.
     */
    public Interfacer(SimulationManager s) {
        simulationFile = new Writer("simulation.DOT");
        simulationAnalyzer = new Analyzer();
        stops = s.getBuses();
        routes = s.getRoutes();
    }

    public static void addBus(Bus bus) {
        // Poll Analyzer
        // Update FileWriter
    }

    public static void addRoute(Route route) {
        // Poll Analyzer
        // Update FileWriter
    }

    public static void addStop(Stop stop) {
        // Poll Analyzer
        // Update FileWriter
    }

    public static void extendRoute(Route route, Stop stop) {

    }
    
}
