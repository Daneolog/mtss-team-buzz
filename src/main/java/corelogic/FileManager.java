package corelogic;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class FileManager {
    static void importScenario(String scenarioFilename, String distributionFilename, HashMap<Integer, Bus> buses, HashMap<Integer, Stop> stops, HashMap<Integer, Route> routes, int simTime) {

        try {
            File scenario = new File(scenarioFilename);
            File distribution = new File(distributionFilename);
            BufferedReader s = new BufferedReader(new FileReader(scenario));
            BufferedReader d = new BufferedReader(new FileReader(distribution));
            String line;

            HashMap<Integer, String> distributions = new HashMap<>();

            for (int i = 0; (line = d.readLine()) != null; ++i) {
                String[] split = line.split(",", 2);
                distributions.put(Integer.parseInt(split[0]), split[1]);
            }

            for (int i = 0; (line = s.readLine()) != null; ++i) {
                String[] command = line.split(",");
                Route r;
                Stop s1, s2;
                Bus b;
                switch (command[0]) {
                    case "add_stop":
                        Stop.StopBuilder stopBuilder = new Stop.StopBuilder();
                        stopBuilder.id(Integer.parseInt(command[1])).name(command[2]).x(Double.parseDouble(command[3])).y(Double.parseDouble(command[4]));
                        String[] dist;

                        try {
                            dist = distributions.get(Integer.parseInt(command[1])).split(",");
                            stopBuilder.arrivalRate((Integer.parseInt(dist[0]) + Integer.parseInt(dist[1])) / 2.0);
                            stopBuilder.disembarkRate((Integer.parseInt(dist[2]) + Integer.parseInt(dist[3])) / 2.0);
                        } catch (NullPointerException e) {
                            stopBuilder.arrivalRate(Double.parseDouble(command[5])).disembarkRate(Double.parseDouble(command[6]));
                        }

                        stops.put(Integer.parseInt(command[1]), stopBuilder.build());
                        break;
                    case "add_route":
                        routes.put(Integer.parseInt(command[1]), new Route(Integer.parseInt(command[1]), new ArrayList<>(), false));
                        break;
                    case "extend_route":
                        r = routes.get(Integer.parseInt(command[1]));
                        s1 = stops.get(Integer.parseInt(command[2]));
                        if (r == null) {
                            System.out.println("Line " + i + ":Invalid route ID");
                            System.exit(1);
                        } else if (s1 == null) {
                            System.out.println("Line " + i + ":Invalid stop ID");
                            System.exit(1);
                        } else
                            r.addStop(s1);
                        break;
                    case "add_bus":
                        r = routes.get(Integer.parseInt(command[2]));
                        if (r == null) {
                            System.out.println("Line " + i + ":Invalid route ID");
                            System.exit(1);
                        } else {
                            Bus.BusBuilder busBuilder = new Bus.BusBuilder(r, simTime);
                            busBuilder.id(Integer.parseInt(command[1])).startStop(Integer.parseInt(command[3])).capacity(Integer.parseInt(command[4])).speed(Double.parseDouble(command[5]));
                            buses.put(Integer.parseInt(command[1]), busBuilder.build());
                        }
                        break;
                    case "add_pass_bus":
                        b = buses.get(Integer.parseInt(command[1]));
                        s1 = stops.get(Integer.parseInt(command[2]));
                        if (b == null) {
                            System.out.println("Line " + i + ":Invalid bus ID");
                            System.exit(1);
                        } else if (s1 == null) {
                            System.out.println("Line " + i + ":Invalid stop ID");
                            System.exit(1);
                        } else
                            b.passengers.add(new Passenger(s1));
                        break;
                    case "add_pass_stop":
                        s1 = stops.get(Integer.parseInt(command[1]));
                        s2 = stops.get(Integer.parseInt(command[2]));
                        if (s1 == null || s2 == null) {
                            System.out.println("Line " + i + ":Invalid stop ID");
                            System.exit(1);
                        } else
                            s1.passengerQueue.add(new Passenger(s2));
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found, please enter valid filename");
        } catch (IOException e) {
            System.err.println("I have no idea why this would be thrown");
        }
    }
}
