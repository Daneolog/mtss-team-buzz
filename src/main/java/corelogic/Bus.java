package corelogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Objects that collect passengers and move along routes
 */
public class Bus {

    private int id;
    private double speed;
    private int capacity;
    private Route route;
    private List<Passenger> passengers;
    private int currentStop;
    private int nextStop;

    private int arrivalTime;

    public Bus(int id, Route route, int startStop, int initialPassengers, int capacity, double speed, int simTime) {
        this.id = id;
        this.route = route;
        this.capacity = capacity;
        this.speed = speed;
        currentStop = startStop;
        simInit(initialPassengers, simTime);
    }

    private Bus(Route route) {
        this.route = route;
    }

    private void simInit(int initialPassengers, int simTime) {
        passengers = new ArrayList<>();
        for (int i = 0; i < initialPassengers; ++i) {
            passengers.add(new Passenger(null));
        }
        CalculateNextStop();
        CalculateArrival(simTime);
    }

    boolean tick(int simTime) {
        if (simTime == arrivalTime) {
            currentStop = nextStop;
            CalculateNextStop();
            CalculateArrival(simTime);
            ExchangePassengers();
            return true;
        }
        return false;
    }

    public Stop getCurrentStop() {
        return route.getStops().get(currentStop);
    }

    public int getId() {
        return id;
    }

    public double getSpeed() {
        return speed;
    }

    public int getCapacity() {
        return capacity;
    }

    public Route getRoute() {
        return route;
    }

    public Stop getNextStop() { return route.getStops().get(nextStop); }

    public int getNumPassengers() {
        return passengers.size();
    }

    private void CalculateNextStop() {
        if (route.isLinear()) {
            if (currentStop + 1 == route.getStops().size()) {
                nextStop = currentStop - 1;
            } else {
                nextStop = currentStop + 1;
            }
        } else {
            nextStop = (currentStop + 1) % route.getStops().size();
        }
    }

    private void CalculateArrival(int currTime) {
        Stop current = route.getStops().get(currentStop);
        Stop next = route.getStops().get(nextStop);
        double dist = Math.sqrt(Math.pow(next.getX() - current.getX(), 2) + Math.pow(next.getY() - current.getY(), 2));
        arrivalTime = Math.max((int)(dist / speed), 1) + currTime;
    }

    private void ExchangePassengers() {
        Stop current = route.getStops().get(currentStop);
        //Unload
        for (int i = passengers.size() - 1; i >= 0; --i) {
            if (passengers.get(i).getDestination() == current) {
                System.out.println("Bus " + id + " dropped off a passenger");
                passengers.remove(i);
            }
        }
        //Board
        for (int i = current.passengerQueue.size() - 1; i >=0 && passengers.size() < capacity; --i) {
            if (route.getStops().contains(current.passengerQueue.get(i).getDestination())) {
                System.out.println("Bus " + id + " picked up a passenger");
                passengers.add(current.passengerQueue.remove(i));
            }
        }
    }

    public static class BusBuilder {
        private Bus bus;
        private int simTime;
        private int initialPassengers;

        BusBuilder(Route route, int simTime) {
            bus = new Bus(route);
            this.simTime = simTime;
        }

        BusBuilder id(int id) {
            bus.id = id;
            return this;
        }

        BusBuilder speed(double speed) {
            bus.speed = speed;
            return this;
        }

        BusBuilder capacity(int capacity) {
            bus.capacity = capacity;
            return this;
        }

        BusBuilder startStop(int startStop) {
            bus.currentStop = startStop;
            return this;
        }

        BusBuilder initPassengers(int initialPassengers) {
            this.initialPassengers = initialPassengers;
            return this;
        }

        Bus build() {
            bus.simInit(initialPassengers, simTime);
            return bus;
        }
    }
}
