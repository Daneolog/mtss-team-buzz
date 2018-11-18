package ui;
import corelogic.Stop;
import javafx.scene.image.Image;

public class StopObject {
    private Stop stop;
    private Image stopImage;

    public StopObject(Stop stop, Image StopImage) {
        this.stop = stop;
        this.stopImage = stopImage;
    }

    public Stop getStop() {
        return this.stop;
    }

    public Image getStopImage() {
        return this.stopImage;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    /**
     * This sets a new image to stop which can be used to highlight a stop or change color to note traffic.
     * @param stopImage an image of a stop
     */
    public void setStopImage(Image stopImage) {
        this.stopImage = stopImage;
    }


}
