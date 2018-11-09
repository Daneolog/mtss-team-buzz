package corelogic;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class FileManager {
    static void importScenario(String filename, List<Bus> busList, List<Stop> stopList, List<Route> routeList, int simTime) {
        HashMap<Integer, Bus> buses = new HashMap<>();
        HashMap<Integer, Stop> stops = new HashMap<>();
        HashMap<Integer, Route> routes = new HashMap<>();

        try {
            File file = new File(filename);
            BufferedReader f = new BufferedReader(new FileReader(file));
            String line;

            while ((line = f.readLine()) != null) {
                String[] command = line.split(",");

                switch (command[0]) {
                    case "add_stop":
                        Stop.StopBuilder stopBuilder = new Stop.StopBuilder();
                        stopBuilder.id(Integer.parseInt(command[1])).name(command[2]).x(Double.parseDouble(command[4])).y(Double.parseDouble(command[5]));
                        stops.put(Integer.parseInt(command[1]), stopBuilder.build());
                        break;
                    case "add_route":
                        routes.put(Integer.parseInt(command[1]), new Route(Integer.parseInt(command[1]), new ArrayList<>(), false));
                        break;
                    case "extend_route":
                        routes.get(Integer.parseInt(command[1])).stops.add(stops.get(Integer.parseInt(command[2])));
                        break;
                    case "add_bus":
                        Bus.BusBuilder busBuilder = new Bus.BusBuilder(routes.get(Integer.parseInt(command[2])), simTime);
                        busBuilder.id(Integer.parseInt(command[1])).startStop(Integer.parseInt(command[3])).initPassengers(Integer.parseInt(command[4])).capacity(Integer.parseInt(command[5])).speed(Double.parseDouble(command[8]));
                        buses.put(Integer.parseInt(command[1]), busBuilder.build());
                        break;
                    case "add_event":
                        // aren't we doing this dynamically in simulation...?
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found, please enter valid filename");
        } catch (IOException e) {
            System.err.println("I have no idea why this would be thrown");
        }

        busList.addAll(buses.values());
        stopList.addAll(stops.values());
        routeList.addAll(routes.values());
    }
}
