package corelogic;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import filedatabase.DBClass;
import filedatabase.SimulationGenerator;

public class SimulationManager {

    static private List<Bus> buses;
    static private List<Stop> stops;

    static private int simTime;

    public static void main(String[] args) throws ParseException {
        DBClass dbclass = new DBClass("68.183.20.174", 5432, "cs3300",
                                      "cs3300_readonly", "cs3300_b80ed2986e");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimulationGenerator sg = new SimulationGenerator(dbclass);
        System.out.println(sg.createSimulationFile(
            format.parse("2016/07/01 12:30:00"),
            format.parse("2016/07/01 13:35:00")));
    }

    /**
     * Simulate one tick on every simulation entity until a bus arrives at a stop
     */
    private static void MoveNextBus() {
        while (!Tick()) {}
    }

    private static boolean Tick() {
        boolean busArrived = false;
        ++simTime;
        for (Stop stop : stops)
            stop.Tick();
        for (Bus bus : buses)
            busArrived = bus.Tick(simTime) || busArrived;
        return busArrived;
    }

    private static void InitSim() {
        buses = new ArrayList<>();
        stops = new ArrayList<>();
        simTime = 0;

        //Sample init
        stops.add(new Stop("Downtown", 0, -10));
        stops.add(new Stop("Midtown", 0, 0));
        List<Stop> routeStops = new ArrayList<>(stops);
        Route route = new Route(routeStops, false);
        buses.add(new Bus(route, 0, 5, simTime));
    }
}
