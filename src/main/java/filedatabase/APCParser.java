package filedatabase;

import java.io.Reader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import filedatabase.DBClass;

class APCParser {
    DBClass dbclass;
    Reader csvReader;
    HashSet<Integer> uniqueBusIds;
    HashSet<Integer> uniqueStopIds;
    HashSet<Integer> uniqueRouteIds;
    HashMap<Integer, HashMap<Integer, Boolean>> stopAdjacencyMatrix;

    APCParser(Reader reader, DBClass dbclass) {
        this.dbclass = dbclass;
        csvReader = reader;
        uniqueBusIds = new HashSet<>();
        uniqueStopIds = new HashSet<>();
        uniqueRouteIds = new HashSet<>();
    }

    boolean parseRecord(CSVRecord record) {
        String arrival_time = record.get("arrival_time");
        int bus_id = Integer.parseInt(record.get("vehicle_number"));
        String calendar_date = record.get("calendar_day");
        String departure_time = record.get("departure_time");
        String direction = record.get("direction");
        int passenger_ons = Integer.parseInt(record.get("ons"));
        int passenger_offs = Integer.parseInt(record.get("offs"));
        int route_id = Integer.parseInt(record.get("route"));
        int stop_id = Integer.parseInt(record.get("stop_id"));
        // TODO: Add entry to BusLocation database
        if(!uniqueBusIds.contains(bus_id)) {
            if(dbclass.addNewBus(bus_id) == false) {
                System.err.println(String.format(
                    "Could not add new bus with ID %d.", bus_id));
                return false;
            }
            uniqueBusIds.add(bus_id);
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
            String latitude = record.get("latitude");
            String longitude = record.get("longitude");
            String stop_name = record.get("stop_name");
            if(dbclass.addNewStop(stop_id, stop_name, latitude, longitude) == false) {
                System.err.println(String.format(
                    "Could not add new stop with ID %d.", stop_id));
                return false;
            }
            uniqueStopIds.add(stop_id);
        }
        return true;
    }

    void parse() {
        if(dbclass.connect() == false) {
            System.err.println("Could not connect to the database.");
            return;
        }
        if(dbclass.createBusTable() == false) {
            System.err.println("Could not create the Bus table.");
            return;
        }
        if(dbclass.createRouteTable() == false) {
            System.err.println("Could not create the Route table.");
            return;
        }
        if(dbclass.createStopTable() == false) {
            System.err.println("Could not create the Stop table.");
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

        for(CSVRecord record : records) {
            if(parseRecord(record) == false) {
                System.err.println(String.format(
                    "Encountered an error while trying to insert record %s",
                    record.toString()));
                break;
            }
        }
        dbclass.closeConnection();
    }
}
