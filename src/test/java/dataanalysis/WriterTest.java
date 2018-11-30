package dataanalysis;

import corelogic.Bus;
import corelogic.Route;
import corelogic.Stop;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class WriterTest {

    HashMap<Integer, Stop> stops = null;
    HashMap<Integer, Route> routes = null;
    HashMap<Integer, Bus> buses = null;

    @Before
    public void setup() {
        stops = new HashMap<>();
        routes = new HashMap<>();
        buses = new HashMap<>();
    }

    /***
     * This graph contains 3 routes, one cyclic, one acyclic, and one
     * separated from the other two that is cyclic.
     */
    @Test
    public void writerTest() {
        stops.put(0, new Stop(0, "CULC", 0.1, 2, 0.45));
        stops.put(1, new Stop(1, "Tech Square", 3.1, 0.4, 2.03));
        stops.put(2, new Stop(2, "IC", 1.2, 0.89, 0.89));
        stops.put(3, new Stop(3, "Van Leer", 1.2, 0.89, 0.89));
        stops.put(4, new Stop(4, "Biotech Quad", 1.2, 0.89, 0.89));
        stops.put(5, new Stop(5, "Student Center", 1.2, 0.89, 0.89));
        stops.put(6, new Stop(6, "Home Park", 3.0, 2.3, 1.0));
        stops.put(7, new Stop(7, "Atlantic Station", 1.0, 0.1, 2.54));

        ArrayList<Stop> r1 = new ArrayList<>();
        ArrayList<Stop> r2 = new ArrayList<>();
        ArrayList<Stop> r3 = new ArrayList<>();
        r1.add(stops.get(0));
        r1.add(stops.get(1));
        r1.add(stops.get(2));
        r1.add(stops.get(3));
        r2.add(stops.get(2));
        r2.add(stops.get(3));
        r2.add(stops.get(4));
        r2.add(stops.get(5));
        r3.add(stops.get(6));
        r3.add(stops.get(7));
        Route route1 = new Route(0, r1, false);
        Route route2 = new Route(1, r2, true);
        Route route3 = new Route(2, r3, false);
        routes.put(0, route1);
        routes.put(1, route2);
        routes.put(2, route3);

        Interfacer testInterfacer = new Interfacer(buses, stops, routes,
                "full.DOT");
        testInterfacer.createGraph();

    }

    /***
     * This graph contains one cyclic route with 4 stops.
     */
    @Test
    public void cycleWriterTest() {
        stops.put(0, new Stop(0, "CULC", 0.1, 2, 0.45));
        stops.put(1, new Stop(1, "Tech Square", 3.1, 0.4, 2.03));
        stops.put(2, new Stop(2, "IC", 1.2, 0.89, 0.89));
        stops.put(3, new Stop(3, "Van Leer", 1.2, 0.89, 0.89));

        ArrayList<Stop> r1 = new ArrayList<>();
        r1.add(stops.get(0));
        r1.add(stops.get(1));
        r1.add(stops.get(2));
        r1.add(stops.get(3));
        Route route1 = new Route(0, r1, false);
        routes.put(0, route1);
        Interfacer testInterfacer = new Interfacer(buses, stops, routes,
                "cycle.DOT");
        testInterfacer.createGraph();
    }

    /***
     * This graph contains one acyclic route with 4 stops.
     */
    @Test
    public void dagWriterTest() {
        stops.put(0, new Stop(2, "IC", 1.2, 0.89, 0.89));
        stops.put(1, new Stop(3, "Van Leer", 1.2, 0.89, 0.89));
        stops.put(2, new Stop(4, "Biotech Quad", 1.2, 0.89, 0.89));
        stops.put(3, new Stop(5, "Student Center", 1.2, 0.89, 0.89));

        ArrayList<Stop> r1 = new ArrayList<>();
        r1.add(stops.get(0));
        r1.add(stops.get(1));
        r1.add(stops.get(2));
        r1.add(stops.get(3));
        Route route1 = new Route(0, r1, true);
        routes.put(0, route1);
        Interfacer testInterfacer = new Interfacer(buses, stops, routes,
                "dag.DOT");
        testInterfacer.createGraph();

    }

    /***
     * This graph contains no stops or routes, but still creates an empty
     * digraph object in the .DOT file.
     */
    @Test
    public void emptyGraphTest() {
        Interfacer testInterfacer = new Interfacer(buses, stops, routes,
                "empty.DOT");
        testInterfacer.createGraph();
    }
}
