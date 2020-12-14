package pl.edu.agh.cs.app.ui.panes;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatisticsViewPane extends VBox {

    public StatisticsViewPane() {
        this.setMaxSize(480, 480);
        this.getChildren().add(new Label("Statistics will be here"));
    }
}
