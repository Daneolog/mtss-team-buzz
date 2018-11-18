package ui;
import corelogic.Bus;
import javafx.scene.image.Image;

public class BusObject {
    private Bus bus;
    private Image busImage;

    public BusObject(Bus bus, Image busImage) {
        this.bus = bus;
        this.busImage = busImage;
    }

    public Bus getBus() {
        return this.bus;
    }

    public Image getBusImage() {
        return this.busImage;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    /**
     * This sets a new image to bus which can be used to highlight a bus
     * @param busImage an image of a bus
     */
    public void setBusImage(Image busImage) {
        this.busImage = busImage;
    }


}
