package fydp.view;

import fydp.controller.CarInputDialogController;
import fydp.model.CarCharger;
import fydp.model.ReadConsoleRunnable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;

/**
 * Method for running algorithm
 */
public class Main extends Application{

    public static Map<String, String> parameters;

    @Override
    public void start(Stage primaryStage) throws Exception{
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../fydp/view/Main.fxml"));
//        loader.setController(this);
//        Parent root = loader.load();
        //Scene scene = new Scene(new Group(), 800, 600);
        parameters = getParameters().getNamed();

        (new Thread(new ReadConsoleRunnable())).start();

        Parent root = FXMLLoader.load(getClass().getResource("../../fydp/view/Main.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("SmartCharger Algorithm");
        // Group root = (Group) scene.getRoot();
        // root.getChildren().add(accordion);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
