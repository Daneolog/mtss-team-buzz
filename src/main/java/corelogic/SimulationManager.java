package corelogic;

import java.util.*;
import dataanalysis.Interfacer;

public class SimulationManager {
    static private Interfacer DataAnalysis;

    private static HashMap<Integer, Bus> buses;
    private static HashMap<Integer, Stop> stops;
    private static HashMap<Integer, Route> routes;

    public static HashMap<Integer, Bus> getBuses() { return buses; }
    public static HashMap<Integer, Stop> getStops() { return stops; }
    public static HashMap<Integer, Route> getRoutes() { return routes; }

    private static int simTime;
    private static boolean running;

    private static Timer timer;
    private static float interval;

    static class Run extends TimerTask {
        public void run() {
            if (running) {
                tick();
            }
        }
    }

    public static void main(String[] args) {
        initSim("test_scenario_fun.txt", 1);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            scanner.nextLine();
            togglePlay();
        }
    }

    /**
     * Simulate one tick on every simulation entity until a bus arrives at a stop
     */
    public static void MoveNextBus() {
        while (!tick()) {}
    }

    public static void togglePlay() {
        running = !running;
        if (running) {
            timer = new Timer();
            timer.schedule(new Run(), 0, (int)(interval * 1000));
        } else {
            timer.cancel();
        }
    }

    public static boolean tick() {
        boolean busArrived = false;
        ++simTime;
        System.out.println("Simtime: " + simTime);
        for (Stop stop : stops.values()) {
            int num = stop.tick();
            System.out.println(stop.getName() + ": Spawned " + num + " passengers");
        }
        for (Bus bus : buses.values()) {
            boolean busArrivedNow = bus.tick(simTime);
            System.out.println("Bus " + bus.getId() + " is at " + bus.getCurrentStop().getName());
            busArrived = busArrivedNow || busArrived;
        }

        DataAnalysis.updateEffectiveness();
        return busArrived;
    }

    public static void initSim(String path, float interval) {
        buses = new HashMap<>();
        stops = new HashMap<>();
        routes = new HashMap<>();
        simTime = 0;
        
        FileManager.importScenario(path, buses, stops, routes, simTime);
        SimulationManager.interval = interval;
    }
}
