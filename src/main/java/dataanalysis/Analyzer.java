package dataanalysis;

import corelogic.Route;
import corelogic.Stop;

public class Analyzer {

    public Analyzer() {
    }

    public static double addStop(Stop stop) {
        return stop.getDisembarkRate();
    }

    public static double addRoute(Route route) {
        int sum = 0;
        for (int i = 0; i < route.stops.size(); i++) {
            sum += route.stops.get(i).getDisembarkRate();
        }
        return sum/route.stops.size();
    }

}