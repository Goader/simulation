package pl.edu.agh.cs.app.ui.panes;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IStarveObserver;
import pl.edu.agh.cs.app.ui.utils.SimulationStatus;

public class SimulationViewPane extends BorderPane implements IBreedObserver<Animal>, IStarveObserver<Animal> {
    protected Simulation simulation;
    protected SimulationStatus status;
    protected StatisticsViewPane statisticsView;
    protected MapViewPane mapView;

    public SimulationViewPane(Simulation simulation, int pxWidth, int pxHeight) {
        super();
        this.simulation = simulation;
        status = simulation.getStatus();

        ControlPane controls = new ControlPane(status);
        this.setBottom(controls);

        int padding = 10;
        int centerSpacing = 20;
        int controlsHeight = 40;

        controls.setMaxHeight(controlsHeight);

        int pxCenterHeight = (int) (pxHeight - controls.getHeight()) - 2 * padding - controlsHeight;
        int pxCenterWidth = pxWidth - 2 * padding;

        int maxMapHeightPx = pxCenterHeight;
        int maxMapWidthPx = (int) (0.7 * pxWidth);

        int mapWidth = simulation.getMap().getWidth();
        int mapHeight = simulation.getMap().getHeight();

        // configuring cellSide so that map fits the bounds being expanded to the max on the height or width
        int cellSide = maxMapHeightPx / mapHeight;
        if (cellSide * mapWidth > maxMapWidthPx) cellSide = maxMapWidthPx / mapWidth;

        this.mapView = new MapViewPane(mapWidth, mapHeight, simulation.getMap(), cellSide, this);
        this.statisticsView = new StatisticsViewPane(pxCenterWidth - cellSide * mapWidth - centerSpacing,
                pxCenterHeight,
                simulation.getStatistics());

        this.setCenter(new HBox(centerSpacing, statisticsView, mapView));
        this.setPadding(new Insets(padding, padding, padding, padding));

        simulation.getSeeder().addSeedObserver(mapView);

        for (Animal animal : simulation.getAnimals()) {
            animal.addViewObserver(mapView);
            animal.addBreedObserver(this);
        }
    }

    public void showIndividualStatistics(Animal animal) {
        statisticsView.drawIndividualStatistics(animal);
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
