package pl.edu.agh.cs.app.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.ui.panes.SimulationViewPane;
import pl.edu.agh.cs.app.ui.utils.JSONSimParser;
import pl.edu.agh.cs.app.ui.utils.SimulationConfiguration;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class View extends Application {

    private Stage window;
    private ArrayList<Simulation> simulations;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Simulation");

        simulations = new ArrayList<>();
        System.out.println("here");
        ArrayList<SimulationConfiguration> configs = JSONSimParser.parse();
        System.out.println(configs.size());
        for (SimulationConfiguration config : configs) {
            simulations.add(new Simulation(config));
        }

        VBox root = new VBox();
        for (Simulation simulation : simulations) {
            SimulationViewPane pane = new SimulationViewPane(simulation);
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
            root.getChildren().add(pane);
        }


        Scene scene = new Scene(root, 960, 1080);
        window.setScene(scene);
        window.show();
    }
}
