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
        for (int i = 0; i < route.getStops().size(); i++) {
            sum += route.getStops().get(i).getDisembarkRate();
        }
        return sum/route.getStops().size();
    }

}