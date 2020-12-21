package pl.edu.agh.cs.app.simulation.statistics;

import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.ui.utils.SimulationStatus;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TotalStatistics implements ChangeListener<Number> {
    protected final Simulation simulation;
    protected final SimulationStatus status;
    protected final PopulationStatistics statistics;
    protected int day;

    protected int totalPlantsCount;
    protected int totalAnimalsCount;
    protected float totalAverageEnergy;
    protected float totalAverageLifetime;
    protected float totalAverageKidsCount;

    protected ArrayList<Float> totalAverageGenotype;

    public TotalStatistics(Simulation simulation, SimulationStatus status) {
        this.simulation = simulation;
        this.status = status;
        this.statistics = simulation.getStatistics();

        status.getDayProperty().addListener(this);

        totalPlantsCount = 0;
        totalAnimalsCount = 0;
        totalAverageEnergy = 0;
        totalAverageLifetime = 0;
        totalAverageKidsCount = 0;

        totalAverageGenotype = new ArrayList<>();
        for (int i = 0; i < Genotype.GENE_TYPES; i++) totalAverageGenotype.add((float) 0);
    }

    protected void update() {
        totalPlantsCount += statistics.getPlantsCount();
        totalAnimalsCount += statistics.getAnimalsCount();
        totalAverageEnergy += statistics.getAverageEnergy();
        totalAverageLifetime += statistics.getAverageLifetime();
        totalAverageKidsCount += statistics.getAverageKidsCount();

        ArrayList<Float> averageGenotype = statistics.getAverageGenotype();
        for (int i = 0; i < averageGenotype.size(); i++) {
            totalAverageGenotype.set(i, totalAverageGenotype.get(i) + averageGenotype.get(i));
        }
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (!newValue.equals(day)) {
            update();
            day = (int) newValue;
        }
    }

    public void writeTotalStatistics() {
        
    }
}
