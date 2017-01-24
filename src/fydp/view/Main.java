package fydp.view;
import java.util.ArrayList;
import java.util.List;

import fydp.model.CarCharger;
import fydp.model.CarChargerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

    private static List<List<CarCharger>> chrom = CarChargerController.GenerateInitialSolution(5,10);

    private int chromSize = chrom.size();

    private TitledPane[] tps = new TitledPane[chromSize];

    // each graph needs its own x-y axis values
    private NumberAxis[] xAxis = new NumberAxis[chromSize];
    private NumberAxis[] yAxis = new NumberAxis[chromSize];

    //
    AreaChart<Number, Number>[] ac = new AreaChart[chromSize];

    // charging schedule of each car, gets number of cars from 1st solution
    private XYChart.Series[] series = new XYChart.Series[chrom.get(0).size()];

    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("../../fydp/view/Main.fxml"));
        Scene scene = new Scene(new Group(), 800, 600);
        final Accordion accordion = new Accordion();

        // loop through every parking lot and graph the solution
        for (int i = 0; i < chrom.size(); i++) {
            xAxis[i] = new NumberAxis("Time", 1, 24, 1);
            yAxis[i] = new NumberAxis("Charge Rate (kW)", 1, 10, 1);
            ac[i] = new AreaChart<>(xAxis[i], yAxis[i]);

            // loop through every charger
            for (int x = 0; x < chrom.get(i).size(); x++) {
                series[x] = new XYChart.Series();
                double[] chargeTime = chrom.get(i).get(x).getChargeTime();

                // loop through every half hour
                for (int y = 0; y < 48; y++) {
                    if (chargeTime[y] == 1) {
                        series[x].getData().add(new XYChart.Data<>(y, chrom.get(i).get(x).getCharge_rate()));
                    }
                    else {
                        series[x].getData().add(new XYChart.Data<>(y, 0));
                    }
                }
            }
            ac[i].getData().addAll(series[i]);
            String carName = String.format("Chrom number: %d", i);
            tps[i] = new TitledPane(carName, ac[i]);
        }
        accordion.getPanes().addAll(tps);
        accordion.setExpandedPane(tps[0]);

        primaryStage.setTitle("SmartCharger Algorithm");
        Group root = (Group) scene.getRoot();
        root.getChildren().add(accordion);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
	// Generate list of cars

        double[] electricityPrice = new double[48];

        for (int i = 0; i < 48; i ++) {
            electricityPrice[i] = 0.035;
        }

        List<Pair<List<CarCharger>, Double>> chromWithFit = new ArrayList<>();
        chromWithFit = CarChargerController.EvaluateFitness(chrom, electricityPrice);


        launch(args);
    }

}
