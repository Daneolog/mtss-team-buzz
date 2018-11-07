package corelogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;

public class ExampleTest {
    /**
     * Test case: Stop1 @ (1,5) and Stop2 @ (1,1), Bus starts at Stop1 with speed 1
     * Expected behavior: After Bus ticks 5 times, Bus arrives at Stop2
     */
    @Test
    public void exampleTest() {
        Stop stop1 = new Stop(1, "Stop1", 1, 5, 1);
        Stop stop2 = new Stop(2, "Stop2", 1, 1, 2);
        ArrayList<Stop> stops = new ArrayList<>();
        stops.add(stop1);
        stops.add(stop2);
        Route route = new Route(2, stops, true);
        Bus bus = new Bus(3, route, 0, 20, 20, 1, 1);
        assertEquals(stop1.getName(), "Stop1");
        assertEquals(stop1.getX(), 1, 0.00001);
        assertEquals(stop1.getY(), 5, 0.00001);
        assertEquals(stop1.getDisembarkRate(), 1, 0.00001);
        assertEquals(stop2.getName(), "Stop2");
        assertEquals(stop2.getX(), 1, 0.00001);
        assertEquals(stop2.getY(), 1, 0.00001);
        assertEquals(stop2.getDisembarkRate(), 2, 0.00001);
        assertTrue(route.isLinear());
        assertEquals(bus.getCurrentStop(), stop1);
        assertTrue(bus.tick(5));
        assertEquals(bus.getCurrentStop(), stop2);
    }
}

