package main.java.corelogic;

public class Passenger {
    private Stop destination;

    public Passenger(Stop destination) {
        this.destination = destination;
    }

    public Stop getDestination() {
        return destination;
    }
}
