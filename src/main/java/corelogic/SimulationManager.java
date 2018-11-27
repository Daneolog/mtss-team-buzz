package corelogic;

import java.util.*;
import dataanalysis.Interfacer;

public class SimulationManager {
    static private Interfacer dataAnalysis;

    private static HashMap<Integer, Bus> buses;
    private static HashMap<Integer, Stop> stops;
    private static HashMap<Integer, Route> routes;

    public static HashMap<Integer, Bus> getBuses() { return buses; }
    public static HashMap<Integer, Stop> getStops() { return stops; }
    public static HashMap<Integer, Route> getRoutes() { return routes; }

    private static int simTime;
    private static boolean running;

    public static int getSimTime() { return simTime; }

    private static Timer timer;
    private static int interval;
    private static float fastForwardMultiplier;
    private static boolean isFast = false;

    static class Run extends TimerTask {
        public void run() {
            if (running) {
                tick();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please include a path to a simulation file");
            return;
        }
        dataAnalysis = new Interfacer();
        initSim(args[0], 1000, 5);

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

    /**
     * Toggles automatic ticking of simulation
     */
    public static void togglePlay() {
        running = !running;
        if (running) {
            timer = new Timer();
            timer.schedule(new Run(), 0, (int)(interval));
        } else {
            timer.cancel();
        }
    }

    /**
     * Toggles fast forward mode
     */
    public static void toggleFastForward() {
        interval = isFast ? interval : (int) (interval * fastForwardMultiplier);
    }

    /**
     * Simulates one time unit of simulation
     * @return true if a bus reached a stop, otherwise false
     */
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

        dataAnalysis.updateEffectiveness();
        return busArrived;
    }

    /**
     * Initializes the simulation with the given file path and tick interval
     * @param path Path to simulation file
     * @param interval Interval in milliseconds to tick
     * @param fastForwardMultiplier Multiplier for fast forward mode
     */
    public static void initSim(String path, int interval, float fastForwardMultiplier) {
        buses = new HashMap<>();
        stops = new HashMap<>();
        routes = new HashMap<>();
        simTime = 0;
        
        FileManager.importScenario(path, buses, stops, routes, simTime);
        SimulationManager.interval = interval;
        SimulationManager.fastForwardMultiplier = fastForwardMultiplier;
    }
}
