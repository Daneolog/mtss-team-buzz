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
    ArrayList<String> probDistFile = new ArrayList<>();

    public SimulationGenerator(DBClass dbClass) {
        this.dbClass = dbClass;
    }

    public ArrayList<String> createSimulationFile(Date startDate, Date endDate) {
        HashSet<Integer> routesInDateRange = new HashSet<>();
        HashSet<Integer> busesInDateRange = new HashSet<>();
        HashMap<Integer, Integer> routeLengthMap = new HashMap<>();
        HashMap<Integer, Integer> routeLastLocationMap = new HashMap<>();
        ArrayList<String> addBusCommands = new ArrayList<>();
        ArrayList<String> addRouteCommands = new ArrayList<>();
        HashSet<String> addStopCommands = new HashSet<>();
        ArrayList<String> extendRouteCommands = new ArrayList<>();

        ArrayList<String> simulationFile = new ArrayList<>();

        HashMap<Integer, Integer> stopMinPassengersOn = new HashMap<>();
        HashMap<Integer, Integer> stopMaxPassengersOn = new HashMap<>();
        HashMap<Integer, Integer> stopMinPassengersOff = new HashMap<>();
        HashMap<Integer, Integer> stopMaxPassengersOff = new HashMap<>();
        int stop_ID_Count = 0;

        if (this.dbClass.connect() == false) {
            System.err.println("Could not connect to the database.");
            simulationFile = null;
            return null;
        }

        ArrayList<ResultSet> resultList = this.dbClass.getBusLocationsInDateRange(
                startDate, endDate);
        if (resultList.size() != 2) {
            return null;
        }

        try {
            while (resultList.get(0).next()) {
                int bus_id = resultList.get(0).getInt(1);
                int route_id = resultList.get(0).getInt(2);
                String route_name = resultList.get(0).getString(3);
                if (!busesInDateRange.contains(bus_id)) {
                    if (!routesInDateRange.contains(route_id)) {
                        addRouteCommands.add(String.format("add_route,%d,%s\n",
                                route_id, route_name));
                        ResultSet routeOrderRs = this.dbClass.getRouteOrder(route_id);
                        routeLengthMap.put(route_id, 0);
                        while (routeOrderRs.next()) {
                            stop_ID_Count++;
                            addStopCommands.add(String.format("add_stop,%d,%s,%s,%s\n",
                                    routeOrderRs.getInt(1),
                                    routeOrderRs.getString(2),
                                    routeOrderRs.getBigDecimal(3).toString(),
                                    routeOrderRs.getBigDecimal(4).toString()));
                            extendRouteCommands.add(String.format("extend_route,%d,%d\n",
                                    route_id,
                                    routeOrderRs.getInt(1)));
                            int length = routeLengthMap.get(route_id);
                            routeLengthMap.put(route_id, length + 1);
                        }
                    }
                    routesInDateRange.add(route_id);
                    int routeLength = 0;
                    if (routeLengthMap.containsKey(route_id)) {
                        routeLength = routeLengthMap.get(route_id);
                    }
                    int routeLoc = 0;
                    if (routeLastLocationMap.containsKey(route_id)) {
                        routeLoc = routeLastLocationMap.get(route_id) + 1;
                    }
                    routeLastLocationMap.put(route_id, routeLoc);
                    addBusCommands.add(String.format("add_bus,%d,%d,%d,100,10\n",
                            bus_id, route_id, routeLoc % routeLength));
                }
                busesInDateRange.add(bus_id);
            }
            resultList.get(0).close();

            while (resultList.get(1).next()) {
                int stop_id = resultList.get(1).getInt(1);
                int passenger_ons = resultList.get(1).getInt(2);
                int passenger_offs = resultList.get(1).getInt(3);

                if(!stopMinPassengersOn.containsKey(stop_id)) {
                    stopMinPassengersOn.put(stop_id, passenger_ons);
                } else if(stopMinPassengersOn.get(stop_id) > passenger_ons) {
                    stopMinPassengersOn.put(stop_id, passenger_ons);
                }

                if(!stopMaxPassengersOn.containsKey(stop_id)) {
                    stopMaxPassengersOn.put(stop_id, passenger_ons);
                } else if(stopMaxPassengersOn.get(stop_id) < passenger_ons) {
                    stopMaxPassengersOn.put(stop_id, passenger_ons);
                }

                if(!stopMinPassengersOff.containsKey(stop_id)) {
                    stopMinPassengersOff.put(stop_id, passenger_offs);
                } else if(stopMinPassengersOff.get(stop_id) > passenger_offs) {
                    stopMinPassengersOff.put(stop_id, passenger_offs);
                }

                if(!stopMaxPassengersOff.containsKey(stop_id)) {
                    stopMaxPassengersOff.put(stop_id, passenger_offs);
                } else if(stopMaxPassengersOff.get(stop_id) < passenger_offs) {
                    stopMaxPassengersOff.put(stop_id, passenger_offs);
                }

            }
//            resultList.get(1).close();
            resultList.get(1).beforeFirst();

            while (resultList.get(1).next()) {
                int stop_id = resultList.get(1).getInt(1);
                probDistFile.add(String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
                        stop_id,
                        stopMaxPassengersOn.getOrDefault(stop_id, 0),
                        stopMinPassengersOn.getOrDefault(stop_id, 0),
                        stopMaxPassengersOff.getOrDefault(stop_id, 0),
                        stopMinPassengersOff.getOrDefault(stop_id, 0),
                        stopMaxPassengersOn.getOrDefault(stop_id, 0),
                        stopMinPassengersOn.getOrDefault(stop_id, 0),
                        stopMaxPassengersOff.getOrDefault(stop_id, 0),
                        stopMinPassengersOff.getOrDefault(stop_id, 0)
                        ));
            }
            resultList.get(1).close();
            System.out.println(probDistFile);

            for (String command : addStopCommands) {
                simulationFile.add(command);
            }
            for (String command : addRouteCommands) {
                simulationFile.add(command);
            }
            for (String command : extendRouteCommands) {
                simulationFile.add(command);
            }
            for (String command : addBusCommands) {
                simulationFile.add(command);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return simulationFile;
    }

    private void createProbDistFile(String filePath) throws IOException {
        int cutOff = filePath.lastIndexOf("\\");
        String probFilePath = filePath.substring(0, cutOff + 1) + "simprobability.txt";
        File file = new File(probFilePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("Cannot write simulation to directory, please specify a file path");
            }
            if (!file.delete()) {
                throw new IOException("File already exists and could not be deleted");
            }
        }
        FileWriter writer = new FileWriter(file);

        for (String row : probDistFile) {
            writer.write(row);
        }
        writer.close();
    }

    public void writeSimulationFile(Date startDate, Date endDate, String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("Cannot write simulation to directory, please specify a file path");
            }
            if (!file.delete()) {
                throw new IOException("File already exists and could not be deleted");
            }
        }
        FileWriter writer = new FileWriter(file);

        ArrayList<String> simulationFile = this.createSimulationFile(startDate, endDate);
        for (String command : simulationFile) {
            writer.write(command);
        }
        writer.close();

        createProbDistFile(filePath);
    }
}
