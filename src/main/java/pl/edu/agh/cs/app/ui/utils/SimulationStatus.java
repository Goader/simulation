package pl.edu.agh.cs.app.ui.utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SimulationStatus {
    protected boolean running;
    protected boolean stopped;
    protected IntegerProperty day;
    protected boolean showDominantGene;



    protected IntegerProperty tickTime;

    public SimulationStatus() {
        running = false;
        stopped = false;
        day = new SimpleIntegerProperty(this, "day", 0);
        tickTime = new SimpleIntegerProperty(100);
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isStopped() {
        return stopped;
    }

    public boolean isShowDominantGene() {
        return showDominantGene;
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

    public void setShowDominantGene(boolean showDominantGene) {
        this.showDominantGene = showDominantGene;
    }

    public void nextDay() {
        day.set(day.get() + 1);
    }

    public int getTickTime() {
        return tickTime.get();
    }

    public IntegerProperty tickTimeProperty() {
        return tickTime;
    }
}
