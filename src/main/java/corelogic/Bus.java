package corelogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Objects that collect passengers and move along routes
 */
public class Bus {

    double speed;
    private Route route;
    private List<Passenger> passengers;
    private int currentStop;
    private int nextStop;

    private int arrivalTime;

    public Bus(Route route, int startStop, double speed, int simTime) {
        this.route = route;
        passengers = new ArrayList<>();
        currentStop = startStop;
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
        arrivalTime = (int)(dist / speed) + currTime;
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
}
