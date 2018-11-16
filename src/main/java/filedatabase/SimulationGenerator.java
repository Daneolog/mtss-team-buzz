package filedatabase;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class SimulationGenerator {
    DBClass dbClass;
    SimulationFile simulationFile;

    public SimulationGenerator(DBClass dbClass) {
        this.dbClass = dbClass;
    }

    public String createSimulationFile(Date startDate, Date endDate) {
        HashSet<Integer> routesInDateRange = new HashSet<>();
        HashMap<Integer, Integer> busRouteMap = new HashMap<>();
        StringBuilder simulationFile = new StringBuilder();

        if(this.dbClass.connect() == false) {
            System.err.println("Could not connect to the database.");
            simulationFile = null;
            return "asdf";
        }

        ArrayList<ResultSet> resultList = this.dbClass.getBusLocationsInDateRange(
            startDate, endDate);
        if(resultList.size() != 2) {
            return String.format("bbbb %d", resultList.size());
        }

        try {
            while(resultList.get(0).next()) {
                int route_id = resultList.get(0).getInt(1);
                String route_name = resultList.get(0).getString(2);
                if(!routesInDateRange.contains(route_id)) {
                    simulationFile.append(String.format("add_route,%d,%d,%s\n",
                        route_id, route_id, route_name));
                    ResultSet routeOrderRs = this.dbClass.getRouteOrder(route_id);
                    while(routeOrderRs.next()) {
                        simulationFile.append(String.format("add_stop,%d,%s,%d,%s,%s\n",
                            routeOrderRs.getInt(1),
                            routeOrderRs.getString(2),
                            5,
                            routeOrderRs.getBigDecimal(3).toString(),
                            routeOrderRs.getBigDecimal(4).toString()));
                        simulationFile.append(String.format("extend_route,%d,%d\n",
                            route_id,
                            routeOrderRs.getInt(1)));
                    }
                }
                routesInDateRange.add(route_id);
            }
            resultList.get(0).close();

            while(resultList.get(1).next()) {
                //routesInDateRange.add(resultList.get(1).getInt(1));
            }
            resultList.get(1).close();
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return "";
        }

        return simulationFile.toString();
    }

    public boolean send(SimulationFile simulationFile) {
        return false;
    }



}
