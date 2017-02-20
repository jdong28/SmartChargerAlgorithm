package fydp.controller;

import fydp.view.Solution;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

import static fydp.view.Solution.initialSolution;

public class MainViewController {
    private static int solSize;

    private static TitledPane[] tps;

    // each graph needs its own x-y axis values
    private static NumberAxis[] xAxis;
    private static NumberAxis[] yAxis;

    //
    private static AreaChart<Number, Number>[] ac;

    // charging schedule of each car
    private static XYChart.Series[] series;

    // javafx fields
    @FXML
    private Accordion fxScheduleAccordion;

    @FXML
    private void initialize() {
        InitializeFields();
        ConfigureGraphs();
    }

    private void ConfigureGraphs() {
        // loop through every car and graph the solution
        for (int i = 0; i < initialSolution.size(); i++) {
            xAxis[i] = new NumberAxis("Time", 0, 48, 1);
            yAxis[i] = new NumberAxis("Charge Rate (kW)", 1, 10, 1);
            ac[i] = new AreaChart<>(xAxis[i], yAxis[i]);

            series[i] = new XYChart.Series();
            double[] chargeTime = initialSolution.get(i).chargeTime;

            // loop through every half hour
            for (int y = 0; y < 47; y++) {
                if (chargeTime[y] == 3) {
                    series[i].getData().add(new XYChart.Data<>(y, initialSolution.get(i).getChargeRate()));
                }
                else {
                    series[i].getData().add(new XYChart.Data<>(y, chargeTime[y]));
                }
            }
            ac[i].getData().addAll(series[i]);
            String carName = String.format("Chrom number: %d", i);
            tps[i] = new TitledPane(carName, ac[i]);
        }
        fxScheduleAccordion.getPanes().addAll(tps);
        fxScheduleAccordion.setExpandedPane(tps[0]);
    }

    /** Initializes fields for algorithm and GUI */
    private static void InitializeFields() {
        Solution.GenerateInitialSolution(10);

        solSize = initialSolution.size();
        tps = new TitledPane[solSize];
        xAxis = new NumberAxis[solSize];
        yAxis = new NumberAxis[solSize];
        ac = new AreaChart[solSize];
        series = new XYChart.Series[initialSolution.size()];
    }
}
