package pl.edu.agh.cs.app.ui.elements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class PlantView extends ImageView {
    protected InputStream plant;
    protected Image plantIcon;
    protected int imageSideSize = 20;

    public PlantView() {
        super();
        plant = PlantView.class.getResourceAsStream("/plant.jpg");

        // needs change, we wanna know width and height from arguments
        plantIcon = new Image(plant, imageSideSize, imageSideSize, true, true);
        this.setImage(plantIcon);
    }

    public int getImageSideSize() {
        return imageSideSize;
    }

    public void setImageSideSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be a positive integer");
        }
        imageSideSize = size;

        plant = PlantView.class.getResourceAsStream("/plant.jpg");

        plantIcon = new Image(plant, size, size, true, true);

        this.setImage(plantIcon);
    }
}
