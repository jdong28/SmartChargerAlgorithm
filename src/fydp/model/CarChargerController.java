package fydp.model;

import javafx.util.Pair;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by xiuxu on 2017-01-16.
 */
public class CarChargerController {

    //generates list of initial solutions
    public static List<List<CarCharger>> GenerateInitialSolution (int poolSize, int carNumber) {
        List<List<CarCharger>> chromosome = new ArrayList<>();

        for (int i=0; i<poolSize; i++) {
            chromosome.add(GenerateRandomCarChargers(carNumber));
        }

        return chromosome;
    }

    //generates list of random car chargers, representing one solution
    private static List<CarCharger> GenerateRandomCarChargers (int length) {
       List<CarCharger> carChargerList = new ArrayList<>();

        for (int i = 0; i < length; i++){
            carChargerList.add(new CarCharger());
        }

        return carChargerList;
    }

    //untested
    public static List<Pair<List<CarCharger>, Double>> EvaluateFitness (List<List<CarCharger>> chroms, double[] electricityPrice) {

        List<Pair<List<CarCharger>, Double>> chromWithFitness = new ArrayList<>(chroms.size());

        for (int i = 0; i < chroms.size(); i++) {

            double costsum = 0;

            for (int j = 0; j < chroms.get(i).size(); j++) {
                CarCharger currentCar = chroms.get(i).get(j);
                double[] chargeTime = currentCar.getChargeTime();

                double sumElectricityPrice = 0;

                //assumes price is array of half hour prices
                for (int x = 0; x < 48; x++) {
                    if (chargeTime[x] == 1) {
                        // divide by 2 because half hour
                        sumElectricityPrice = sumElectricityPrice + electricityPrice[x]/2;
                    }
                }

                costsum = costsum + sumElectricityPrice * currentCar.getCharge_rate();
            }
            Pair<List<CarCharger>, Double> solWithFitness = new Pair<>(chroms.get(i), costsum);
            chromWithFitness.add(i, solWithFitness);
        }
        return chromWithFitness;
    }

}
