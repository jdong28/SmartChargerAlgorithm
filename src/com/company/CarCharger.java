/**
 * Stores information about a single charger and the attached car.
 */
package com.company;

import java.time.LocalTime;
import java.time.Duration;

public class CarCharger {

    //Start time
    private LocalTime localTime;

    //Duration of stay
    private Duration duration;

    //Range of travel
    private int travel_distance;

    //Battery level
    private double battery_level;

    //Rate of charge in kWH
    private double charge_rate;

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public int getTravel_distance() {
        return travel_distance;
    }

    public void setTravel_distance(int travel_distance) {
        this.travel_distance = travel_distance;
    }

    public double getBattery_level() {
        return battery_level;
    }

    public void setBattery_level(double battery_level) {
        this.battery_level = battery_level;
    }

    public double getCharge_rate() {
        return charge_rate;
    }

    public void setCharge_rate(double charge_rate) {
        this.charge_rate = charge_rate;
    }

    public CarCharger(LocalTime time, Duration dura, int dist, double batt, double crate){
        localTime = time;
        duration = dura;
        travel_distance = dist;
        battery_level = batt;
        charge_rate = crate;
    }

}
