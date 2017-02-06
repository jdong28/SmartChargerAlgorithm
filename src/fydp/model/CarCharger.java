/**
 * Stores information about a single charger and the attached car.
 */

/**
 * Calculate Priority
 * Assign 1 and 2 and 0s
 */
package fydp.model;

import java.util.concurrent.ThreadLocalRandom;

public class CarCharger{
    //number of half hour slots
    private int chargeSlots;

    //priority of charging
    private double chargePriority;

    //hours to fully charge vehicle
    private double fullChargeTime = 4;

    //Start time, half hour intervals
    public double[] chargeTime = new double[48];

    //Range of travel
    private double travelDistance;

    //Battery level
    private double batteryLevel;

    //Rate of charge in kWH
    private double chargeRate;

    // Time user arrives
    private int startTime;

    // Time user leaves
    private int endTime;

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public double getChargeRate() {
        return chargeRate;
    }

    public double getChargePriority() {
        return chargePriority;
    }

    public CarCharger() {
        travelDistance = ThreadLocalRandom.current().nextInt(5, 100);
        batteryLevel = ThreadLocalRandom.current().nextDouble(0.1, 1);
        chargeSlots = (int) Math.ceil((1- batteryLevel) * fullChargeTime * 2);

        // constant rate for all cars for now
        chargeRate = 3.3;

        //nextInt upper bound exclusive
        //use 24 hour notations
        startTime = ThreadLocalRandom.current().nextInt(6, 11);
        endTime = ThreadLocalRandom.current().nextInt(15,20);

        //assume each car takes 4 hours to charge
        calculateChargeTime();

        // Prioritizes charging from highest to lowest
        chargePriority = (double) chargeSlots/(endTime - startTime);
    }

    /** Calculates how many 30 minutes slots the car requires to fully charge */
    void calculateChargeTime() {
        int counter = 0;

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
