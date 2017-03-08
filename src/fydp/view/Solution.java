package fydp.view;

import fydp.model.CarCharger;
import fydp.model.CarChargerController;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by xiuxu on 2017-02-19.
 */
public class Solution {
    public static ObservableList<CarCharger> initialSolution;
    public static double[] electricityPrice;
    public static int[] chargeCapacity = new int[48];
    private static int capacityFactor = 0;

    public static void recalculateSolution() {
        initialSolution = CarChargerController.CarChargerSlotAssign(initialSolution,electricityPrice, chargeCapacity);
    }

    public static void generateInitialSolution(int num){

        capacityFactor = num;

        // Generate electricity price before generating solution
        electricityPrice = new double[48];
        generateElectricityPriceAndCapacity();

        // loop to be removed
        for(int i=0; i<48; i++) {
            chargeCapacity[i]=6;
        }
        /*
        for (int i = 0; i < 48; i ++) {
            electricityPrice[i] = ThreadLocalRandom.current().nextDouble(0, 0.5);
        }*/

        initialSolution = CarChargerController.generateRandomCarChargers(num);

        // sorts cars based on charging priority
        Collections.sort(initialSolution, (o1, o2) ->
                doubleToIntFloor(o2.getChargePriority() - o1.getChargePriority()));

        // assigns car charging schedule
        initialSolution = CarChargerController.CarChargerSlotAssign(initialSolution,electricityPrice, chargeCapacity);
    }

    private static int doubleToIntFloor(double num) {
        if (num < 0) {
            return (int) num - 1;
        }
        else if (num == 0) {
            return 0;
        }
        else {
            return (int) num + 1;
        }
    }

    // More realistic electricity price and hourly capacity
    private static void generateElectricityPriceAndCapacity() {
        //2017 YTD
        //double[] electricityPrice24 = {13.92, 13.42, 12.11, 10.98, 9.44, 7.12 , 13.87, 23.58, 26.54, 28.28, 26.11
        //, 25.8, 19.26, 20.92, 26.08, 21.65, 19.90, 31.57, 29.13, 27.26, 26.72, 32.19, 23.58, 14.46};

        //2015 Year Average
        double[] electricityPrice24 = {13.56, 11.08, 10.11, 8.87, 10.23, 12.78, 18.84, 30.18, 31.88, 26.40, 27.07,
        25.44, 23.35, 20.65, 20.96, 28.23, 26.87, 28.30, 30.58, 31.68, 25.55, 23.11, 19.34, 14.88};

        for(int i=0; i<48; i++) {
            if (i > 14 && i < 20) {
                chargeCapacity[i]= capacityFactor - capacityFactor/2 * i/20;
            }
            else if (i >= 20 && i <= 34) {
                chargeCapacity[i] = capacityFactor/2;
            }
            else if (i > 34 && i < 40) {
                chargeCapacity[i] = capacityFactor/2 + capacityFactor/2 * i/40;
            }
            else {
                chargeCapacity[i] = capacityFactor;
            }
            electricityPrice[i] = electricityPrice24[i/2] / 1000;
        }
    }

    public static double calculateCostBefore() {
        double cost = 0;
        for (Iterator<CarCharger> iterator = initialSolution.iterator(); iterator.hasNext(); ) {
            CarCharger next = iterator.next();
            cost = cost + next.getUnoptimizedChargeCost();
        }
        return cost;
    }

    public static double calculateCostAfter() {
        double cost = 0;
        for (CarCharger carCharger : initialSolution) {
            for (int i = 0; i < 48; i++) {
                if (carCharger.chargeTime[i] == 3) {
                    // Divide by 2 since price is per hour
                    cost = cost + electricityPrice[i]/2;
                }
            }
        }

        return cost;
    }
}
