package pl.edu.agh.cs.app.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.ui.panes.SimulationViewPane;

public class View extends Application {

    private Stage window;
    private Simulation simulation1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Simulation");

        simulation1 = new Simulation();
        SimulationViewPane pane = new SimulationViewPane(simulation1);
        Thread thread = new Thread(() -> {
            Runnable runnable = this::updateSimulations;
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    simulation1.getStatus().setStopped(true);
                }
                if (simulation1.getStatus().isRunning()) {
                    Platform.runLater(runnable);
                }
                // how to make it work?
                if (simulation1.getStatus().isStopped()) {
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();


        Scene scene = new Scene(pane, 1920, 1080);
        window.setScene(scene);
        window.show();
    }

    protected void updateSimulations() {
        simulation1.nextDay();
    }
}
