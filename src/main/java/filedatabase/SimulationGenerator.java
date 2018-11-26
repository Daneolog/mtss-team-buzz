package filedatabase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class SimulationGenerator {
    DBClass dbClass;

    public SimulationGenerator(DBClass dbClass) {
        this.dbClass = dbClass;
    }

    public String createSimulationFile(Date startDate, Date endDate) {
        HashSet<Integer> routesInDateRange = new HashSet<>();
        HashSet<Integer> busesInDateRange = new HashSet<>();
        HashMap<Integer, Integer> routeLengthMap = new HashMap<>();
        HashMap<Integer, Integer> routeLastLocationMap = new HashMap<>();
        ArrayList<String> addBusCommands = new ArrayList<>();
        ArrayList<String> addRouteCommands = new ArrayList<>();
        HashSet<String> addStopCommands = new HashSet<>();
        ArrayList<String> extendRouteCommands = new ArrayList<>();
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
                int bus_id = resultList.get(0).getInt(1);
                int route_id = resultList.get(0).getInt(2);
                String route_name = resultList.get(0).getString(3);
                if(!busesInDateRange.contains(bus_id)) {
                    if(!routesInDateRange.contains(route_id)) {
                        addRouteCommands.add(String.format("add_route,%d,%d,%s\n",
                            route_id, route_id, route_name));
                        ResultSet routeOrderRs = this.dbClass.getRouteOrder(route_id);
                        routeLengthMap.put(route_id, 0);
                        while(routeOrderRs.next()) {
                            addStopCommands.add(String.format("add_stop,%d,%s,%d,%s,%s\n",
                                routeOrderRs.getInt(1),
                                routeOrderRs.getString(2),
                                5,
                                routeOrderRs.getBigDecimal(3).toString(),
                                routeOrderRs.getBigDecimal(4).toString()));
                            extendRouteCommands.add(String.format("extend_route,%d,%d\n",
                                route_id,
                                routeOrderRs.getInt(1)));
                            int length = routeLengthMap.get(route_id);
                            routeLengthMap.put(route_id, length+1);
                        }
                    }
                    routesInDateRange.add(route_id);
                    int routeLength = 0;
                    if(routeLengthMap.containsKey(route_id)) {
                        routeLength = routeLengthMap.get(route_id);
                    }
                    int routeLoc = 0;
                    if(routeLastLocationMap.containsKey(route_id)) {
                        routeLoc = routeLastLocationMap.get(route_id) + 1;
                    }
                    routeLastLocationMap.put(route_id, routeLoc);
                    addBusCommands.add(String.format("add_bus,%d,%d,%d,100,10\n",
                        bus_id, route_id, routeLoc % routeLength));
                }
                busesInDateRange.add(bus_id);
            }
            resultList.get(0).close();

            while(resultList.get(1).next()) {
                //routesInDateRange.add(resultList.get(1).getInt(1));
            }
            resultList.get(1).close();

            for(String command : addStopCommands) {
                simulationFile.append(command);
            }
            for(String command : addRouteCommands) {
                simulationFile.append(command);
            }
            for(String command : extendRouteCommands) {
                simulationFile.append(command);
            }
            for(String command : addBusCommands) {
                simulationFile.append(command);
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            return "";
        }

        return simulationFile.toString();
    }

    public void writeSimulationFile(Date startDate, Date endDate, String filePath) throws IOException {
        File file = new File(filePath);
        if(file.exists()) {
            if(file.isDirectory()) {
                throw new IOException("Cannot write simulation to directory, please specify a file path");
            }
            if(!file.delete()) {
                throw new IOException("File already exists and could not be deleted");
            }
        }
        FileWriter writer = new FileWriter(file);
        writer.write(this.createSimulationFile(startDate, endDate));
    }
}
