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
        currentStop = startStop;
        passengers = new ArrayList<>();
        for (int i=0; i<initialPassengers; i++) {
            passengers.add(new Passenger(null));
        }
        this.capacity = capacity;
        this.speed = speed;
        CalculateNextStop();
        CalculateArrival(simTime);
    }


    boolean Tick(int simTime) {
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
        return route.stops.get(currentStop);
    }


    private void CalculateNextStop() {
        if (route.isLinear()) {
            if (currentStop + 1 == route.stops.size()) {
                nextStop = currentStop - 1;
            } else {
                nextStop = currentStop + 1;
            }
        } else {
            nextStop = (currentStop + 1) % route.stops.size();
        }
    }

    private void CalculateArrival(int currTime) {
        Stop current = route.stops.get(currentStop);
        Stop next = route.stops.get(nextStop);
        double dist = Math.sqrt(Math.pow(next.getX() - current.getX(), 2) + Math.pow(next.getY() - current.getY(), 2));
        arrivalTime = Math.max((int)(dist / speed), 1) + currTime;
    }

    private void ExchangePassengers() {
        Stop current = route.stops.get(currentStop);
        //Unload
        for (int i = passengers.size() - 1; i >= 0; --i) {
            if (passengers.get(i).getDestination() == current) {
                passengers.remove(i);
            }
        }
        //Board
        for (int i = current.passengerQueue.size() - 1; i >=0; --i) {
            if (route.stops.contains(passengers.get(i).getDestination())) {
                passengers.add(current.passengerQueue.remove(i));
            }
        }
    }

    public double getSpeed() {
        return speed;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}