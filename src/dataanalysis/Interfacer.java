package dataanalysis;

// Imports
import corelogic.Bus;
import corelogic.Route;
import corelogic.SimulationManager;
import corelogic.Stop;
import java.util.List;
import java.util.ArrayList;

/**
 * Hub that interfaces with Core Logic (Maybe also Database), Analyzer, and File Writer.
 */
public class Interfacer {

    private FileWriter simulationFile;
    private List<Bus> buses;
    private List<Stop> stops;

    public Interfacer(SimulationManager s) {
        buses = s.getBuses();
        stops = s.getStops();
    }

    public static void addBus(Bus bus) {
    
    }

    public static void addRoute(Route route) {
    
    }

    public static void addStop(Stop stop) {

    }

    public static void extendRoute(Route route, Stop stop) {

    }
    
}
