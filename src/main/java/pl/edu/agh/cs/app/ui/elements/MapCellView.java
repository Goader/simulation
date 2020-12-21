package pl.edu.agh.cs.app.ui.elements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;

import java.io.InputStream;
import java.util.Optional;

public class MapCellView extends StackPane {
    protected ImageView background;
    protected final int sideSize;


    public MapCellView(int sideSize, boolean isJungle) {
        this.sideSize = sideSize;
        InputStream back;
        if (isJungle) {
            back = MapCellView.class.getResourceAsStream("/jungle.jpg");
        } else {
            back = MapCellView.class.getResourceAsStream("/savanna.jpg");
        }

        Image im = new Image(back, sideSize, sideSize, true, true);
        background = new ImageView(im);

        this.getChildren().add(background);
    }

    public void updated(Optional<JungleMapCell<IJungleMapElement, Plant, Animal>> optCell) {
        this.getChildren().clear();
        this.getChildren().add(background);

        if (optCell.isPresent()) {
            JungleMapCell<IJungleMapElement, Plant, Animal> cell = optCell.get();
            if (cell.hasNonMovableElements()) {
                Plant plant = cell.getNonMovableElements().getFirst();
                if (plant.getView().getImageSideSize() != sideSize) {
                    plant.getView().setImageSideSize(sideSize);
                }
                this.getChildren().add(plant.getView());
            }
            if (cell.hasMovableElements()) {
                Animal animal = cell.getMaxEnergyElement();
                if (animal.getView().getImageSideSize() != sideSize) {
                    animal.getView().setImageSideSize(sideSize);
                }
                animal.getView().update();
                this.getChildren().add(animal.getView());
            }
        }
    }
}
