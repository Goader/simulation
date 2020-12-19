package pl.edu.agh.cs.app.ui.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import pl.edu.agh.cs.app.ui.utils.SimulationStatus;

public class ControlPane extends HBox {
    protected SimulationStatus status;
    protected Button startBtn;
    protected Button pauseBtn;
    protected Button stopBtn;
    protected HBox daysLabelsBox;
    protected Label firstLabel;
    protected Label secondLabel;

    public ControlPane(SimulationStatus status) {
        super(30);

        this.status = status;
        startBtn = new Button("Start");
        startBtn.setOnAction(e -> startOnClick());
        pauseBtn = new Button("Pause");
        pauseBtn.setOnAction(e -> pauseOnClick());
        stopBtn = new Button("Stop");
        stopBtn.setOnAction(e -> stopOnClick());

        firstLabel = new Label("The days passed: ");
        secondLabel = new Label();
        secondLabel.textProperty().bind(status.getDayProperty().asString());
        daysLabelsBox = new HBox(0, firstLabel, secondLabel);

        this.getChildren().addAll(startBtn, pauseBtn, stopBtn, daysLabelsBox);
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
}
