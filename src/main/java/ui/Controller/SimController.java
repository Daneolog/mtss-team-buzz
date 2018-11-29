package ui.Controller;

import corelogic.Bus;
import corelogic.Route;
import corelogic.SimulationManager;
import corelogic.Stop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import ui.BusObject;
import ui.StopObject;

import java.util.HashMap;
import java.util.ResourceBundle;

public class SimController implements Initializable {

    @FXML
    private Pane simLayout;

    @FXML
    private GridPane lanes;

    @FXML
    private Accordion infoPanes;

    @FXML
    private TitledPane busInfo;

    @FXML
    private ToggleGroup routes;

    @FXML
    private Button addBus;

    @FXML
    private Button addStop;

    @FXML
    private Button stopButton;

    @FXML
    private Button createSimButton;

    @FXML
    private ToggleButton fastForward;

    @FXML
    private ToggleButton play;

    @FXML
    private Button load;

    @FXML
    private Text busInfoNumPpl;

    @FXML
    private Text busInfoStop;

    @FXML
    private Text busInfoNextS;

    @FXML
    private Text busInfoBusId;

    @FXML
    private Text busInfoSpeed;

    @FXML
    private Text busInfoRoute;

    @FXML
    private MenuButton busInfoStops;

    @FXML
    private TitledPane stopInfo;

    @FXML
    private Text stopInfoArrive;

    @FXML
    private Text stopInfoId;

    @FXML
    private Text stopInfoName;

    @FXML
    private MenuButton stopInfoDes;

    @FXML
    private Text stopInfoDrate;



    private File file;

    private HashMap<Integer, BusObject> buses;

    private HashMap<Integer, StopObject> stops;

    private HashMap<Integer, Route> routesMap;

    private boolean simLoaded;

    // variable for initialization purposes so that we don't continually add same objects on grid pane

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        simLayout.setVisible(true);
        this.file = null;
        this.buses = new HashMap<>();
        this.stops = new HashMap<>();
        this.routesMap = new HashMap<>();
        setInfoTextVisible(false, false, true);

    }

    public HashMap<Integer, BusObject> getBuses() {
        return this.buses;
    }

    public HashMap<Integer, StopObject> getStops() {
        return this.stops;
    }

    public HashMap<Integer, Route> getRoutesMap() { return this.routesMap; }

    private void updatePlaceBus(BusObject element, double height, double width, int row) {
        element.setFitHeight(height);
        element.setFitWidth(width);
        Bus bus = element.getBus();
        element.setOnMouseClicked(event -> {
            updateBusInfoPane(bus);
        });
        buses.put(bus.getId(), element);
        lanes.add(element, 0, row);
    }

    private void updatePlaceStop(StopObject element, double height, double width, int row) {
        element.setFitHeight(height);
        element.setFitWidth(width);
        Stop stop = element.getStop();
        element.setOnMouseClicked(event -> {
            updateStopInfoPane(stop);
        });
        stops.put(stop.getId(), element);
        lanes.add(element, 1, row);
    }

    /**
     * Clears all data from ui and sets data structures to initial state.
     */
    private void clearData() {
        this.lanes.getChildren().removeAll(buses.values());
        this.lanes.getChildren().removeAll(stops.values());
        this.buses = new HashMap<>();
        this.stops = new HashMap<>();
        setInfoTextVisible(false, false, true);
    }

    /**
     * use to clear textfield and stops in bus pane
     * @param busVisible if true to make text visible otherwise not visible
     * @param stopVisible if true to makke text visibile in stopPane
     * @param clear if true clear stops list
     */
    private void setInfoTextVisible(boolean busVisible, boolean stopVisible, boolean clear) {
        //bus pane
        this.busInfoBusId.setVisible(busVisible);
        this.busInfoSpeed.setVisible(busVisible);
        this.busInfoNumPpl.setVisible(busVisible);
        this.busInfoStop.setVisible(busVisible);
        this.busInfoNextS.setVisible(busVisible);
        //stop pane
        this.stopInfoDrate.setVisible(stopVisible);
        this.stopInfoArrive.setVisible(stopVisible);
        this.stopInfoName.setVisible(stopVisible);
        this.stopInfoId.setVisible(stopVisible);
        TitledPane expanded = this.infoPanes.getExpandedPane();
        if (expanded != null) {
            if (expanded.getText().equals("Bus Info")) {
                this.infoPanes.getExpandedPane().setExpanded(busVisible);
            } else if (expanded.getText().equals("Stop Info")) {
                expanded.setExpanded(stopVisible);
            }
        }
        if (clear) {
            this.busInfoStops.getItems().clear();
            this.stopInfoDes.getItems().clear();
        }
    }

    /**
     * Used to update the busInfo pane
     * @param bus
     */
    private void updateBusInfoPane(Bus bus) {
        //TODO
        // may need to change this to be contained in border pane for highlighting
        infoPanes.setExpandedPane(busInfo);
        this.busInfoBusId.setText(String.valueOf(bus.getId()));
        this.busInfoStop.setText(bus.getCurrentStop().getName());
        this.busInfoNextS.setText(bus.getNextStop().getName());
        this.busInfoNumPpl.setText(String.valueOf(bus.getNumPassengers()));
        this.busInfoSpeed.setText(String.valueOf(bus.getSpeed()));
        this.busInfoRoute.setText("Route: " + bus.getRoute().getId());
        setInfoTextVisible(true, false, true);
        for (Stop stop : bus.getRoute().getStops()) {
            MenuItem stopDisplay = new MenuItem(stop.getName());
            this.busInfoStops.getItems().add(stopDisplay);
        }
    }

    private void updateStopInfoPane(Stop stop) {
        //TODO
        // Issue with only beeing able to click on image itself and not white interior
        infoPanes.setExpandedPane(this.stopInfo);
        this.stopInfoId.setText(String.valueOf(stop.getId()));
        this.stopInfoName.setText(stop.getName());
        this.stopInfoArrive.setText(String.valueOf(stop.getArrivalRate()));
        this.stopInfoDrate.setText(String.valueOf(stop.getDisembarkRate()));
        setInfoTextVisible(false, true, true);
        for (Stop des : stop.getDestinations()) {
            MenuItem stopDisplay = new MenuItem(des.getName());
            this.stopInfoDes.getItems().add(stopDisplay);
        }
    }


    @FXML
    public void createSimFile(ActionEvent event) throws IOException {
        if (!simLoaded) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI_date.fxml"));
            Scene scene = new Scene(loader.load());
            Stage addBusStage = new Stage();
            addBusStage.initOwner(addBus.getScene().getWindow());
            addBusStage.initModality(Modality.APPLICATION_MODAL); // only allows child stage to allow for user events.
            addBusStage.setScene(scene);
            addBusStage.setResizable(false);
            addBusStage.showAndWait();
        }
    }

    @FXML
    public void loadSim(ActionEvent e) {
        // getting the stage to pass to file chooser
        Window primaryStage = simLayout.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        this.file = fileChooser.showOpenDialog(primaryStage);
        if (this.file != null) {
            this.simLoaded = true;
            // clearing buses and stops from ui
            clearData();
            SimulationManager.initSim(this.file.getAbsolutePath(), 1000, 5);
            Image busImage = new Image("busImg.png");
            Image stopImage = new Image("stopImg.png");
            routesMap = SimulationManager.getRoutes();

            // intializing bus and stop objexts to be place in the grid
            int counter = 0;
            for (Stop stop : SimulationManager.getStops().values()) {
                StopObject stopObject = new StopObject(stop, stopImage, counter);
                updatePlaceStop(stopObject, 70, 70, counter);
                counter++;
            }
            for (Bus bus : SimulationManager.getBuses().values()) {
                BusObject busObject = new BusObject(bus, busImage);
                updatePlaceBus(busObject, 70, 70, stops.get(bus.getCurrentStop().getId()).getLaneNumber());
            }
        }
    }

    @FXML
    public void playSim(ActionEvent e) {
        //TODO
        SimulationManager.togglePlay();
    }

    @FXML
    public void addBusSim(ActionEvent e) throws IOException {
        if (simLoaded) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI_addBus.fxml"));
            Scene scene = new Scene(loader.load());
            Stage addBusStage = new Stage();
            addBusStage.initOwner(addBus.getScene().getWindow());
            addBusStage.initModality(Modality.APPLICATION_MODAL); // only allows child stage to allow for user events.
            addBusStage.setScene(scene);
            addBusStage.setResizable(false);

            //passing and recieving information from the controllers
            addBusController controller = loader.getController();
            controller.setParent(this); // used to access variables from this controller in child.
            controller.getBusProperty().addListener((observable, oldValue, newValue) -> {
                // listerner to changes in creating a new bus object to update ui
                Bus newBus = newValue.getBus();
                SimulationManager.getBuses().put(newBus.getId(), newBus);
                StopObject uiStop = stops.get(newBus.getCurrentStop().getId());
                updatePlaceBus(newValue, 70, 70, stops.get(newBus.getCurrentStop().getId()).getLaneNumber());
                System.out.println(newBus.getId());

            });
            addBusStage.showAndWait(); // wait till addBusStage closes to resume execution
        }

    }

    @FXML
    public void stopSim(ActionEvent e) {
        this.simLoaded = false;
        clearData();
    }
}
