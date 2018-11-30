package dataanalysis;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import corelogic.Bus;
import corelogic.Route;
import corelogic.Stop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InterfacerTest {

    HashMap<Integer, Stop> stops = null;
    HashMap<Integer, Route> routes = null;
    HashMap<Integer, Bus> buses = null;

    @Before
    public void setup() {
        stops = new HashMap<>();
        routes = new HashMap<>();
        buses = new HashMap<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullExceptionTest() {
        Interfacer testInterfacer = new Interfacer(buses, null, routes,
                "simulation.DOT");

    }

    @Test
    public void constructorTest() {
        stops.put(0, new Stop(1, "Harold Dr. and 5th", 0.4, 7.0, 1.0));
        Interfacer testInterfacer = new Interfacer(buses, stops, routes,
                "simulation.DOT");
        assertEquals(testInterfacer.getStops().get(0).getName(), "Harold Dr. and 5th");
        assertEquals(testInterfacer.getStops().get(0).getX(), 0.4, 0.01);
        assertEquals(testInterfacer.getStops().get(0).getX(), 0.4, 0.01);
    }

    @Test
    public void simulationTest() {

        Stop stop1 = new Stop(0, "CULC", 0.4, 7.0, 1);
        Stop stop2 = new Stop(1, "CRC", 0.6, 1.5, 2);
        Stop stop3 = new Stop(2, "Student Center", 0.9, 4.3, 3);
        HashMap<Integer, Stop> stops = new HashMap();
        stops.put(0, stop1);
        stops.put(1, stop2);
        stops.put(2, stop3);

        List<Stop> s1 = new ArrayList<>();
        s1.addAll(stops.values());
        Route route1 = new Route(0, s1, false);
        HashMap<Integer, Route> routes = new HashMap<>();
        routes.put(0, route1);

        Bus bus1 = new Bus(0, route1, 0, 10, 50, 50, 0);
        Bus bus2 = new Bus(1, route1, 1, 5, 50, 40, 0);
        HashMap<Integer, Bus> buses = new HashMap<>();
        buses.put(0, bus1);
        buses.put(1, bus2);

        // Initialize Dummy Simulation

        Interfacer test = new Interfacer(buses, stops, routes, "dummytest.DOT");
        test.dummySimulationInit(buses, stops, routes);
        // At this moment, route effectiveness should be 52.5
        test.updateEffectiveness();
        assertEquals(52.5, test.getEffectiveness(), 0.1);
    }
}
