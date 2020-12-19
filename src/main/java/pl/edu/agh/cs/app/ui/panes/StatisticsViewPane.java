package pl.edu.agh.cs.app.ui.panes;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatisticsViewPane extends VBox {

    public StatisticsViewPane(int width, int height) {
        this.setMaxSize(width, height);
        this.getChildren().add(new Label("Statistics will be here"));
        //this.setWidth(width);
    }
}
