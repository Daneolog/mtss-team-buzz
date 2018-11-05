import org.junit.Test;
import static org.junit.Assert.assertEquals;
import dataanalysis.Interfacer;
import corelogic.Stop;

public class InterfacerTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullExceptionTest() {
        Interfacer testInterfacer = new Interfacer();
        Stop stop = null;
        testInterfacer.addStop(stop);
    }

    @Test
    public void constructorTest() {
        Interfacer testInterfacer = new Interfacer();
        testInterfacer.addStop(new Stop("Harold Dr. and 5th", 0.4, 7.0));
        assertEquals(testInterfacer.getStops().get(0).getName(), "Harold Dr. and 5th");
        assertEquals(testInterfacer.getStops().get(0).getX(), 0.4, 0.01);
        assertEquals(testInterfacer.getStops().get(0).getX(), 0.4, 0.01);
    }
}
