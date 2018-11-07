package corelogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimulationManager {

    static private List<Bus> buses;
    static private List<Stop> stops;
    static private List<Route> routes;

    static private int simTime;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initSim(args[0]);
        System.out.println("Bus 1 is at stop " + buses.get(0).getCurrentStop().getName());
        System.out.println("Bus 2 is at stop " + buses.get(1).getCurrentStop().getName());
        while (scanner.hasNext()) {
            scanner.next();
            MoveNextBus();
            System.out.println("Bus 1 is at stop " + buses.get(0).getCurrentStop().getName());
            System.out.println("Bus 2 is at stop " + buses.get(1).getCurrentStop().getName());
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
        for (Stop stop : stops)
            stop.tick();
        for (Bus bus : buses)
            busArrived = bus.tick(simTime) || busArrived;
        return busArrived;
    }

    public static void initSim(String path) {
        buses = new ArrayList<>();
        stops = new ArrayList<>();
        routes = new ArrayList<>();
        simTime = 0;

        FileManager.importScenario(path, buses, stops, routes, simTime);
    }
}
