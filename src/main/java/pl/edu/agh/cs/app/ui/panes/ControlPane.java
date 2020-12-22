package pl.edu.agh.cs.app.ui.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.ui.utils.SimulationStatus;

public class ControlPane extends HBox {
    protected SimulationStatus status;
    protected Button startBtn;
    protected Button pauseBtn;
    protected Button stopBtn;
    protected Button writeBtn;
    protected CheckBox geneCheck;
    protected Slider slider;
    protected HBox daysLabelsBox;
    protected Label firstLabel;
    protected Label secondLabel;

    public ControlPane(Simulation simulation) {
        super(30);

        this.status = simulation.getStatus();
        startBtn = new Button("Start");
        startBtn.setOnAction(e -> startOnClick());
        pauseBtn = new Button("Pause");
        pauseBtn.setOnAction(e -> pauseOnClick());
        stopBtn = new Button("Stop");
        stopBtn.setOnAction(e -> stopOnClick());
        writeBtn = new Button("Write JSON");
        writeBtn.setOnAction(e -> simulation.getTotalStatistics().writeTotalStatistics());
        geneCheck = new CheckBox("Show animals with dominant gene");
        geneCheck.setOnAction(e -> geneOnClick());
        geneCheck.setSelected(false);
        Slider slider = new Slider();
        slider.setMin(20);
        slider.setMax(1000);
        slider.setValue(100);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(20);
        slider.setBlockIncrement(10);
        slider.setMinWidth(200);
        slider.valueProperty().bindBidirectional(status.tickTimeProperty());

        firstLabel = new Label("The days passed: ");
        secondLabel = new Label();
        secondLabel.textProperty().bind(status.getDayProperty().asString());
        daysLabelsBox = new HBox(0, firstLabel, secondLabel);

        this.getChildren().addAll(startBtn, pauseBtn, stopBtn, writeBtn, daysLabelsBox, geneCheck, slider);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10, 10, 10, 10));
    }

    protected void startOnClick() {
        if (!status.isStopped()) {
            status.setRunning(true);
        }
    }

    protected void pauseOnClick() {
        if (!status.isStopped()) {
            status.setRunning(false);
        }
    }

    protected void stopOnClick() {
        status.setRunning(false);
        status.setStopped(true);
    }

    protected void geneOnClick() {
        status.setShowDominantGene(geneCheck.isSelected());
    }
}
