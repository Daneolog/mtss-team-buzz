import static org.junit.Assert.assertEquals;
import org.junit.Test;
import main.java.corelogic.Stop;

public class StopTest {
    @Test
    public void exampleTest() {
        Stop stop = new Stop(1, "Example stop", 4.3, 3.2);
        assertEquals(stop.getName(), "Example stop");
        assertEquals(stop.getX(), 4.3, 0.00001);
        assertEquals(stop.getY(), 3.2, 0.00001);
    }
}
