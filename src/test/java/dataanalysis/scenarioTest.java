package dataanalysis;

import corelogic.SimulationManager;
import org.junit.Test;

public class scenarioTest {

    @Test
    public void tests2Test() {
        SimulationManager.initSim("scenarios/test2.txt", null, 1000, 5);
        SimulationManager.tick();
        System.out.println(SimulationManager.getStops().size() + " stops");
        System.out.println(SimulationManager.getRoutes().size() + " routes");
        System.out.println(SimulationManager.getBuses().size() + " buses");
        SimulationManager.takeSnapshot();
    }

}
