package pl.edu.agh.cs.app.ui.panes;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IBreedPublisher;
import pl.edu.agh.cs.app.simulation.observers.IStarveObserver;
import pl.edu.agh.cs.app.simulation.utils.SimulationStatus;
import pl.edu.agh.cs.app.ui.elements.MapCellView;

import java.util.HashSet;

public class SimulationViewPane extends BorderPane implements IBreedObserver<Animal>, IStarveObserver<Animal> {
    protected Simulation simulation;
    protected SimulationStatus status;
    protected StatisticsViewPane statisticsView;
    protected MapViewPane mapView;

    public SimulationViewPane(Simulation simulation) {
        super();
        this.simulation = simulation;
        status = simulation.getStatus();

        this.setBottom(new ControlPane(status));
        // change magic size numbers
        this.mapView = new MapViewPane(20, 20, simulation.getMap(), 48);
        this.statisticsView = new StatisticsViewPane();

        this.setCenter(new HBox(20, statisticsView, mapView));
        this.setPadding(new Insets(10, 10, 10, 10));

        simulation.getSeeder().addSeedObserver(mapView);

        for (Animal animal : simulation.getAnimals()) {
            animal.addViewObserver(mapView);
            animal.addBreedObserver(this);
        }
    }

    @Override
    public void bred(Animal firstElement, Animal secondElement, Animal newElement, int firstEnergyBefore, int secondEnergyBefore, IVector2d position) {
        newElement.addViewObserver(mapView);
        newElement.addBreedObserver(this);
    }

    @Override
    public void starved(Animal starvedElement, IVector2d position) {
        starvedElement.removeViewObserver(mapView);
        starvedElement.removeBreedObserver(this);
    }
}
