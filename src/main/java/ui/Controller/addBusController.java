package ui.Controller;

import corelogic.Bus;
import corelogic.Route;
import corelogic.SimulationManager;
import corelogic.Stop;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import ui.BusObject;
import ui.StopObject;

import java.net.URL;
import java.util.ResourceBundle;

public class addBusController implements Initializable {

    @FXML
    private TextField busId;

    @FXML
    private MenuButton routes;

    @FXML
    private MenuButton firstStop;

    @FXML
    private TextField passengerNum;

    @FXML
    private TextField fuelNum;

    @FXML
    private TextField speed;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    @FXML
    private Text error;

    private ReadOnlyObjectWrapper<BusObject> newBusObject;

    private SimController parentController;

    private Route selectedRoute;

    private Stop selectedStop;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.newBusObject = new ReadOnlyObjectWrapper<>();
        this.error.setVisible(false);
    }

    public ReadOnlyObjectProperty<BusObject> getBusProperty() {
        return newBusObject.getReadOnlyProperty();
    }

    /**
     * Sets the parent controller to get varriables and also initializes all values
     * @param parent
     */
    public void setParent(SimController parent) {
        this.parentController = parent;
        for (Route e: parentController.getRoutesMap().values()) {
            MenuItem routeDisplay = new MenuItem(e.toString());
            routeDisplay.setOnAction(event -> {
                selectedRoute = e;
                routes.setText("Route " + e.getId());
                //populating the stops of menubutton based upon the selected route.
                for (Stop stop: selectedRoute.getStops()) {
                    MenuItem stopDisplay = new MenuItem("Stop Id: " + stop.getId() + " " + stop.getName());
                    stopDisplay.setOnAction(event2 -> {
                        selectedStop = stop;
                        firstStop.setText(stop.getName());
                    });
                    firstStop.getItems().add(stopDisplay);
                }
            });
            routes.getItems().add(routeDisplay);
        }

    }

    @FXML
    public void cancel(ActionEvent e) {
        cancelButton.getScene().getWindow().hide();
    }

    @FXML
    public void okAccept(ActionEvent e) {

        try {
            int id = Integer.parseInt(busId.getText());
            int passengers = Integer.parseInt(passengerNum.getText());
            int speedVal = Integer.parseInt(speed.getText());
            int fuel = Integer.parseInt(fuelNum.getText());
            if (parentController.getBuses().containsKey(id)) {
                error.setText("- Bus id already exist. Try another.");
            } else if (passengers > 15 || fuel > 300 || speedVal > 45) {
                error.setText("- Input exceeded max value for passengers or fuel. try again.");
            } else if (selectedRoute == null) {
                error.setText("- Please select a route for the bus.");
            } else if (firstStop == null) {
                error.setText("- Please select a beginning stop for the bus.");
            } else {
                int counter = 0;
                for (Stop index: selectedRoute.getStops()) {
                    if (selectedStop.getId() == index.getId()) {
                        break;
                    }
                    counter++;
                }
                Bus newBus = new Bus(id, selectedRoute, counter, passengers, fuel, speedVal, SimulationManager.getSimTime());
                newBusObject.set(new BusObject(newBus, new Image("busImg.png")));
                okButton.getScene().getWindow().hide();
            }
            this.error.setVisible(true);

        } catch (NumberFormatException e2) {
            // erros when parse int cant get a number
            error.setText("- Please provide an integer value in the input text");
            this.error.setVisible(true);
        }
    }

}
