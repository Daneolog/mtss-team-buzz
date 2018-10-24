package dataanalysis;

// Imports
import src.corelogic.Bus;
import src.corelogic.Route;
import src.corelogic.Stop;
import java.util.ArrayList;

/**
 * Hub that interfaces with Core Logic (Maybe also Database), Analyzer, and File Writer.
 */
public class Interfacer {

    static FileWriter simulationFile = null;
    static List<Bus> buses = null;
    static List<Route> routes = null;

    public static simulationInit() {
        simulationFile = new FileWriter();

    }

    public static addBus(Bus bus) {
    
    }

    public static addRoute(Route route) {
    
    }

    public static addStop(Stop stop) {

    }

    public static extendRoute(Route route, Stop stop) {

    }
    
}
