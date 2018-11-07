package corelogic;

import java.util.LinkedList;
import java.util.Queue;

public class Stop {
    private int id;
    private double x;
    private double y;
    private String name;
    LinkedList<Passenger> passengerQueue;

    public Stop(int id, String name, double x, double y) {
        this.id = id;
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

    boolean Tick() {
        //TODO: Spawn passengers in a Poisson random distribution based on an average arrival rate
        return false;
    }
}
