package dataanalysis;

import corelogic.Route;
import corelogic.Stop;

import java.util.List;

public class Analyzer {

    List<Route> routes = null;
    List<Integer> stopEffectiveness = null;

    public Analyzer(List<Route> routes) {
        this.routes = routes;
        this.stopEffectiveness = null;
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