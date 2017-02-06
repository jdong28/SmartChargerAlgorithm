package fydp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculate price matrix
 * Sort CarChargers
 * Assign CarChargers according to price matrix
 */
public final class CarChargerController {

    /** Generates a list of random car chargers. */
    public static List<CarCharger> generateRandomCarChargers(int length) {
       List<CarCharger> carChargerList = new ArrayList<>();

        for (int i = 0; i < length; i++){
            CarCharger carCharger = new CarCharger();
            carChargerList.add(carCharger);

            System.out.println(String.format("Battery level: %f", carCharger.getBatteryLevel()));
        }

        return carChargerList;
    }

    /** Sorts list of car chargers based on chargePriority */
    public static List<CarCharger> sortCarChargersPriority(List<CarCharger> carChargers) {



        return carChargers;
    }

/**
 * Deprecated
    //generates list of initial solutions
    public static List<List<CarCharger>> generateInitialSolution(int poolSize, int carNumber) {
        List<List<CarCharger>> chromosome = new ArrayList<>();

        for (int i=0; i<poolSize; i++) {
            chromosome.add(generateRandomCarChargers(carNumber));
        }

        return chromosome;
    }


    //Calculate fitness for GA
    public static List<Pair<List<CarCharger>, Double>> evaluateFitness(List<List<CarCharger>> chroms, double[] electricityPrice) {

        List<Pair<List<CarCharger>, Double>> chromWithFitness = new ArrayList<>(chroms.size());

        for (int i = 0; i < chroms.size(); i++) {

            double costsum = 0;

            for (int j = 0; j < chroms.get(i).size(); j++) {
                CarCharger currentCar = chroms.get(i).get(j);
                double sumElectricityPrice = 0;

                //assumes price is array of half hour prices
                for (int x = 0; x < 48; x++) {
                    if (currentCar.chargeTime[x] == 1) {
                        // divide by 2 because half hour
                        sumElectricityPrice = sumElectricityPrice + electricityPrice[x]/2;
                    }
                }

                costsum = costsum + sumElectricityPrice * currentCar.getChargeRate();
            }
            Pair<List<CarCharger>, Double> solWithFitness = new Pair<>(chroms.get(i), costsum);
            chromWithFitness.add(i, solWithFitness);
        }
        return chromWithFitness;
    }
**/

}
