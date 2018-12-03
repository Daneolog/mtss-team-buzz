package ui;

import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;

public class ImageWrapper extends Label implements Toggle {
    private ObjectProperty<ToggleGroup> toggleGroup = new SimpleObjectProperty<>();

    public ImageWrapper(String text, ImageView center) {
        super(text, center);
    }


    @Override
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public boolean isSelected() {
        return selected.get();
    }

    @Override
    public BooleanProperty selectedProperty() {
        return selected;
    }

    private BooleanProperty selected = new SimpleBooleanProperty(false);

    @Override
    public ToggleGroup getToggleGroup() {
        return toggleGroup.get();
    }

    @Override
    public void setToggleGroup(ToggleGroup toggleGroup) {
        this.toggleGroup.set(toggleGroup);
    }

    @Override
    public ObjectProperty<ToggleGroup> toggleGroupProperty() {
        return toggleGroup;
    }
}
