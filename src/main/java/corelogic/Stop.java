package corelogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Stop {
    private int id;
    private double x;
    private double y;
    private String name;

    // Average number of passengers that get off at this stop per tick
    private double disembarkRate;

    // Average number of passengers that queue at this stop per tick
    private double arrivalRate;

    // Collection of all stops reachable from this stop (excluding this stop)
    private List<Stop> destinations;

    // Sum of all disembark rates of all routes that pass through this stop
    private double totalDisembarkRate;

    private AliasMethod destGenerator;
    public LinkedList<Passenger> passengerQueue;

    public Stop(int id, String name, double x, double y, double disembarkRate) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.disembarkRate = disembarkRate;
        passengerQueue = new LinkedList<>();
        destinations = new ArrayList<>();
    }

    private Stop() {
        this.name = "";
        passengerQueue = new LinkedList<>();
        destinations = new ArrayList<>();
        disembarkRate = 1;
    }

    public double getX() {
        return x;
    }

    public int getId() { return this.id; }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public double getArrivalRate() { return arrivalRate; }

    public double getTotalDisembarkRate() { return totalDisembarkRate; }

    public double getDisembarkRate() { return disembarkRate; }

    public List<Stop> getDestinations() {
        return destinations;
    }

    /**
     * Updates the reachable stops from this stop
     * @param destinations New reachable stops
     */
    void updateDesinations(List<Stop> destinations) {
        this.destinations = destinations;
        totalDisembarkRate = 0;
        for (Stop s : destinations) {
            totalDisembarkRate += s.getDisembarkRate();
        }
        List<Double> probabilities = new ArrayList<>();
        for (Stop s : destinations) {
            probabilities.add(s.disembarkRate / totalDisembarkRate);
        }

        destGenerator = new AliasMethod(probabilities);
    }

    /**
     * Adds a new stop to the reachable stops from this stop if it doesn't already exist
     * Does not recompute probabilities for each destination
     * @param destination Reachable stop
     */
    void addDestinationNoCalc(Stop destination) {
        if (!destinations.contains(destination)) {
            destinations.add(destination);
            totalDisembarkRate += destination.getDisembarkRate();
        }
    }

    /**
     * Recomputes the destination probabilities
     * To be called after adding all the destinations to a stop
     */
    void recomputeDestinationProbabilities() {
        List<Double> probabilities = new ArrayList<>();
        for (Stop s : destinations) {
            probabilities.add(s.disembarkRate / totalDisembarkRate);
        }
        if (probabilities.size() != 0)
            destGenerator = new AliasMethod(probabilities);
    }

    /**
     * Simulates one tick of time at the stop. Potentially spawns new passengers.
     * @return number of passengers spawned this tick
     */
    int tick() {
        //Poisson random variable generation, found on Wikipedia
        //Generate number of passengers to spawn
        double l = Math.exp(-arrivalRate);
        int k = 0;
        double p = 1;
        do {
            k += 1;
            p *= Math.random();
        } while(p > l);

        //Generate each passenger's destination stop
        for (int i = 0; i < k - 1; ++i) {

            if (destGenerator != null) {
                int destIndex = destGenerator.next();
                passengerQueue.add(new Passenger(destinations.get(destIndex)));
            } else {
                return 0;
            }
        }
        return k - 1;
    }

    public static class StopBuilder {

        private Stop stop;

        StopBuilder() {
            stop = new Stop();
        }

        StopBuilder id(int id) {
            stop.id = id;
            return this;
        }

        StopBuilder x(double x) {
            stop.x = x;
            return this;
        }

        StopBuilder y(double y) {
            stop.y = y;
            return this;
        }

        StopBuilder name(String name) {
            stop.name = name;
            return this;
        }

        StopBuilder disembarkRate(double disembarkRate) {
            stop.disembarkRate = disembarkRate;
            return this;
        }

        StopBuilder arrivalRate(double arrivalRate) {
            stop.arrivalRate = arrivalRate;
            return this;
        }

        StopBuilder destinations(List<Stop> destinations) {
            stop.destinations = destinations;
            return this;
        }

        Stop build() {
            return stop;
        }
    }
}