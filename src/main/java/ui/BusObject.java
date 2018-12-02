package ui;
import corelogic.Bus;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BusObject extends ImageView {
    private Bus bus;
    private int laneNumber;

    public BusObject(Bus bus, Image busImage, int laneNumber) {
        super(busImage);
        this.bus = bus;
        this.laneNumber = laneNumber;
    }

    public Bus getBus() {
        return this.bus;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }


}
