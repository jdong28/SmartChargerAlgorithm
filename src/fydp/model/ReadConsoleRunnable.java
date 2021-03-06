package fydp.model;

import fydp.controller.CarInputDialogController;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringJoiner;
import java.io.*;
import java.net.*;
import static fydp.view.Solution.initialSolution;

import org.json.simple.JSONObject;

/**
 * Created by xiuxu on 2017-03-09.
 */
public class ReadConsoleRunnable implements Runnable {
    // might need if he isn't already running a background thread to run this.
//    public static void main (String[] args) throws Exception{
//        ReadConsoleRunnable SERVER = new ReadConsoleRunnable();
//        SERVER.run();
//    }

    public int charge;
    public int chargerate;
    public int chargeslots;
    public int batteryProgress;
    public void run(){

        ServerSocket SRVSOCK = null;
        BufferedReader br = null;
        Socket SOCK = null;
        try {
            SRVSOCK = new ServerSocket(444);
            SOCK = SRVSOCK.accept();
            InputStreamReader IR = new InputStreamReader(SOCK.getInputStream());

            // instead of this: br = new BufferedReader(new InputStreamReader(System.in));
            br = new BufferedReader(IR);

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
                charge = 0;
                if (sl[0].equals("-charge")) {
                    charge = 1;
                }

                chargerate = 0;
                if (sl[0].equals("-chargerate")) {
                    chargerate = 1;
                }
                chargeslots = 0;
                if (sl[0].equals("-chargeslots")) {
                    chargeslots = 1;
                }
                batteryProgress = 0;
                if (sl[0].equals("-batteryProgress")) {
                    batteryProgress = 1;
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


                if (SOCK.isConnected()) {

                    PrintStream PS = new PrintStream(SOCK.getOutputStream());


                    //ArrayList<CarCharger> idSortedList = new ArrayList<>(initialSolution);
                    // idSortedList.sort((o1, o2) -> (o1.getCarID() - o2.getCarID()));
                    //PS.println(Arrays.toString(carCharger.chargeTime));
                    // TODO: add loop to output into database the list of cars
                    JSONObject dataSet = new JSONObject();


                    for (Iterator<CarCharger> iterator = initialSolution.iterator(); iterator.hasNext(); ) {
                        CarCharger carCharger = iterator.next();

                        //dataSet.put(carCharger.getCarID(),Arrays.toString(carCharger.batteryProgress));


                        dataSet.put(carCharger.getCarID(), Arrays.toString(carCharger.chargeTime));
                        if (batteryProgress == 1) {
                            dataSet.put(carCharger.getCarID(), Arrays.toString(carCharger.batteryProgress));
                        }

                        if (charge == 1) {
                            dataSet.put(carCharger.getCarID(), Double.toString(carCharger.getBatteryLevel()));
                        }
                        if (chargerate == 1) {
                            dataSet.put(carCharger.getCarID(), Double.toString(carCharger.getChargeRate()));
                        }
                        if (chargeslots == 1) {
                            dataSet.put(carCharger.getCarID(), Integer.toString(carCharger.getChargeSlots()));
                        }
                        //System.out.println(carCharger.getCarID());
                        //System.out.println(Arrays.toString(carCharger.chargeTime));

                    }
                    PS.println(dataSet);
                }

                if ("q".equals(input)) {
                    System.out.println("Exit!");
                    //System.exit(0);
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        if (br != null) {
            try {
                br.close();
                SOCK.close();
                SRVSOCK.close();
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
