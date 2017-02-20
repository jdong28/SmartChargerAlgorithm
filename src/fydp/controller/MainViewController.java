package fydp.controller;

import fydp.model.CarCharger;
import fydp.view.Solution;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

import static fydp.view.Solution.electricityPrice;
import static fydp.view.Solution.initialSolution;

public class MainViewController {
    private static int solSize;

    private static TitledPane[] tps;

    // each graph needs its own x-y axis values
    private static NumberAxis[] xAxis;
    private static NumberAxis[] yAxis;

    // may change to LineChart
    private static AreaChart<Number, Number>[] ac;

    // charging schedule of each car
    private static XYChart.Series[] series;

    // javafx fields
    @FXML
    private Accordion fxScheduleAccordion;
    @FXML
    private LineChart<Number, Number> priceComparisonChart;
    @FXML
    private LineChart<Number, Number> electricityChart;

    // runs first
    @FXML
    private void initialize() {
        InitializeFields();
        ConfigureChargingGraphs();
        ConfigureDemandGraph();
        ConfigureElectricityGraph();
    }

    private void ConfigureChargingGraphs() {
        // loop through every car and graph the solution
        for (int i = 0; i < initialSolution.size(); i++) {
            xAxis[i] = new NumberAxis("Time", 0, 48, 1);
            yAxis[i] = new NumberAxis("Charge Rate (kW)", 1, 10, 1);
            ac[i] = new AreaChart<>(xAxis[i], yAxis[i]);

            series[i] = new XYChart.Series();
            double[] currentChargeTime = initialSolution.get(i).chargeTime;

            // loop through every half hour
            for (int y = 0; y < 47; y++) {
                if (currentChargeTime[y] == 3) {
                    series[i].getData().add(new XYChart.Data<>(y, initialSolution.get(i).getChargeRate()));
                }
                else {
                    series[i].getData().add(new XYChart.Data<>(y, currentChargeTime[y]));
                }
            }
            ac[i].getData().addAll(series[i]);
            String carName = String.format("Chrom number: %d", i);
            tps[i] = new TitledPane(carName, ac[i]);
        }
        // Setup

        fxScheduleAccordion.getPanes().addAll(tps);
        fxScheduleAccordion.setExpandedPane(tps[0]);
    }

    private void ConfigureDemandGraph() {
        ObservableList<XYChart.Series<Number, Number>> data = FXCollections
                .observableArrayList();

        XYChart.Series beforeAlgo = new XYChart.Series();
        beforeAlgo.setName("Demand before optimization");
        XYChart.Series afterAlgo = new XYChart.Series();
        afterAlgo.setName("Demand after optimization");

        for (int i = 0; i < 47; i++) {

            double yValueBeforeAlgo = 0;
            double yValueAfterAlgo = 0;

            // loop through every car
            for (int j = 0; j < solSize; j ++) {
                CarCharger currentCar = initialSolution.get(j);
                // within charging period
                if (currentCar.unoptimizedChargeTime[i] == 1) {
                    yValueBeforeAlgo = yValueBeforeAlgo + currentCar.getChargeRate();
                }
                if (currentCar.chargeTime[i] == 3) {
                    yValueAfterAlgo = yValueAfterAlgo + currentCar.getChargeRate();
                }
            }
            beforeAlgo.getData().add(new XYChart.Data<>(i, yValueBeforeAlgo));

            afterAlgo.getData().add(new XYChart.Data<>(i, yValueAfterAlgo));
        }

        data.addAll(beforeAlgo, afterAlgo);
        priceComparisonChart.setData(data);
        priceComparisonChart.setTitle("Price Comparison");
    }

    private void ConfigureElectricityGraph() {
        ObservableList<XYChart.Series<Number, Number>> data = FXCollections
                .observableArrayList();
        XYChart.Series elecPrice = new XYChart.Series();

        for (int i = 0; i < 47; i++) {
            elecPrice.getData().add(new XYChart.Data<>(i, electricityPrice[i]));
        }

        data.addAll(elecPrice);
        electricityChart.setData(data);
        electricityChart.setTitle("Electricity Price");
    }

    /** Initializes fields for algorithm and GUI */
    private static void InitializeFields() {
        Solution.GenerateInitialSolution(50);

        solSize = initialSolution.size();
        tps = new TitledPane[solSize];
        xAxis = new NumberAxis[solSize];
        yAxis = new NumberAxis[solSize];
        ac = new AreaChart[solSize];
        series = new XYChart.Series[initialSolution.size()];
    }
}
