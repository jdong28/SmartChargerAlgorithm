package fydp.view;
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

public class Main extends Application {

    List<List<CarCharger>> chrom = CarChargerController.GenerateInitialSolution(5,10);

    final TitledPane[] tps = new TitledPane[chrom.size()];
    final NumberAxis xAxis = new NumberAxis("Time", 1, 24, 1);
    final NumberAxis yAxis = new NumberAxis("Charge Rate (kW)", 1, 10, 1);
    AreaChart<Number, Number>[] ac = new AreaChart[chrom.size()];

    // charging schedule of each car, gets number of cars from 1st solution
    private XYChart.Series[] series = new XYChart.Series[chrom.get(0).size()];

    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("../../fydp/view/Main.fxml"));
        Scene scene = new Scene(new Group(), 400, 600);
        final Accordion accordion = new Accordion();

        // loop through every parking lot and graph the solution
        for (int i = 0; i < chrom.size(); i++) {
            ac[i] = new AreaChart<>(xAxis, yAxis);

            // loop through every charger
            for (int x = 0; x < chrom.get(i).size(); x++) {
                series[x] = new XYChart.Series();
                double[] chargeTime = chrom.get(i).get(x).getChargeTime();

                // loop through every half hour
                for (int y = 0; y < 48; y++) {
                    if (chargeTime[y] == 1) {
                        series[x].getData().add(new XYChart.Data<>(y, chrom.get(i).get(x).getCharge_rate()));
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
        launch(args);
    }

}
