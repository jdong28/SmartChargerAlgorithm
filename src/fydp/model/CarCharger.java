/**
 * Stores information about a single charger and the attached car.
 */

/**
 * Calculate Priority
 * Assign 1 and 2 and 0s
 */
package fydp.model;

import java.util.concurrent.ThreadLocalRandom;

public class CarCharger {
    //number of half hour slots
    private int chargeSlots;

    //priority of charging
    private double chargePriority;

    //hours to fully charge vehicle
    private double fullChargeTime = 4;

    //Start time, half hour intervals
    public double[] chargeTime = new double[48];

    //Range of travel
    private double travel_distance;

    //Battery level
    private double battery_level;

    //Rate of charge in kWH
    private double charge_rate;

    public int getChargeSlots() {
        return chargeSlots;
    }

    public double getChargePriority() {
        return chargePriority;
    }

    public void setChargePriority(double chargePriority) {
        this.chargePriority = chargePriority;
    }

    public double getFullChargeTime() {
        return fullChargeTime;
    }

    public void setFullChargeTime(double fullChargeTime) {
        this.fullChargeTime = fullChargeTime;
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

    public CarCharger(int chargeSlots, double chargePriority, double fullChargeTime, double[] chargeTime,
                      double travel_distance, double battery_level, double charge_rate) {
        this.chargeSlots = chargeSlots;
        this.chargePriority = chargePriority;
        this.fullChargeTime = fullChargeTime;
        this.chargeTime = chargeTime;
        this.travel_distance = travel_distance;
        this.battery_level = battery_level;
        this.charge_rate = charge_rate;
    }

    public CarCharger() {
        travel_distance = ThreadLocalRandom.current().nextInt(5, 100);
        battery_level = ThreadLocalRandom.current().nextDouble(0.1, 1);
        chargeSlots = (int) Math.ceil((1-battery_level) * fullChargeTime * 2);

        // constant rate for all cars for now
        charge_rate = 3.3;

        //assume each car takes 4 hours to charge
        CalculateChargeTime();
    }

    void CalculateChargeTime () {
        int counter = 0;

        //nextInt upper bound exclusive
        //use 24 hour notations
        int startTime = ThreadLocalRandom.current().nextInt(6, 11);
        int endTime = ThreadLocalRandom.current().nextInt(15,20);

        for (int i = 0; i < 48; i ++) {
            if (i < startTime * 2 || i > endTime * 2) {
                chargeTime[i] = 2;
            }
            else if (counter < chargeSlots * 2) {
                chargeTime[i] = 1;
                counter++;
            }
            else {
                chargeTime[i] = 0;
            }
        }


    }





}
