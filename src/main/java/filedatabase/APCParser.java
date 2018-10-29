package filedatabase;

import java.io.Reader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

class APCParser {
    Reader csvReader;
    HashSet<Integer> uniqueBusIds;
    HashSet<Integer> uniqueStopIds;
    HashSet<Integer> uniqueRouteIds;
    HashMap<Integer, HashMap<Integer, Boolean>> stopAdjacencyMatrix;

    APCParser(Reader reader, String ip, int port, String db, String user,
              String password) {
        csvReader = reader;
        uniqueBusIds = new HashSet<>();
        uniqueStopIds = new HashSet<>();
        uniqueRouteIds = new HashSet<>();
    }

    void parseRecord(CSVRecord record) {
        String arrival_time = record.get("arrival_time");
        int bus_id = Integer.parseInt(record.get("vehicle_number"));
        String calendar_date = record.get("calendar_day");
        String departure_time = record.get("departure_time");
        String direction = record.get("direction");
        int passenger_ons = Integer.parseInt(record.get("passenger_ons"));
        int passenger_offs = Integer.parseInt(record.get("passenger_offs"));
        int route_id = Integer.parseInt(record.get("route"));
        int stop_id = Integer.parseInt(record.get("stop_id"));
        if(!uniqueBusIds.contains(bus_id)) {
            // Add this bus to the database
            uniqueBusIds.add(bus_id);
        }
        if(!uniqueRouteIds.contains(route_id)) {
            String route_name = record.get("route_name");
            // Add this route to the database
            uniqueRouteIds.add(route_id);
        }
        if(!uniqueRouteIds.contains(stop_id)) {
            String latitude = record.get("latitude");
            String longitude = record.get("longitude");
            String stop_name = record.get("stop_name");
            // Add this stop to the database
            uniqueStopIds.add(stop_id);
        }
    }

    void parse() throws IOException {
        // TODO:
        // 1) Read a CSV row
        // 2) Parse out the date_time, route_id, stop_id, direction, ons/offs, vehicle number
        // 3) Insert this row into the database in a log table
        //    - need to increase DB memory limit?
        // 4) Use SQL to sort the data by date, use stopAdjacencyMatrix to generate routes
        // 5) Save the generated routes in the DB
        CSVFormat csvFormat = CSVFormat.newFormat(',')
            .withRecordSeparator('\n')
            .withFirstRecordAsHeader();
        Iterable<CSVRecord> records = csvFormat.parse(csvReader);

        for(CSVRecord record : records) {
            parseRecord(record);
        }
    }
}
