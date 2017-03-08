/**
 * Stores information about a single charger and the attached car.
 */

/**
 * Calculate Priority
 * Assign 1 and 2 and 0s
 */
package fydp.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.concurrent.ThreadLocalRandom;

import static fydp.view.Solution.electricityPrice;

public class CarCharger{
    //ID of charger
    private int carID;

    //number of half hour slots
    private int chargeSlots;

    //priority of charging
    private double chargePriority;

    //hours to fully charge vehicle
    private double fullChargeTime = 4;

    //Start time, half hour intervals
    public double[] chargeTime = new double[48];

    //chargeTime that is not optimized
    public double[] unoptimizedChargeTime = new double[48];

    private double unoptimizedChargeCost = 0;

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

    private IntegerProperty carIDProperty;

    private DoubleProperty carBatteryLevelProperty;

    public int getCarIDProperty() {
        carIDProperty.set(carID);
        return carIDProperty.get();
    }

    public IntegerProperty carIDPropertyProperty() {
        return carIDProperty;
    }

    public double getCarBatteryLevelProperty() {
        carBatteryLevelProperty.set(batteryLevel);
        return carBatteryLevelProperty.get();
    }

    public DoubleProperty carBatteryLevelPropertyProperty() {
        return carBatteryLevelProperty;
    }

    public int getCarID() {
        return carID;
    }

    public double getUnoptimizedChargeCost() {
        return unoptimizedChargeCost;
    }

    public int getStartTime() {
        return startTime;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public double getChargeRate() {
        return chargeRate;
    }

    public double getChargePriority() {
        return chargePriority;
    }

    public int getChargeSlots() {return chargeSlots;}

    public CarCharger(int id) {
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

        unoptimizedChargeCost = calculateCost();

        carID = id;

        carIDProperty = new SimpleIntegerProperty(carID);
        carBatteryLevelProperty = new SimpleDoubleProperty(batteryLevel);

    }

    /** Calculates how many 30 minutes slots the car requires to fully charge */
    private void calculateChargeTime() {
        int counter = 0;

        for (int i = 0; i < 48; i ++) {
            if (i < startTime * 2 || i > endTime * 2) {
                chargeTime[i] = 2;
                unoptimizedChargeTime[i] = 2;
            }
            else if (counter < chargeSlots * 2) {
                chargeTime[i] = 1;
                unoptimizedChargeTime[i] = 1;
                counter++;
            }
            else {
                chargeTime[i] = 0;
                unoptimizedChargeTime[i] = 0;
            }
        }
    }

    // Calculates unoptimized cost
    private double calculateCost() {
        double cost = 0;
        for (int i = 0; i < 48; i++) {
            if (chargeTime[i] == 1)

            //Divide by 2 since cost is given per hour
            cost = cost + chargeTime[i]*electricityPrice[i]/2;
        }

        return cost;
    }

}
