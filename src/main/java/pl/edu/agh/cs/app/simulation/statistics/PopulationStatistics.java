package pl.edu.agh.cs.app.simulation.statistics;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.*;
import pl.edu.agh.cs.app.ui.utils.SimulationStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PopulationStatistics implements IEnergyChangeObserver<Animal>,
                                             IEatObserver<Animal, Plant>,
                                             IBreedObserver<Animal>,
                                             IStarveObserver<Animal>,
                                             ISeedObserver<Plant> {
    protected Simulation simulation;
    protected SimulationStatus status;

    protected HashSet<Plant> plants;
    protected HashMap<Animal, Integer> birthday;
    protected HashMap<Animal, Integer> kidsCount;
    protected int starvedAnimals;

    protected IntegerProperty plantsCount;
    protected IntegerProperty animalsCount;
    protected FloatProperty averageEnergy;
    protected FloatProperty averageLifetime;
    protected FloatProperty averageKidsCount;

    protected ArrayList<FloatProperty> averageGenotype;

    public PopulationStatistics(Simulation simulation, SimulationStatus status) {
        this.simulation = simulation;
        this.status = status;

        plants = new HashSet<>();
        birthday = new HashMap<>();
        kidsCount = new HashMap<>();
        starvedAnimals = 0;

        plantsCount = new SimpleIntegerProperty();
        animalsCount = new SimpleIntegerProperty();
        averageEnergy = new SimpleFloatProperty();
        averageLifetime = new SimpleFloatProperty();
        averageKidsCount = new SimpleFloatProperty();

        animalsCount.set(simulation.getConfig().getAnimalCount());
        averageEnergy.set(simulation.getConfig().getStartEnergy());

        averageGenotype = new ArrayList<>();
    }

    protected void updateAverageGenotype(Genotype genotype, int countBefore, int countAfter) {
        if (countAfter == 0) {
            for (FloatProperty gene : averageGenotype) gene.set(0);
            return;
        }
        ArrayList<Integer> newGenesCounter = genotype.getGenesCounter();
        int sign = 1;  // new element, so we are adding genes
        if (countAfter < countBefore) sign = -1;  // element starved, subtracting genes

        for (int i = 0; i < Genotype.GENE_TYPES; i++) {
            float totalCount = averageGenotype.get(i).get() * countBefore;
            totalCount += (sign * newGenesCounter.get(i));
            averageGenotype.get(i).set(totalCount / countAfter);
        }
    }

    public void initialiseAnimals(List<Animal> animals) {
        // initialising genotypes
        ArrayList<Integer> genesCount = new ArrayList<>();
        for (int i = 0; i < Genotype.GENE_TYPES; i++) {
            genesCount.add(0);
            averageGenotype.add(new SimpleFloatProperty(0));
        }
        for (Animal animal : animals) {
            ArrayList<Integer> genes = animal.getGenotype().getGenesCounter();
            for (int i = 0; i < Genotype.GENE_TYPES; i++) {
                genesCount.set(i, genesCount.get(i) + genes.get(i));
            }
        }
        for (int i = 0; i < Genotype.GENE_TYPES; i++) {
            averageGenotype.get(i).set((float) genesCount.get(i) / animalsCount.get());
        }

        // initialising birthdays
        for (Animal animal : animals) {
            birthday.put(animal, 0);
            kidsCount.put(animal, 0);
        }
    }

    @Override
    public void bred(Animal firstElement, Animal secondElement, Animal newElement, int firstEnergyBefore, int secondEnergyBefore, IVector2d position) {
        newElement.addStarveObserver(this);
        newElement.addBreedObserver(this);
        newElement.addEatObserver(this);
        newElement.addEnergyChangeObserver(this);

        // updating energy
        float totalEnergy = averageEnergy.get() * animalsCount.get();
        totalEnergy += newElement.getEnergy();
        totalEnergy += (firstElement.getEnergy() - firstEnergyBefore);
        totalEnergy += (secondElement.getEnergy() - secondEnergyBefore);

        animalsCount.set(animalsCount.get() + 1);

        averageEnergy.set(totalEnergy / animalsCount.get());

        // updating genotype
        updateAverageGenotype(newElement.getGenotype(), animalsCount.get() - 1, animalsCount.get());

        // preparing lifetime
        birthday.put(newElement, status.getDay());

        // updating kids count
        kidsCount.put(newElement, 0);
        kidsCount.put(firstElement, kidsCount.get(firstElement) + 1);
        kidsCount.put(secondElement, kidsCount.get(secondElement) + 1);

        float totalKidsCount = averageKidsCount.get() * (animalsCount.get() - 1);
        totalKidsCount += 2;
        averageKidsCount.set(totalKidsCount / animalsCount.get());
    }

    @Override
    public void ate(Animal ateElement, Plant eatenElement, int eatenEnergy, IVector2d position) {
        plants.remove(eatenElement);
        plantsCount.set(plants.size());

        float totalEnergy = averageEnergy.get() * animalsCount.get();
        totalEnergy += eatenEnergy;
        averageEnergy.set(totalEnergy / animalsCount.get());
    }

    @Override
    public void energyChanged(Animal element, int energyBefore, int energyAfter) {
        float totalEnergy = averageEnergy.get() * animalsCount.get();
        totalEnergy += (energyAfter - energyBefore);
        averageEnergy.set(totalEnergy / animalsCount.get());
    }

    @Override
    public void sowed(Plant sownElement, IVector2d position) {
        plants.add(sownElement);
        plantsCount.set(plants.size());
    }

    @Override
    public void starved(Animal starvedElement, IVector2d position) {
        starvedElement.removeStarveObserver(this);
        starvedElement.removeBreedObserver(this);
        starvedElement.removeEatObserver(this);
        starvedElement.removeEnergyChangeObserver(this);

        // updating energy
        float totalEnergy = averageEnergy.get() * animalsCount.get();
        totalEnergy -= starvedElement.getEnergy();
        animalsCount.set(animalsCount.get() - 1);
        averageEnergy.set(totalEnergy / animalsCount.get());

        // updating genotype
        updateAverageGenotype(starvedElement.getGenotype(), animalsCount.get() + 1, animalsCount.get());

        // updating lifetime
        int lifetime = status.getDay() - birthday.get(starvedElement);
        float totalLifetime = averageLifetime.get() * starvedAnimals;
        totalLifetime += lifetime;
        starvedAnimals += 1;
        averageLifetime.set(totalLifetime / starvedAnimals);

        // updating children count
        float totalKidsCount = averageKidsCount.get() * (animalsCount.get() + 1);
        totalKidsCount -= kidsCount.get(starvedElement);
        kidsCount.remove(starvedElement);
        averageKidsCount.set(totalKidsCount / animalsCount.get());
    }

    public int getPlantsCount() {
        return plantsCount.get();
    }

    public IntegerProperty plantsCountProperty() {
        return plantsCount;
    }

    public int getAnimalsCount() {
        return animalsCount.get();
    }

    public IntegerProperty animalsCountProperty() {
        return animalsCount;
    }

    public float getAverageEnergy() {
        return averageEnergy.get();
    }

    public FloatProperty averageEnergyProperty() {
        return averageEnergy;
    }

    public float getAverageLifetime() {
        return averageLifetime.get();
    }

    public FloatProperty averageLifetimeProperty() {
        return averageLifetime;
    }

    public float getAverageKidsCount() {
        return averageKidsCount.get();
    }

    public FloatProperty averageKidsCountProperty() {
        return averageKidsCount;
    }

    public ArrayList<FloatProperty> averageGenotypeProperties() {
        return averageGenotype;
    }

    public ArrayList<Float> getAverageGenotype() {
        ArrayList<Float> genotype = new ArrayList<>();
        for (FloatProperty prop : averageGenotype) {
            genotype.add(prop.get());
        }
        return genotype;
    }
}
