package corelogic;

import java.util.List;

public class Route {
    private int id;
    public List<Stop> stops;
    private boolean isLinear;

    public Route(int id, List<Stop> stops, boolean isLinear) {
        this.id = id;
        this.stops = stops;
        this.isLinear = isLinear;
    }

    public boolean isLinear() {
        return isLinear;
    }
}
