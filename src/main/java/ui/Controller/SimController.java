package ui.Controller;

import corelogic.Bus;
import corelogic.SimulationManager;
import corelogic.Stop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ToggleGroup;

import java.io.File;
import java.net.URL;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import ui.BusObject;
import ui.StopObject;

import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class SimController implements Initializable {

    @FXML
    private Pane simLayout;

    @FXML
    private GridPane lanes;

    @FXML
    private ToggleGroup routes;

    @FXML
    private Button addBus;

    @FXML
    private Button addStop;

    @FXML
    private Button stopButton;

    @FXML
    private Button fastForward;

    @FXML
    private ToggleButton play;

    @FXML
    private Button load;

    private File file;

    private HashMap<Integer, BusObject> buses;

    private HashMap<Integer, StopObject> stops;

    // variable for initialization purposes so that we don't continually add same objects on grid pane

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        simLayout.setVisible(true);
        this.file = null;
        this.buses = new HashMap<>();
        this.stops = new HashMap<>();
    }

    @FXML
    public void loadSim(ActionEvent e) {
        // getting the stage to pass to file chooser
        lanes.getChildren().removeAll(buses.values());
        lanes.getChildren().removeAll(stops.values());
        this.buses = new HashMap<>();
        this.stops = new HashMap<>();
        Window primaryStage = simLayout.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        this.file = fileChooser.showOpenDialog(primaryStage);
        if (this.file != null) {
            System.out.println(this.file.getAbsolutePath());
            SimulationManager.initSim(this.file.getAbsolutePath(), 1);
            Image busImage = new Image("busImg.png");
            Image stopImage = new Image("stopImg.png");

            // intializing bus and stop objexts to be place in the grid
            for (Bus bus : SimulationManager.getBuses().values()) {
                BusObject busObject = new BusObject(bus, busImage);
                busObject.setFitHeight(70);
                busObject.setFitWidth(70);
                buses.put(bus.getId(), busObject);
                lanes.add(buses.get(bus.getId()), 0, bus.getCurrentStop().getId());
            }
            for (Stop stop : SimulationManager.getStops().values()) {
                StopObject stopObject = new StopObject(stop, stopImage);
                stopObject.setFitHeight(70);
                stopObject.setFitWidth(70);
                stops.put(stop.getId(), stopObject);
                lanes.add(stops.get(stop.getId()), 1, stop.getId());
            }
            for (BusObject bus: buses.values()) {
                System.out.println(bus.getBus().getId());

            }
            System.out.println(lanes.getChildren().size());
        }
    }

    @FXML
    public void playSim(ActionEvent e) {
       //TODO
    }

    @FXML
    public void stopSim(ActionEvent e) {
        lanes.getChildren().removeAll(buses.values());
        lanes.getChildren().removeAll(stops.values());
        this.buses = new HashMap<>();
        this.stops = new HashMap<>();
    }
}
