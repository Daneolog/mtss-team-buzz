package ui.Controller;

import corelogic.Bus;
import corelogic.Route;
import corelogic.SimulationManager;
import corelogic.Stop;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
import javafx.util.Duration;
import ui.BusObject;
import ui.ImageWrapper;
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

    private int interval;

    private File file;

    private HashMap<Integer, ImageWrapper> buses;

    private HashMap<Integer, ImageWrapper> stops;

    private HashMap<Integer, Route> routesMap;

    private ToggleGroup clipBoard;

    private boolean simLoaded; // Very important boolean value that checks if sim is loaded

    private double ROW_WIDTH;

    private Timeline animation;

    private double nodeStartPosition;

    private int rowNum;

    private int multiplier;

    // variable for initialization purposes so that we don't continually add same objects on grid pane

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        simLayout.setVisible(true);
        this.file = null;
        this.buses = new HashMap<>();
        this.stops = new HashMap<>();
        this.routesMap = new HashMap<>();
        this.clipBoard = new ToggleGroup();
        this.interval = 2000;
        this.multiplier = 2;
        this.ROW_WIDTH = lanes.getMaxWidth();
        this.rowNum = 0;
        // check to see if a specific image wrapper has been clicked then highlights and update all relevant info.
        this.lanes.setOnMouseClicked(event -> {
            if (event.getTarget() instanceof ImageWrapper) {
                clipBoard.selectToggle((ImageWrapper) event.getTarget());
            } else {
                clipBoard.selectToggle(null);
            }
        });
        int duration = 100; // change duration for update
        KeyFrame frame = new KeyFrame(Duration.millis(duration), event -> {
            for (ImageWrapper busLabel : buses.values()) {
                moveBus(busLabel, duration);
            }
        });
        animation = new Timeline(frame);
        animation.setCycleCount(Timeline.INDEFINITE);
        setInfoTextVisible(false, false, true);

    }

    public HashMap<Integer, ImageWrapper> getBuses() {
        return this.buses;
    }

    public int getRowNum() {
        return this.rowNum;
    }

    public HashMap<Integer, Route> getRoutesMap() { return this.routesMap; }

    /**
     * Used to animate the bus
     * @param busLabel
     * @param duration
     */
    private void moveBus(ImageWrapper busLabel, int duration) {
        BusObject busImage = (BusObject) busLabel.getGraphic();
        ImageWrapper stopLabel = stops.get(busImage.getLaneNumber());
        Stop stop = busImage.getBus().getCurrentStop();
        if ( stop != ((StopObject) stopLabel.getGraphic()).getStop()) {
            busLabel.setTranslateX(this.nodeStartPosition);
            stopLabel.setText(stop.getName());
            ((StopObject) stopLabel.getGraphic()).setStop(stop);
        }

        double arrivalTimeBus = (((BusObject) busLabel.getGraphic()).getBus().getDeltaArrivalTime()) * this.interval;
        //System.out.println(arrivalTimeBus);
        double deltaMove = ROW_WIDTH / (arrivalTimeBus / duration);
        //System.out.println(deltaMove);
        busLabel.setTranslateX(busLabel.getTranslateX() + deltaMove);
    }

    private void intializeBus(BusObject element, double height, double width, int row) {
        element.setFitHeight(height);
        element.setFitWidth(width);
        Bus bus = element.getBus();
        ImageWrapper newBusBorder = new ImageWrapper(String.valueOf(bus.getId()), element);
        newBusBorder.setContentDisplay(ContentDisplay.TOP);
        newBusBorder.setOnMouseClicked(event -> {
            updateBusInfoPane(bus);

        });
        // setup to make the ImageWrapper highlighted
        // Cant place updateInfoPane inside listener as it does not work properly
        newBusBorder.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                newBusBorder.getStyleClass().add("highlighted");
            } else {
                newBusBorder.getStyleClass().remove(1);
            }
        });
        newBusBorder.setToggleGroup(this.clipBoard);
        // end of setup
        buses.put(bus.getId(), newBusBorder);
        lanes.add(newBusBorder, 0, row);
        this.nodeStartPosition = newBusBorder.getTranslateX();
    }

    private void initializeStop(StopObject element, double height, double width, int row) {
        element.setFitHeight(height);
        element.setFitWidth(width);
        Stop stop = element.getStop();
        ImageWrapper newStopBorder = new ImageWrapper((stop.getName()), element);
        newStopBorder.setContentDisplay(ContentDisplay.BOTTOM);
        newStopBorder.setOnMouseClicked(event -> {
            Stop newStop = ((StopObject)newStopBorder.getGraphic()).getStop();
            updateStopInfoPane(newStop);
        });

        // setup to make the ImageWrapper highlighted
        newStopBorder.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                newStopBorder.getStyleClass().add("highlighted");
            } else {
                newStopBorder.getStyleClass().remove(1);
            }
        });
        newStopBorder.setToggleGroup(this.clipBoard);
        // end of setup

        stops.put(row, newStopBorder);
        lanes.add(newStopBorder, 1, row);
    }

    /**
     * Clears all data from ui and sets data structures to initial state.
     */
    private void clearData() {
        this.rowNum = 0;
        this.animation.pause();
        this.lanes.getChildren().removeAll(buses.values());
        this.lanes.getChildren().removeAll(stops.values());
        this.buses = new HashMap<>();
        this.stops = new HashMap<>();
        this.clipBoard = new ToggleGroup();
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
        //checks to see if play is on to pause simulation
        if (play.isSelected() && this.simLoaded) {
            animation.pause();
            SimulationManager.togglePlay();
        }
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
            play.setSelected(false);

            this.interval = 2000;

            SimulationManager.initSim(this.file.getAbsolutePath(), this.interval, this.multiplier);
            Image busImage = new Image("busImg.png");
            Image stopImage = new Image("stopImg.png");
            routesMap = SimulationManager.getRoutes();

            // intializing bus and stop objexts to be place in the grid
            rowNum = 0;
            for (Bus bus : SimulationManager.getBuses().values()) {
                Stop stop = bus.getCurrentStop();
                StopObject stopObject = new StopObject(stop, stopImage, rowNum);
                initializeStop(stopObject, 30, 30, rowNum);
                BusObject busObject = new BusObject(bus, busImage, rowNum);
                intializeBus(busObject, 30, 30, rowNum);
                rowNum++;
            }
        }
    }

    @FXML
    public void playSim(ActionEvent e) {
        //TODO
        if (simLoaded) {
            SimulationManager.togglePlay();
            if (play.isSelected()) {
                animation.play();
            } else {
                animation.pause();
                this.interval = 2000;
            }
        }
    }

    @FXML
    public void addBusSim(ActionEvent e) throws IOException {
        if (simLoaded) {
            if (play.isSelected()) {
                animation.pause();
                SimulationManager.togglePlay();
            }
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
                intializeBus(newValue, 30, 30, ++rowNum);
                Image stopImage = new Image("stopImg.png");
                Stop stop = newBus.getCurrentStop();
                StopObject stopObject = new StopObject(stop, stopImage, rowNum);
                initializeStop(stopObject, 30, 30, rowNum);
                System.out.println(newBus.getId());


            });
            addBusStage.showAndWait(); // wait till addBusStage closes to resume execution
            if (play.isSelected()) {
                animation.play();
                SimulationManager.togglePlay();
            }
        }

    }

    @FXML
    public void stopSim(ActionEvent e) {
        if (simLoaded) {
            this.simLoaded = false;
            if (play.isSelected()) {
                animation.pause();
                SimulationManager.togglePlay();
                play.setSelected(false);
            }
            clearData();
        }
    }

    @FXML
    void playFaster(ActionEvent event) {
        if (simLoaded && play.isSelected()) {
            this.interval /= multiplier;
            SimulationManager.toggleFastForward();
        }
    }
}
