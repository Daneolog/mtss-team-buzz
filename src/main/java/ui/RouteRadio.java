package ui;

import corelogic.Route;
import javafx.scene.control.RadioButton;

public class RouteRadio extends RadioButton {
    private Route route;

    public RouteRadio(String name, Route route) {
        super(name);
        this.route = route;
    }

    public Route getRoute() {
        return route;
    }
}
