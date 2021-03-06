/**
 * Stores information about a single charger and the attached car.
 */

/**
 * Calculate Priority
 * Assign 1 and 2 and 0s
 */
package fydp.model;

import javafx.beans.property.*;

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

    public double[] batteryProgress = new double[48];

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

    private DoubleProperty startTimeProperty;

    private DoubleProperty endTimeProperty;

    private StringProperty chargeTimes = new SimpleStringProperty();

    public String getChargeTimes() {
        return chargeTimesProperty().get();
    }

    public StringProperty chargeTimesProperty() {
        if (chargeTimes == null) {
            String times = "t";
            for (int i = 0; i < chargeTime.length; i++) {
                //if (chargeTime[i] == 3) {
                    times = times + ", " + Double.toString((double)i/2.0);
                //}
            }
            chargeTimes = new SimpleStringProperty(times);
        }

        return chargeTimes;
    }

    public void setChargeTimes(String value) {
        chargeTimesProperty().setValue(value);
    }

    public double getStartTimeProperty() {
        startTimeProperty.set((double)startTime/2.0);
        return startTimeProperty.get();
    }

    public DoubleProperty startTimePropertyProperty() { return startTimeProperty; }

    public DoubleProperty endTimePropertyProperty() { return endTimeProperty; }

    public double getEndTimeProperty() {
        endTimeProperty.set((double)endTime/2.0);
        return endTimeProperty.get();
    }

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

    public int getEndTime() {
        return endTime;
    }

    public double getFullChargeTime() { return fullChargeTime; }

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

    public CarCharger () {
    }

    public void setFields() {
        //fullChargeTime = ThreadLocalRandom.current().nextDouble(3,5);

        chargeSlots = (int) Math.ceil((1- batteryLevel) * fullChargeTime * 2);

        //assume each car takes 4 hours to charge
        calculateChargeTime();

        // Prioritizes charging from highest to lowest
        chargePriority = (double) chargeSlots/(endTime - startTime);

        unoptimizedChargeCost = calculateCost();

        carIDProperty = new SimpleIntegerProperty(carID);
        carBatteryLevelProperty = new SimpleDoubleProperty(batteryLevel);
        startTimeProperty = new SimpleDoubleProperty((double)startTime/2.0);
        endTimeProperty = new SimpleDoubleProperty((double)endTime/2.0);
    }

    public CarCharger(int carID, double batteryLevel, int startTime, int endTime, double chargeRate) {
        this.carID = carID;
        this.batteryLevel = batteryLevel;
        this.startTime = startTime;
        this.endTime = endTime;
        this.chargeRate = chargeRate;

        setFields();
    }

    public CarCharger(int id) {
        travelDistance = ThreadLocalRandom.current().nextInt(5, 100);
        batteryLevel = ThreadLocalRandom.current().nextDouble(0.3, 0.8);
        //chargeSlots = (int) Math.ceil((1- batteryLevel) * fullChargeTime * 2);

        // constant rate for all cars for now
        chargeRate = ThreadLocalRandom.current().nextDouble(10, 20);
        //chargeRate = 120;
        carID = id;

        //nextInt upper bound exclusive
        //use 48 half-hour notations
        startTime = ThreadLocalRandom.current().nextInt(14, 21);
        endTime = ThreadLocalRandom.current().nextInt(30, 41);

        setFields();

    }

    /** Calculates how many 30 minutes slots the car requires to fully charge */
    private void calculateChargeTime() {
        int counter = 0;

        for (int i = 0; i < 48; i ++) {
            if (i < startTime || i > endTime) {
                chargeTime[i] = 2;
                unoptimizedChargeTime[i] = 2;
            }
            else if (counter < chargeSlots) {
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
