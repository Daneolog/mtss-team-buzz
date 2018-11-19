package ui;

import corelogic.Bus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            //Tip in scene builder have fxml file correspond to controller class by setting fx:id packagename.controller
            Parent root = FXMLLoader.load(getClass().getResource("../../resources/UI_main.fxml"));
            //Label root = new Label("Hellow World!");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            primaryStage.show();
            primaryStage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
