/**
 * Stores information about a single charger and the attached car.
 */
package fydp.model;

import java.util.concurrent.ThreadLocalRandom;

public class CarCharger {

    //Start time, half hour intervals
    private double[] chargeTime = new double[48];

    //Range of travel
    private double travel_distance;

    //Battery level
    private double battery_level;

    //Rate of charge in kWH
    private double charge_rate;

    public double[] getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(double[] chargeTime) {
        this.chargeTime = chargeTime;
    }

    public double getTravel_distance() {
        return travel_distance;
    }

    public void setTravel_distance(double travel_distance) {
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

    public CarCharger(double[] time, double[] dura, int dist, double batt, double crate){
        chargeTime = time;
        travel_distance = dist;
        battery_level = batt;
        charge_rate = crate;
    }

    public CarCharger() {

        //assume charge from 8AM to 12PM
        for (int i = 16; i < 24; i++) {
            chargeTime[i] = 1;
        }
        travel_distance = ThreadLocalRandom.current().nextInt(5, 100);
        battery_level = ThreadLocalRandom.current().nextDouble(0.1, 1);

        // constant rate for all cars for now
        charge_rate = 3.3;

    }

}
