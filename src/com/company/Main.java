package com.company;

import java.util.List;

public class Main {

    public static void main(String[] args) {
	// Generate list of cars
        List<List<CarCharger>> chrom = CarChargerController.GenerateInitialSolution(5,10);
    }
}
