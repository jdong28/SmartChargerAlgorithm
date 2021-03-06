package fydp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Arrays;

/**
 * Calculate price matrix
 * Sort CarChargers
 * Assign CarChargers according to price matrix
 */
public final class CarChargerController {


    public static ObservableList<CarCharger> CarChargerSlotAssign(ObservableList<CarCharger> solution, double[] price, double[] capacity){
        int[] hours_remaining;
        int[] priority= new int[48];
        double[] sorted_price= new double[48];
        double [] unsorted_price= new double[48];
        double [] car_capacity= new double[48];
        boolean flag =false;

        // for benchmarking execution time
        //long startTime = System.nanoTime();

        // sort price and calculate priority list
        for(int i=0; i<48; i++){
            unsorted_price[i]=price[i];
            sorted_price[i]=price[i];
            car_capacity[i]=capacity[i];
        }
        Arrays.sort(sorted_price);
        for(int j=0; j<48; j++){
            for(int k=0; k<48;k++){
                if (sorted_price[j]==unsorted_price[k]){
                    for (int l=0; l<48;l++) {
                        if(priority[l]==k){
                            flag=true;
                        }
                    }
                    if(flag){
                        flag=false;
                        continue;
                    }
                    priority[j]=k;
                    break;
                }
            }
        }
        // get size of solution and hours remaining to be charged for each car
        int size = solution.size();
        hours_remaining = new int[size];
        for (int i=0;i<size;i++){
			// array for hours to be charged remaining for each car, could be done directly at each object if passed by pointer
            hours_remaining[i]=solution.get(i).getChargeSlots();
            //System.out.println(hours_remaining[i]);
        }

        // assign time slot
        // for each time slot
        for (int i=0;i<48;i++){

            int timeslot=priority[i];

            //for each car
            for ( int j=0; j<size; j++){ 
				// if car not here or hours to be charged fully assigned or remaining capacity less than charging rate, continue with the next car
                if((solution.get(j).chargeTime[timeslot]==2)||(hours_remaining[j]==0)||(car_capacity[timeslot]<solution.get(j).getChargeRate())){
                    continue;
                }
				// else assign a charging slot and decrement hour to be charged and capacity
                else {
                    solution.get(j).chargeTime[timeslot]=3;
                    hours_remaining[j]--;
					car_capacity[timeslot]=car_capacity[timeslot]-solution.get(j).getChargeRate();
                }
            }
        }

        // for each car
        for (int k=0;k< size; k++){
			// for each timeslot
            if( hours_remaining[k]!=0){
                for (int l=0;l<48;l++){
					// if not here or assigned, continue with the next timeslot
                    if (solution.get(k).chargeTime[l]==2 || solution.get(k).chargeTime[l]==3){
                        continue;
                    }
					// assign 
                    else {
                        solution.get(k).chargeTime[l]=3;
                        hours_remaining[k]--;
                    }
					// if all hours taken care, break
                    if(hours_remaining[k]==0){
                        break;
                    }
					// might need a decrement on capacity here
                }
            }
        }

        long endTime = System.nanoTime();

        //double duration = (endTime - startTime)/1000000f ;

        //System.out.println(duration);

        return solution;
    }

    /** Generates a list of random car chargers. */
    public static ObservableList<CarCharger> generateRandomCarChargers(int length) {
        ObservableList<CarCharger> carChargerList = FXCollections.observableArrayList();

        for (int i = 0; i < length; i++){
            CarCharger carCharger = new CarCharger(i);
            carChargerList.add(carCharger);

            //System.out.println(String.format("Battery level: %f", carCharger.getBatteryLevel()));
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
