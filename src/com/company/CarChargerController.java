package com.company;

import javafx.util.Pair;

import java.time.Duration;
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
        LocalTime randomStartTime = LocalTime.of(8, 0);
        List<CarCharger> carChargerList = new ArrayList<>();

        for (int i = 0; i < length; i++){
            int randomDuraInt = ThreadLocalRandom.current().nextInt(1, 10+1);
            Duration randomDuration = Duration.ofHours((long) randomDuraInt);

            int randomDist = ThreadLocalRandom.current().nextInt(5, 100);
            double randomBatt = ThreadLocalRandom.current().nextDouble(0.1, 1);

            // constant rate for all cars for now
            double chargeRate = 3.3;

            carChargerList.add(new CarCharger(randomStartTime, randomDuration, randomDist, randomBatt, chargeRate));
        }

        return carChargerList;
    }

    //untested
    public static List<Pair<List<CarCharger>, Double>> EvaluateFitness (List<List<CarCharger>> chroms, double[] electricityPrice) {

        List<Pair<List<CarCharger>, Double>> chromWithFitness = new ArrayList<>();

        for (int i = 0; i < chroms.size(); i++) {

            double costsum = 0;

            for (int j = 0; j < chroms.get(i).size(); j++) {
                CarCharger currentCar = chroms.get(i).get(j);
                LocalTime starttime = currentCar.getLocalTime();
                Duration duration = currentCar.getDuration();
                LocalTime endtime = starttime.plus(duration);

                double sumElectricityPrice = 0;

                //assumes price is an hourly array, will have to revise
                for (int x = starttime.getHour(); x < endtime.getHour(); x++) {
                    sumElectricityPrice =+ electricityPrice[x];
                }

                costsum =+ sumElectricityPrice * currentCar.getCharge_rate();
            }
            Pair<List<CarCharger>, Double> solWithFitness = new Pair<>(chroms.get(i), costsum);
            chromWithFitness.set(i, solWithFitness);
        }
        return chromWithFitness;
    }

}
