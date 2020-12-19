package pl.edu.agh.cs.app.ui.elements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PlantView extends ImageView {
    protected final Image plantIcon;

    public PlantView() {
        super();
        InputStream pl = InputStream.nullInputStream();
        try {
            pl = new FileInputStream("C:\\FILES_IN_USE\\java\\simulation\\src\\main\\resources\\plant.jpg");
        } catch (FileNotFoundException ex) {
            System.exit(-54);  // couldn't find a better way to handle it
            // of course, we could have used some default textures from JavaFX, but it's not beautiful :)
        }
        // needs change, we wanna know width and height from arguments
        plantIcon = new Image(pl, 23, 23, true, true);
        this.setImage(plantIcon);
    }
}
