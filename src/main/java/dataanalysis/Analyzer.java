package main.java.dataanalysis;

import main.java.corelogic.Route;
import main.java.corelogic.Stop;

public class Analyzer {

    public Analyzer() {
    }

    public static int addStop(Stop stop) {
        return stop.getPassengerQueue().size();
    }

    public static int addRoute(Route route) {
        int sum = 0;
        for (int i = 0; i < route.stops.size(); i++) {
            sum += route.stops.get(i).getPassengerQueue().size();
        }
        return sum/route.stops.size();
    }

}