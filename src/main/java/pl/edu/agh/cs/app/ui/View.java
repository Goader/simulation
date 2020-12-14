package pl.edu.agh.cs.app.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.ui.panes.SimulationViewPane;

public class View extends Application {

    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Simulation");

        Simulation simulation1 = new Simulation();
        SimulationViewPane pane = new SimulationViewPane(simulation1);
        Thread t1 = new Thread(simulation1);
        t1.start();
        //Platform.runLater(simulation1);

        Scene scene = new Scene(pane, 960, 540);
        window.setScene(scene);
        window.show();
    }
}
