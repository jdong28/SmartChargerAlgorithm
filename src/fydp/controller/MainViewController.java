package fydp.controller;

import fydp.model.CarCharger;
import fydp.view.Solution;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import static fydp.view.Main.parameters;
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


    private static double[] demandBefore = new double[48];
    private static double[] demandAfter = new double[48];

    // javafx fields
    @FXML
    private Accordion fxScheduleAccordion;
    @FXML
    private LineChart<Number, Number> priceComparisonChart;
    @FXML
    private LineChart<Number, Number> electricityChart;
    @FXML
    private ListView<String> dataListView;
    @FXML
    private Label priceBeforeLabel;
    @FXML
    private Label priceAfterLabel;
    @FXML
    private Label priceDifferenceLabel;


    // runs first
    @FXML
    private void initialize() {
        initializeFields();
        setSolutionListener();
        configureChargingGraphs();
        configureDemandGraph();
        configureElectricityGraph();
        //String teststring = GetPrice.getElectricityPrice();
        configureDataLabels();
    }

    private void configureDataLabels() {
        double costbefore = 0;
        double costafter = 0;
        for (int i = 0; i < 48; i++) {
            costbefore = costbefore + demandBefore[i] * electricityPrice[i];
            costafter = costafter + demandAfter[i] * electricityPrice[i];
        }

        priceBeforeLabel.setText("Price before: " + Double.toString(costbefore));
        priceAfterLabel.setText("Price after: " + Double.toString(costafter));
        priceDifferenceLabel.setText("Price difference: " + Double.toString(costbefore - costafter));
        System.out.println("Price difference: " + Double.toString(costbefore - costafter));
    }

    private void configureChargingGraphs() {
        // loop through every car and graph the solution
        for (int i = 0; i < initialSolution.size(); i++) {
            xAxis[i] = new NumberAxis("Time", 0, 24, 0.5);
            yAxis[i] = new NumberAxis("Charge Rate (kW)", 1, 10, 1);
            ac[i] = new AreaChart<>(xAxis[i], yAxis[i]);

            series[i] = new XYChart.Series();
            double[] currentChargeTime = initialSolution.get(i).chargeTime;

            // loop through every half hour
            for (int y = 0; y < 48; y++) {
                double xValue = 0;
                // even
                if ( (y & 1) == 0 ) {
                    xValue = y/2;
                }
                // odd
                else {
                    xValue = y/2 + 0.5;
                }

                if (currentChargeTime[y] == 3) {
                    series[i].getData().add(new XYChart.Data<>(xValue, initialSolution.get(i).getChargeRate()));
                }
                else {
                    series[i].getData().add(new XYChart.Data<>(xValue, initialSolution.get(i).chargeTime[y]));
                }
            }
            ac[i].getData().addAll(series[i]);
            ac[i].setLegendVisible(false);
            String carName = String.format("Car number: %d", initialSolution.get(i).getCarID());
            tps[i] = new TitledPane(carName, ac[i]);
        }
        // Setup

        if (!fxScheduleAccordion.getPanes().isEmpty()) {
            fxScheduleAccordion.getPanes().clear();
        }

        fxScheduleAccordion.getPanes().addAll(tps);
        fxScheduleAccordion.setExpandedPane(tps[0]);
    }

    private void configureDemandGraph() {
        ObservableList<XYChart.Series<Number, Number>> data = FXCollections
                .observableArrayList();

        XYChart.Series beforeAlgo = new XYChart.Series();
        beforeAlgo.setName("Demand before optimization");
        XYChart.Series afterAlgo = new XYChart.Series();
        afterAlgo.setName("Demand after optimization");

        for (int i = 0; i < 48; i++) {

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

            demandBefore[i] = yValueBeforeAlgo;
            demandAfter[i] = yValueAfterAlgo;
        }

        data.addAll(beforeAlgo, afterAlgo);
        priceComparisonChart.setData(data);
        priceComparisonChart.setTitle("Demand Comparison");
    }

    private void configureElectricityGraph() {
        ObservableList<XYChart.Series<Number, Number>> data = FXCollections
                .observableArrayList();
        XYChart.Series elecPrice = new XYChart.Series();

        for (int i = 0; i < 48; i++) {
            elecPrice.getData().add(new XYChart.Data<>(i, electricityPrice[i]));
        }

        data.addAll(elecPrice);
        electricityChart.setData(data);
        electricityChart.setTitle("Electricity Price");
    }

    /** Initializes fields for algorithm and GUI */
    private void initializeFields() {
        System.out.println(parameters);


        //Child controller is called first
        //Solution.generateInitialSolution(25);

        solSize = initialSolution.size();
        tps = new TitledPane[solSize];
        xAxis = new NumberAxis[solSize];
        yAxis = new NumberAxis[solSize];
        ac = new AreaChart[solSize];
        series = new XYChart.Series[initialSolution.size()];
    }

    private void setSolutionListener() {
        initialSolution.addListener(new ListChangeListener<CarCharger>() {
            @Override
            public void onChanged(Change<? extends CarCharger> c) {
                while (c.next()) {
                    // prevents firing on sorting
                    if (c.wasAdded() || c.wasRemoved()) {
                        initializeFields();
                        refreshGraphs();
                    }
                }
            }
        });
    }

    private void refreshGraphs() {
        Solution.calculateSolution();
        configureChargingGraphs();
        configureDemandGraph();
        configureDataLabels();
    }
}
