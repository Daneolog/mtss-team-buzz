import org.junit.Test;
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
    }
}