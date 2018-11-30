package corelogic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class ExampleTest {
    /**
     * Example test checking behavior of Stops, Buses, and Routes
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

    /**
     * Test 1 from Core Logic Test Plan
     * Bus does not prematurely move to next stop before arrival time
     * Test Case:
     *     - 1 Route with 2 Stops 10 miles apart
     *     - 2 Buses:
     *         - Bus 1 with speed 2 and startStop 1
     *         - Bus 2 with speed 5 and startStop 2
     * Expected Behavior:
     *     - First tick() call returns false because neither bus has reached the next stop
     *     - Second tick() call returns true because bus 2 reaches the next stop
     *     - After 2 tick() calls, both buses are at Stop 1
     */
    @Test
    public void test1() {
        SimulationManager.initSim("scenarios/test_scenario_moveNextBus.txt", 1000, 5);

        assertFalse(SimulationManager.tick());
        assertTrue(SimulationManager.tick());
        assertEquals(SimulationManager.getBuses().get(3).getCurrentStop(), SimulationManager.getStops().get(0));
        assertEquals(SimulationManager.getBuses().get(4).getCurrentStop(), SimulationManager.getStops().get(0));
    }

    /**
     * Test 2 from Core Logic Test Plan
     * Bus does not prematurely remove passengers
     * Test Case:
     *     - 1 Route with 2 Stops 10 miles apart
     *     - 1 Bus with speed 2, startStop 1, and 10 passengers
     * Expected Behavior:
     *     - First 4 tick() calls return false because the Bus has not reached the next stop
     *     - After 4 tick() calls, the Bus still has 10 passengers
     */
    @Test
    public void test2() {
        SimulationManager.initSim("scenarios/test_scenario_passengers.txt", 1000, 5);

        for (int i = 0; i < 4; i++) {
            assertFalse(SimulationManager.tick());
        }
        assertEquals(10, SimulationManager.getBuses().get(3).getNumPassengers());
    }

    /**
     * Test 3 from Core Logic Test Plan
     * Stop spawns passengers
     * Test Case:
     *     - 1 Route with 2 Stops each with 0 Passengers
     * Expected Behavior:
     *     - 10 tick() calls return false because the no Bus ever reaches the next Stop
     *     - After 10 tick() calls, both of the Stops have spawned at least one Passenger
     */
    @Test
    public void test3() {
        SimulationManager.initSim("scenarios/test_scenario_stop.txt", 1000, 5);

        for (int i = 0; i < 10; i++) {
            assertFalse(SimulationManager.tick());
        }
        assertNotEquals(0, SimulationManager.getStops().get(0).passengerQueue.size());
        assertNotEquals(0, SimulationManager.getStops().get(1).passengerQueue.size());
    }

    @Test
    public void test4() {
        SimulationManager.initSim("scenarios/test_scenario_unload.txt", 1000, 5);

        for (int i = 1; i <= 99; i++) {
            if (i % 3 != 0) {
                assertFalse(SimulationManager.tick());
            } else {
                assertTrue(SimulationManager.tick());
            }
            assertTrue(SimulationManager.getBuses().get(3).getNumPassengers() <= 20);
        }
    }
}