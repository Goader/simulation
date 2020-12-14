package pl.edu.agh.cs.app.ui.panes;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.simulation.utils.SimulationStatus;

public class SimulationViewPane extends BorderPane {
    protected Simulation simulation;
    protected SimulationStatus status;

    public SimulationViewPane(Simulation simulation) {
        super();
        this.simulation = simulation;
        status = simulation.getStatus();

        this.setBottom(new ControlPane(status));
        // change magic size numbers
        this.setCenter(new HBox(20, new StatisticsViewPane(), new MapViewPane(20, 20, simulation.getMap(), 24)));
        this.setPadding(new Insets(10, 10, 10, 10));
    }
}
