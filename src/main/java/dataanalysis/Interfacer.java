package main.java.dataanalysis;

// Imports
import main.java.corelogic.Bus;
import main.java.corelogic.Route;
import main.java.corelogic.Stop;
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

    private double effectiveness;

    /***
     * Initiates FileWriter and Analyzer objects.
     * Initiates new references to Core Logic's stop/route ArrayLists.
     *
     */
    public Interfacer() {
        simulationFile = new Writer("simulation.DOT");
        stops = new ArrayList<Stop>();
        routes = new ArrayList<Route>();
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

    public void addStop(Stop stop) throws IllegalArgumentException {
        if (stop == null) {
            throw new IllegalArgumentException("Stop may not be null.");
        }
        stops.add(stop);
        // Poll Analyzer
        int score = Analyzer.addStop(stop);
        // Update FileWriter
    }

    public void addBus(Bus bus) throws IllegalArgumentException {
        if (bus == null) {
            throw new IllegalArgumentException("Buss may not be null.");
        }
        // Poll Analyzer
        // Update FileWriter
    }

    public void addRoute(Route route) {
        // Poll Analyzer
        // Update FileWriter
    }

    public void extendRoute(Route route, Stop stop) {

    }

    public void dummySimulationInit(List<Bus> buses, List<Stop> stops, List<Route> routes) {
        this.buses = buses;
        this.stops = stops;
        this.routes = routes;
    }

    /**
     * Updates effectiveness of the route in a "snapshot" approach
     */
    public void updateEffectiveness() {
        double totalCost = 0;
        for (Bus bus: buses) {
            totalCost = totalCost + bus.getSpeed() + bus.getPassengers().size();
        }

        double totalWaitTime = 0;
        for (Stop stop: stops) {
            totalWaitTime = totalWaitTime + stop.getPassengerQueue().size();
        }

        this.effectiveness = (totalCost / buses.size()) + (totalWaitTime / stops.size());
    }
}
