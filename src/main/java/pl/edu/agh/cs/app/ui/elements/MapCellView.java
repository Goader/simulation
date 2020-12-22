package pl.edu.agh.cs.app.ui.elements;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.maps.JungleMap;
import pl.edu.agh.cs.app.ui.panes.SimulationViewPane;

import java.io.InputStream;
import java.util.Optional;

public class MapCellView extends StackPane {
    protected ImageView background;
    protected final int sideSize;
    protected final SimulationViewPane simulationView;
    protected final JungleMap<JungleMapCell<IJungleMapElement, Plant, Animal>, IJungleMapElement, Plant, Animal> map;
    protected final IVector2d position;


    public MapCellView(int sideSize, boolean isJungle, SimulationViewPane simulationView,
                       JungleMap<JungleMapCell<IJungleMapElement, Plant, Animal>, IJungleMapElement, Plant, Animal> map,
                       IVector2d position) {
        this.simulationView = simulationView;
        this.map = map;
        this.position = position;
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

        this.setOnMouseClicked(event -> {
            if (map.getCell(position).isPresent() && map.getCell(position).get().hasMovableElements()) {
                simulationView.showIndividualStatistics(map.getCell(position).get().getMaxEnergyElement());
            }
        });
    }

    public void updated(IVector2d position) {
        this.getChildren().clear();
        this.getChildren().add(background);

        Optional<JungleMapCell<IJungleMapElement, Plant, Animal>> optCell = map.getCell(position);

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
