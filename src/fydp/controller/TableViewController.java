package fydp.controller;

import fydp.model.CarCharger;
import fydp.view.Solution;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
        Solution.generateInitialSolution(25);
        initializeTable();
    }

    private void initializeTable() {
        carChargerTable.setItems(initialSolution);

        TableColumn<CarCharger, Integer> carIDColumn = new TableColumn<>("Charger ID");
        carIDColumn.setCellValueFactory(new PropertyValueFactory<>("carIDProperty"));

        TableColumn<CarCharger, Double> carBatteryLevelColumn = new TableColumn<>("Battery Level");
        carBatteryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("carBatteryLevelProperty"));

        carChargerTable.getColumns().setAll(carIDColumn, carBatteryLevelColumn);
    }

    @FXML
    private void handleDeleteCar() {
        int index = carChargerTable.getSelectionModel().getSelectedIndex();
        carChargerTable.getItems().remove(index);
    }
}
