package ui;
import corelogic.Stop;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StopObject extends ImageView {
    private Stop stop;
    private int laneNumber;

    public StopObject(Stop stop, Image StopImage, int laneNumber) {
        super(StopImage);
        this.stop = stop;
    }

    public Stop getStop() {
        return this.stop;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

}
