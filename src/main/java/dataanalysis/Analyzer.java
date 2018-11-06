package dataanalysis;

import corelogic.Route;
import corelogic.Stop;

public class Analyzer {

    public Analyzer() {
    }

    public int addStop(Stop stop) {
        return stop.passengerQueue.size();
    }

    public int addRoute(Route route) {
        int sum = 0;
        for (int i = 0; i < route.stops.size(); i++) {
            sum += route.stops.get(i).passengerQueue.size();
        }
        return sum/route.stops.size();
    }

}