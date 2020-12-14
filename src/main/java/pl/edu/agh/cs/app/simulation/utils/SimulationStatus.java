package pl.edu.agh.cs.app.simulation.utils;

import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class SimulationStatus {
    protected boolean running;
    protected boolean stopped;
    protected IntegerProperty day;

    public SimulationStatus() {
        running = false;
        stopped = false;
        day = new SimpleIntegerProperty(this, "day", 0);
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isStopped() {
        return stopped;
    }

    public IntegerProperty getDayProperty() {
        return day;
    }

    public int getDay() {
        return day.get();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void nextDay() {
        day.set(day.get() + 1);
    }
}
