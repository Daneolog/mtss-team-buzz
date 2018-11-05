package corelogic;

import java.util.LinkedList;
import java.util.Queue;

public class Stop {
    private double x;
    private double y;
    private String name;
    public LinkedList<Passenger> passengerQueue;

    public Stop(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
        passengerQueue = new LinkedList<>();
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

    boolean Tick(double avgArrivalRate) {
        //Poisson random variable generation, found on wikipedia
        double l = Math.exp(-avgArrivalRate);
        int k = 0;
        double p = 1;
        do {
            k += 1;
            p *= Math.random();
        } while(p > l);


        for (int i = 0; i < k - 1; ++i) {
            //TODO: Assign destination stop randomly
            passengerQueue.add(new Passenger(null));
        }
        return k - 1 > 0;
    }
}
