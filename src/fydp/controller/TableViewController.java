package fydp.controller;

import fydp.model.CarCharger;
import fydp.view.Main;
import fydp.view.Solution;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import static fydp.view.Solution.initialSolution;

/**
 * Created by xiuxu on 2017-03-07.
 */
public class TableViewController {

    @FXML
    private TableView<CarCharger> carChargerTable;

    @FXML
    private Label carIDLabel;
    @FXML
    private Label carBatteryLevelLabel;


    @FXML
    private void initialize() {
        //Solution generated here because this controller executes first
        Solution.generateInitialSolution(50);
        initializeTable();
    }

    private void initializeTable() {
        carChargerTable.setItems(initialSolution);

        TableColumn<CarCharger, Integer> carIDColumn = new TableColumn<>("Charger ID");
        carIDColumn.setCellValueFactory(new PropertyValueFactory<>("carIDProperty"));

        TableColumn<CarCharger, Double> carBatteryLevelColumn = new TableColumn<>("Battery Level");
        carBatteryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("carBatteryLevelProperty"));

        TableColumn<CarCharger, Double> startTimeColumn = new TableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeProperty"));

        TableColumn<CarCharger, Double> endTimeColumn = new TableColumn<>("End Time");
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeProperty"));

        //TableColumn<CarCharger, Double> chargeTimesColumn = new TableColumn<>("Charge Times");
        //chargeTimesColumn.setCellValueFactory(new PropertyValueFactory<>("chargeTimesProperty"));

        carChargerTable.getColumns().setAll(carIDColumn, carBatteryLevelColumn, startTimeColumn, endTimeColumn);
    }

    @FXML
    private void handleDeleteCar() {
        int index = carChargerTable.getSelectionModel().getSelectedIndex();
        carChargerTable.getItems().remove(index);
    }

    @FXML
    private void handleAddCar() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/fydp/view/CarInputDialog.fxml"));
        Parent page = loader.load();
        CarCharger tmpCar = new CarCharger();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("New Car");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        CarInputDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCarCharger(tmpCar);

        dialogStage.showAndWait();

        if(controller.isOkClicked()) {
            carChargerTable.getItems().add(controller.getCarCharger());
        }

    }
}
