package filedatabase;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class SimulationGenerator {
    DBClass dbClass;
    SimulationFile simulationFile;
    //todo: add its own coreLogic attribute here (where we are sending it to)

    public SimulationGenerator(DBClass dbClass) {
        this.dbClass = dbClass;


    }

    public boolean create(String startDate, String endDate) {
        if(dbClass.connect() == false) {
            System.err.println("Could not connect to the database.");
            simulationFile = null;
            return false;
        }

        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter("example.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        printWriter.write();

        printWriter.close();
        return true;
    }

    public boolean send(SimulationFile simulationFile) {
        return false;
    }



}
