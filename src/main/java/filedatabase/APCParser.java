package filedatabase;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

class APCParser {
    DBClass dbclass;
    Reader csvReader;
    HashSet<Integer> uniqueBusIds;
    HashSet<Integer> uniqueStopIds;
    HashSet<Integer> uniqueRouteIds;
//    HashMap<Integer, HashMap<Integer, Boolean>> stopAdjacencyMatrix;
    HashMap<Integer, ArrayList<Integer>> routeOrder; //routeID -> orderID -> list of stopId
    ArrayList<Integer> stops;
    Integer startBusId;
    int prev_route_id;

    APCParser(Reader reader, DBClass dbclass) {
        this.dbclass = dbclass;
        csvReader = reader;
        uniqueBusIds = new HashSet<>();
        uniqueStopIds = new HashSet<>();
        uniqueRouteIds = new HashSet<>();
        routeOrder = new HashMap<>();
        stops = new ArrayList<>();
        startBusId = -1;
        prev_route_id = -1;
    }

    boolean parseRecord(CSVRecord record) {
        String arrival_time = record.get("arrival_time");
        int curr_bus_id = Integer.parseInt(record.get("vehicle_number"));
        String calendar_date = record.get("calendar_day");
        String departure_time = record.get("departure_time");
//        String direction = record.get("direction");
        int passenger_ons = 0;
        if(!record.get("ons").equals("")) {
            passenger_ons = Integer.parseInt(record.get("ons"));
        }
        int passenger_offs = 0;
        if(!record.get("offs").equals("")) {
            passenger_offs = Integer.parseInt(record.get("offs"));
        }
        int route_id = Integer.parseInt(record.get("route"));
        if(prev_route_id == -1) {
            prev_route_id = route_id;
        }
        int stop_id = Integer.parseInt(record.get("stop_id"));
        String datetime = null;

        if(!uniqueBusIds.contains(curr_bus_id)) {
            if(dbclass.addNewBus(curr_bus_id) == false) {
                System.err.println(String.format(
                    "Could not add new bus with ID %d.", curr_bus_id));
                return false;
            }
            uniqueBusIds.add(curr_bus_id);
        }
        if (startBusId == -1) {
            startBusId = curr_bus_id;
        }

        if(!uniqueRouteIds.contains(route_id)) {
            String route_name = record.get("route_name");
            if(dbclass.addNewRoute(route_id, route_name) == false) {
                System.err.println(String.format(
                    "Could not add new route with ID %d.", route_id));
                return false;
            }
            uniqueRouteIds.add(route_id);
        }
        if(!uniqueStopIds.contains(stop_id)) {
            String latitude = "0.0";
            if(!record.get("latitude").equals("")) {
                latitude = record.get("latitude");
            }
            String longitude = "0.0";
            if(!record.get("longitude").equals("")) {
                longitude = record.get("longitude");
            }
            String stop_name = record.get("stop_name");
            if(dbclass.addNewStop(stop_id, stop_name, latitude, longitude) == false) {
                System.err.println(String.format(
                    "Could not add new stop with ID %d.", stop_id));
                return false;
            }
            uniqueStopIds.add(stop_id);
        }

        if(!arrival_time.equals("")) {
            datetime = String.format("%s %s", calendar_date, arrival_time);
        } else if(!departure_time.equals("")) {
            datetime = String.format("%s %s", calendar_date, departure_time);
        }
        if(dbclass.addNewBusLocation(datetime, curr_bus_id, stop_id, route_id,
               passenger_ons, passenger_offs) == false) {
            System.err.println(String.format(
                "Could not add new bus with ID %d.", curr_bus_id));
            return false;
        }

        if (startBusId != curr_bus_id) {
            for (int i = 0; i < stops.size(); i++) {
                for (int j = i + 1; j < stops.size(); j++) {
                    if (stops.get(i).equals(stops.get(j))) {
                        if(!(routeOrder.containsKey(prev_route_id))) {
                            ArrayList<Integer> temp = new ArrayList<Integer>(stops.subList(i, j));
                            routeOrder.put(prev_route_id, temp);
                        }
                    }
                }
            }
            stops.clear();
            startBusId = curr_bus_id;
            prev_route_id = route_id;
        }
        if(stop_id != 99999) {
            stops.add(stop_id);
        }

        return true;
    }

    void parse() {
        if (dbclass.connect() == false) {
            System.err.println("Could not connect to the database.");
            return;
        }
        if (dbclass.createBusTable() == false) {
            System.err.println("Could not create the Bus table.");
            return;
        }
        if (dbclass.createRouteTable() == false) {
            System.err.println("Could not create the Route table.");
            return;
        }
        if (dbclass.createStopTable() == false) {
            System.err.println("Could not create the Stop table.");
            return;
        }
        if (dbclass.createBusLocationTable() == false) {
            System.err.println("Could not create the BusLocation table.");
            return;
        }
        if (dbclass.createRouteOrderTable() == false) {
            System.err.println("Could not create the RouteOrder table.");
            return;
        }
        // TODO:
        // 1) Read a CSV row
        // 2) Parse out the date_time, route_id, stop_id, direction, ons/offs, vehicle number
        // 3) Insert this row into the database in a log table
        //    - need to increase DB memory limit?
        // 4) Use SQL to sort the data by date, use stopAdjacencyMatrix to generate routes
        // 5) Save the generated routes in the DB
        Iterable<CSVRecord> records;

        try {
            CSVFormat csvFormat = CSVFormat.newFormat(',')
                    .withRecordSeparator('\n')
                    .withFirstRecordAsHeader();
            records = csvFormat.parse(csvReader);
        } catch (IOException e) {
            System.err.println(e.toString());
            return;
        }

        for (CSVRecord record : records) {
//            String currRoute = record.get("route");
            //////            int currStop = Integer.parseInt(record.get("stop_id"));
            //////            int currBus = Integer.parseInt(record.get("stop_id"));
            ////
            ////            if (!routeOrder.containsKey(currRoute)) {
            ////                routeOrder.put(currRoute, new ArrayList<>());
            ////            }
            ////            CSVRecord tempRecord = record;
            ////            while (tempRecord.get("route").equals(currRoute)) {
            ////                list.add(tempRecord.stop)
            ////                tempRecord = tempRecord.
            ////            }

            if (parseRecord(record) == false) {
                System.err.println(String.format(
                        "Encountered an error while trying to insert record %s",
                        record.toString()));
                break;
            }
        }
        for (Integer routeId : routeOrder.keySet()) {
            int counter = 1;
            for (Integer stopId : routeOrder.get(routeId)) {
                dbclass.addNewRouteOrder(routeId, stopId, counter);
                counter++;
            }
        }
        dbclass.closeConnection();
    }
}
