package fydp.model;

import fydp.controller.CarInputDialogController;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import static fydp.view.Solution.initialSolution;

/**
 * Created by xiuxu on 2017-03-09.
 */
public class ReadConsoleRunnable implements Runnable {
    public void run() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {

                //System.out.print("Enter vehicle info to be added : add (ID, battery, start, end, charge rate\n");

                String input = br.readLine();

                String sl[] = input.split(" ");

                if (sl[0].equals("-add") && isValid(sl)) {
                    int carID = Integer.parseInt(sl[1]);
                    double batteryLevel = Double.parseDouble(sl[2]);
                    int startTime = Integer.parseInt(sl[3]);
                    int endTime = Integer.parseInt(sl[4]);
                    double chargeRate = Double.parseDouble(sl[5]);

                    // UI changes must occur on javafx thread.
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            initialSolution.add(new CarCharger(carID, batteryLevel, startTime, endTime, chargeRate));  // Update UI here.
                        }
                    });


                    //System.out.println("Car added");
                }

                if (sl[0].equals("-delete")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 1; i < sl.length; i++) {
                                int index = Integer.parseInt(sl[i]);

                                for (Iterator<CarCharger> iterator = initialSolution.iterator(); iterator.hasNext(); ) {
                                    CarCharger next = iterator.next();
                                    if (next.getCarID() == index) {
                                        iterator.remove();
                                    }
                                }
                            }
                        }
                    });
                }

                if ("q".equals(input)) {
                    System.out.println("Exit!");
                    //System.exit(0);
                }

                //System.out.println("input : " + input);
                //System.out.println("-----------\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isValid(String[] list) {
        if (list.length != 6) {
            System.out.println("Invalid input");
            return false;
        }
        if (!CarInputDialogController.isInteger(list[1])) {
            return false;
        }
        int carID = Integer.parseInt(list[1]);
        double batteryLevel = Double.parseDouble(list[2]);
        int startTime = Integer.parseInt(list[3]);
        int endTime = Integer.parseInt(list[4]);
        double chargeRate = Double.parseDouble(list[5]);

        for (CarCharger car : initialSolution) {
            if (car.getCarID() == carID) {
                System.out.println("Duplicate Car ID");
                return false;
            }
        }

        if (batteryLevel < 0 || batteryLevel > 1) {
            System.out.println("Invalid battery level");
            return false;
        }

        if (startTime < 1 || startTime > 48 || endTime < 1 || endTime > 48 || startTime > endTime) {
            System.out.println("Invalid times");
            return false;
        }

        return true;
    }
}
