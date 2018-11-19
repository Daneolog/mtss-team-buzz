package ui;
import corelogic.Bus;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BusObject extends ImageView {
    private Bus bus;

    public BusObject(Bus bus, Image busImage) {
        super(busImage);
        this.bus = bus;
    }

    public Bus getBus() {
        return this.bus;
    }


    public void setBus(Bus bus) {
        this.bus = bus;
    }


}
