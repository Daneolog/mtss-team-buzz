package corelogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SimulationManager {

    static private List<Bus> buses;
    static private List<Stop> stops;
    static private List<Route> routes;

    static private int simTime;
    static private boolean running;

    static class Run extends TimerTask {
        public void run() {
            if (running) {
                Tick();
                simTime++;
            }
            System.out.println("Bus 1 is at stop " + buses.get(0).getCurrentStop().getName());
        }
    }

    public static void main(String[] args) {
        InitSim(1);

        interrupt();
    }

    /**
     * Simulate one tick on every simulation entity until a bus arrives at a stop
     */
    private static void MoveNextBus() {
        while (!Tick()) {}
    }

    private static boolean Tick() {
        boolean busArrived = false;
        for (Stop stop : stops)
            stop.Tick();
        for (Bus bus : buses)
            busArrived = bus.Tick(simTime) || busArrived;
        return busArrived;
    }

    private static void interrupt() {
        running = !running;
    }

    private static void InitSim(float interval) {
        buses = new ArrayList<>();
        stops = new ArrayList<>();
        routes = new ArrayList<>();
        simTime = 0;

        //Sample init
//        stops.add(new Stop(0, "Downtown", 0, -10));
//        stops.add(new Stop(1, "Midtown", 0, 0));
//        List<Stop> routeStops = new ArrayList<>(stops);
//        Route route = new Route(0, routeStops, false);
//        buses.add(new Bus(0, route, 0, 5, simTime));

        FileManager.importScenario("test_scenario_fun.txt", buses, stops, routes, simTime);
        Timer timer = new Timer();
        timer.schedule(new Run(), 1000, (int)(interval * 1000));
    }
}
