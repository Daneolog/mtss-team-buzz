package ui;
import corelogic.Stop;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StopObject extends ImageView {
    private Stop stop;

    public StopObject(Stop stop, Image StopImage) {
        super(StopImage);
        this.stop = stop;
    }

    public Stop getStop() {
        return this.stop;
    }


    public void setStop(Stop stop) {
        this.stop = stop;
    }

}
