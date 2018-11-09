package corelogic;

import java.util.ArrayList;
import java.util.Collection;
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
    private double totalDisembark;

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
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public double getDisembarkRate() { return disembarkRate; }

    void UpdateDesinations(List<Stop> destinations) {
        this.destinations = destinations;
        totalDisembark = 0;
        for (Stop s : destinations) {
            totalDisembark += s.getDisembarkRate();
        }
        List<Double> probabilities = new ArrayList<>();
        for (Stop s : destinations) {
            probabilities.add(s.disembarkRate / totalDisembark);
        }

        destGenerator = new AliasMethod(probabilities);
    }

    /**
     * Simulates one tick of time at the stop. Potentially spawns new passengers.
     * @return true if a passenger was spawned this tick, else false
     */
    boolean tick() {
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
            int destIndex = destGenerator.next();
            passengerQueue.add(new Passenger(destinations.get(destIndex)));
        }
        return k - 1 > 0;
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

        StopBuilder disembarkRate(int disembarkRate) {
            stop.disembarkRate = disembarkRate;
            return this;
        }

        StopBuilder arrivalRate(int arrivalRate) {
            stop.arrivalRate = arrivalRate;
            return this;
        }

        Stop build() {
            return stop;
        }
    }
}