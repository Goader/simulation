package pl.edu.agh.cs.app.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.simulation.utils.SimulationConfiguration;
import pl.edu.agh.cs.app.ui.panes.SimulationViewPane;
import pl.edu.agh.cs.app.ui.utils.JSONSimParser;

import java.util.ArrayList;

public class View extends Application {

    private Stage window;
    private ArrayList<Simulation> simulations;
    private final int APPLICATION_PIXELS_HEIGHT = 1008;
    private final int APPLICATION_PIXELS_WIDTH = 1920;

    private final int SIMULATION_PIXELS_WIDTH = APPLICATION_PIXELS_WIDTH / 2;
    private final int SIMULATION_PIXELS_HEIGHT = APPLICATION_PIXELS_HEIGHT / 2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Simulation");

        simulations = new ArrayList<>();

        ArrayList<SimulationConfiguration> configs = JSONSimParser.parse();
        for (SimulationConfiguration config : configs) {
            simulations.add(new Simulation(config));
        }

        GridPane root = new GridPane();
        for (int i = 0; i < simulations.size(); i++) {
            Simulation simulation = simulations.get(i);
            SimulationViewPane pane = new SimulationViewPane(simulation, SIMULATION_PIXELS_WIDTH, SIMULATION_PIXELS_HEIGHT);
            Thread thread = new Thread(() -> {
                Runnable runnable = simulation::nextDay;
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        simulation.getStatus().setStopped(true);
                    }
                    if (simulation.getStatus().isRunning()) {
                        Platform.runLater(runnable);
                    }
                    if (simulation.getStatus().isStopped()) {
                        break;
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
            root.add(pane, i / 2, i % 2);
        }

        for (int i = 0; i < (simulations.size() + 1) / 2; i++) {
            ColumnConstraints col = new ColumnConstraints(APPLICATION_PIXELS_WIDTH / 2);
            root.getColumnConstraints().add(col);
        }

        int pixelHeight = simulations.size() == 1 ? SIMULATION_PIXELS_HEIGHT : SIMULATION_PIXELS_HEIGHT * 2;

        Scene scene = new Scene(root, ((simulations.size() + 1) / 2) * SIMULATION_PIXELS_WIDTH, pixelHeight);
        window.setScene(scene);
        window.show();
    }
}
