package fydp.view;
import java.util.ArrayList;
import java.util.List;

import fydp.model.CarCharger;
import fydp.model.CarChargerController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.util.Pair;
/**
 * Method for running algorithm
 */
public class Main extends Application {

    private static List<CarCharger> initialSolution =
            CarChargerController.GenerateRandomCarChargers(10);

    private int solSize = initialSolution.size();

    private TitledPane[] tps = new TitledPane[solSize];

    // each graph needs its own x-y axis values
    private NumberAxis[] xAxis = new NumberAxis[solSize];
    private NumberAxis[] yAxis = new NumberAxis[solSize];

    //
    AreaChart<Number, Number>[] ac = new AreaChart[solSize];

    // charging schedule of each car
    private XYChart.Series[] series = new XYChart.Series[initialSolution.size()];

    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("../../fydp/view/Main.fxml"));
        Scene scene = new Scene(new Group(), 800, 600);
        final Accordion accordion = new Accordion();

        // loop through every car and graph the solution
        for (int i = 0; i < initialSolution.size(); i++) {
            xAxis[i] = new NumberAxis("Time", 0, 48, 1);
            yAxis[i] = new NumberAxis("Charge Rate (kW)", 1, 10, 1);
            ac[i] = new AreaChart<>(xAxis[i], yAxis[i]);

            series[i] = new XYChart.Series();
            double[] chargeTime = initialSolution.get(i).chargeTime;

            // loop through every half hour
            for (int y = 0; y < 47; y++) {
                if (chargeTime[y] == 1) {
                    series[i].getData().add(new XYChart.Data<>(y, initialSolution.get(i).getCharge_rate()));
                }
                else {
                    series[i].getData().add(new XYChart.Data<>(y, chargeTime[y]));
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

        launch(args);
    }

}
