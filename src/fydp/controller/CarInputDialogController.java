package fydp.controller;

import fydp.model.CarCharger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.Scanner;

import static fydp.view.Solution.initialSolution;

/**
 * Created by xiuxu on 2017-03-09.
 */
public class CarInputDialogController {
    @FXML
    private TextField carIDField;
    @FXML
    private TextField batteryLevelField;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;
    @FXML
    private TextField chargeRateField;

    private Stage dialogStage;
    private CarCharger carCharger;

    private boolean okClicked = false;

    public CarCharger getCarCharger() {
        return carCharger;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCarCharger(CarCharger carCharger) {
        this.carCharger = carCharger;
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (carIDField.getText() == null
                || carIDField.getText().length() == 0
                || !isInteger(carIDField.getText())) {
            errorMessage += "Not valid Car ID!\n";
        }
        else {
            for (CarCharger car : initialSolution) {
                if (car.getCarID() == Integer.parseInt(carIDField.getText())) {
                    errorMessage += "Car ID already exists!\n";
                }
            }
        }
        if (batteryLevelField.getText() == null
                || batteryLevelField.getText().length() == 0
                || Double.parseDouble(batteryLevelField.getText()) > 1
                || Double.parseDouble(batteryLevelField.getText()) < 0) {
            errorMessage += "Not valid battery level!\n";
        }

        if (startTimeField.getText() == null
                || startTimeField.getText().length() == 0
                || !isInteger(startTimeField.getText())) {
            errorMessage += "Not valid start time!\n";
        }

        if (endTimeField.getText() == null
                || endTimeField.getText().length() == 0
                || !isInteger(endTimeField.getText())) {
            errorMessage += "Not valid end time!\n";
        }
        if (chargeRateField.getText() == null
                || chargeRateField.getText().length() == 0) {
            errorMessage += "Not valid charge rate!\n";
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            int carID = Integer.parseInt(carIDField.getText());
            double batteryLevel = Double.parseDouble(batteryLevelField.getText());
            int startTime = Integer.parseInt(startTimeField.getText());
            int endTime = Integer.parseInt(endTimeField.getText());
            double chargeRate = Double.parseDouble(chargeRateField.getText());

            carCharger = new CarCharger(carID, batteryLevel, startTime, endTime, chargeRate);

            okClicked = true;
            dialogStage.close();
        }
    }
    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public static boolean isInteger(String s) {
        int radix = 10;
        Scanner sc = new Scanner(s.trim());
        if(!sc.hasNextInt(radix)) return false;
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(radix);
        return !sc.hasNext();
    }
}
