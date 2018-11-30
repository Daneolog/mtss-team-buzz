package filedatabase;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

public class ProbDistTest {

    @Test
    public void test_correct_prob_dist_file() throws ParseException {
        DBClass dbclass = new DBClass("68.183.20.174", 5432, "cs3300",
                "cs3300_readonly", "cs3300_b80ed2986e");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimulationGenerator sg = new SimulationGenerator(dbclass);
        try {
            sg.writeSimulationFile(
                    format.parse("2016/07/01 12:30:00"),
                    format.parse("2016/07/01 13:35:00"),
                    "C:\\Users\\Michael Sherman\\Desktop\\simulation.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void correct_path() {
        String filePath = "C:\\Users\\Michael Sherman\\Desktop\\simulation.txt";
        System.out.println(filePath);

        int cutOff = filePath.lastIndexOf("\\");
        String probFilePath = filePath.substring(0, cutOff + 1) + "simprobability.txt";
        System.out.println(probFilePath);
    }
}
