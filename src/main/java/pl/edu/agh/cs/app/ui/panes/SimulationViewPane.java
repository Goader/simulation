package pl.edu.agh.cs.app.ui.panes;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.ui.utils.SimulationStatus;

public class SimulationViewPane extends BorderPane {
    protected Simulation simulation;
    protected SimulationStatus status;

    public SimulationViewPane(Simulation simulation) {
        super();
        this.simulation = simulation;
        status = new SimulationStatus();

        this.setBottom(new ControlPane(status));
        this.setCenter(new HBox(20, new StatisticsViewPane(), new MapViewPane()));
        this.setPadding(new Insets(10, 10, 10, 10));
    }
}
