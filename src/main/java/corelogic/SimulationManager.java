package corelogic;

import java.util.*;

public class SimulationManager {

    private static List<Bus> buses;
    private static List<Stop> stops;
    private static List<Route> routes;

    public static List<Bus> getBuses() { return buses; }
    public static List<Stop> getStops() { return stops; }
    public static List<Route> getRoutes() { return routes; }

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
            interrupt();
        }
    }

    /**
     * Simulate one tick on every simulation entity until a bus arrives at a stop
     */
    public static void MoveNextBus() {
        while (!tick()) {}
    }

    private static void interrupt() {
        running = !running;
        if (running) {
            timer = new Timer();
            timer.schedule(new Run(), 0, (int)(interval * 1000));
        } else {
            timer.cancel();
        }
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
        return busArrived;
    }

    public static void initSim(String path, float interval) {
        buses = new ArrayList<>();
        stops = new ArrayList<>();
        routes = new ArrayList<>();
        simTime = 0;

        FileManager.importScenario(path, buses, stops, routes, simTime);
        SimulationManager.interval = interval;
    }
}
