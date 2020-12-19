package pl.edu.agh.cs.app.ui.elements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PlantView extends ImageView {
    protected InputStream plant = InputStream.nullInputStream();
    protected Image plantIcon;
    protected int imageSideSize = 20;

    public PlantView() {
        super();
        try {
            plant = new FileInputStream("C:\\FILES_IN_USE\\java\\simulation\\src\\main\\resources\\plant.jpg");
        } catch (FileNotFoundException ex) {
            System.exit(-54);  // couldn't find a better way to handle it
            // of course, we could have used some default textures from JavaFX, but it's not beautiful :)
        }
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

        try {
            plant = new FileInputStream("C:\\FILES_IN_USE\\java\\simulation\\src\\main\\resources\\plant.jpg");
        } catch (FileNotFoundException ex) {
            System.exit(-54);  // couldn't find a better way to handle it
            // of course, we could have used some default textures from JavaFX, but it's not beautiful :)
        }

        plantIcon = new Image(plant, size, size, true, true);

        this.setImage(plantIcon);
    }
}
