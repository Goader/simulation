package pl.edu.agh.cs.app.ui.elements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;

public class MapCellView extends StackPane {
    protected ImageView background;


    public MapCellView(int width, int height, boolean isJungle) {
        if (width != height) {
            throw new IllegalArgumentException("Width and height must be the same, because MapCellView is a square");
        }
        InputStream back = InputStream.nullInputStream();
        try {
            if (isJungle) {
                back = new FileInputStream("C:\\FILES_IN_USE\\java\\simulation\\src\\main\\resources\\jungle.jpg");
            } else {
                back = new FileInputStream("C:\\FILES_IN_USE\\java\\simulation\\src\\main\\resources\\savanna.jpg");
            }
        } catch (FileNotFoundException ex) {
            System.exit(-54);  // couldn't find a better way to handle it
            // of course, we could have used some default textures from JavaFX, but it's not beautiful :)
        }
        
        Image im = new Image(back, width, height, true, true);
        background = new ImageView(im);

        this.getChildren().add(background);
    }

    public void updated(JungleMapCell<IJungleMapElement, Plant, Animal> cell) {
        this.getChildren().clear();
        this.getChildren().add(background);

        if (cell.hasNonMovableElements()) {
            Plant plant = cell.getNonMovableElements().getFirst();
            this.getChildren().add(plant.getView());
        }
        if (cell.hasMovableElements()) {
            Animal animal = cell.getMaxEnergyElement();
            this.getChildren().add(animal.getView());
        }
    }
}
