package fydp.view;

import fydp.model.CarCharger;
import fydp.model.CarChargerController;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by xiuxu on 2017-02-19.
 */
public class Solution {
    public static List<CarCharger> initialSolution;

    public static void GenerateInitialSolution (int num){
        initialSolution = CarChargerController.generateRandomCarChargers(num);

        // Generate list of cars

        double[] electricityPrice = new double[48];
        int[] capacity = new int[48];

        // loop to be removed
        for(int i=0; i<48; i++) {
            capacity[i]=100;
        }
        for (int i = 0; i < 48; i ++) {
            electricityPrice[i] = ThreadLocalRandom.current().nextDouble(0, 0.5);
        }

        // sorts cars based on charging priority
        Collections.sort(initialSolution, (o1, o2) -> o1.getChargePriority() < o2.getChargePriority() ? 1:-1);

        // assigns car charging schedule
        initialSolution = CarChargerController.CarChargerSlotAssign(initialSolution,electricityPrice, capacity);
    }
}
