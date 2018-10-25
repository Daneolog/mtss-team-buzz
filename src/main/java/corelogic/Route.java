package corelogic;

import java.util.List;

public class Route {
    public List<Stop> stops;
    private boolean isLinear;

    public Route(List<Stop> stops, boolean isLinear) {
        this.stops = stops;
        this.isLinear = isLinear;
    }

    public boolean isLinear() {
        return isLinear;
    }
}
