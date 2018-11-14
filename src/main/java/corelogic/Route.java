package corelogic;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private int id;
    private List<Stop> stops;
    private boolean isLinear;

    public Route(int id, List<Stop> stops, boolean isLinear) {
        this.id = id;
        this.stops = stops;
        this.isLinear = isLinear;

        //Add each stop along this route as a possible destination from another stop
        for (Stop s : stops) {
            for (Stop ss : stops) {
                if (ss == s) {
                    continue;
                }
                s.addDestinationNoCalc(ss);
            }
            s.recomputeDestinationProbabilities();
        }
    }

    /**
     * Add a stop to the route
     * @param stop Stop to add
     */
    void addStop(Stop stop) {
        //Gotta add this stop as a destination from every other stop on the route, and conversely for the added stop
        for (Stop s : stops) {
            s.addDestinationNoCalc(stop);
            s.recomputeDestinationProbabilities();
            stop.addDestinationNoCalc(s);
        }
        stop.recomputeDestinationProbabilities();
        stops.add(stop);
    }

    /**
     * @return A the stops that belong to the route
     */
    public List<Stop> getStops() {
        return stops;
    }

    public boolean isLinear() {
        return isLinear;
    }
}
