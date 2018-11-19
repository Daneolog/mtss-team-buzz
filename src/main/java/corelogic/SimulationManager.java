package corelogic;

import dataanalysis.Interfacer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimulationManager {
    static private Interfacer DataAnalysis;

    // TODO: Pass this stuff to data analysis
    static private List<Bus> buses;
    static private List<Stop> stops;
    static private List<Route> routes;

    public static List<Bus> getBuses() { return buses; }
    public static List<Stop> getStops() { return stops; }
    public static List<Route> getRoutes() { return routes; }

    // For Testing Purposes Only
    public static Interfacer getDataAnalysis() { return DataAnalysis; }

    static private int simTime;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initSim(args[0]);
        System.out.println("Simulation initialized");
        while (scanner.hasNext()) {
            scanner.next();
            tick();
        }
    }

    /**
     * Simulate one tick on every simulation entity until a bus arrives at a stop
     */
    private static void MoveNextBus() {
        while (!tick()) {}
    }

    private static boolean tick() {
        boolean busArrived = false;
        ++simTime;
        System.out.println("Simtime: " + simTime);
        for (Stop stop : stops) {
            int num = stop.tick();
            System.out.println(stop.getName() + ": Spawned " + num + " passengers");
        }
        for (Bus bus : buses) {
            boolean busArrivedNow = bus.tick(simTime);
            System.out.println("Bus " + bus.getId() + " is at " + bus.getCurrentStop().getName());
            busArrived = busArrivedNow || busArrived;
        }

        DataAnalysis.updateEffectiveness();
        return busArrived;
    }

    public static void initSim(String path) {
        buses = new ArrayList<>();
        stops = new ArrayList<>();
        routes = new ArrayList<>();
        simTime = 0;
        DataAnalysis = new Interfacer(buses, stops, routes, "simulation.DOT");

        FileManager.importScenario(path, buses, stops, routes, simTime);
    }
}