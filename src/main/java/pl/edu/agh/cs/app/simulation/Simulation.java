package pl.edu.agh.cs.app.simulation;

import javafx.concurrent.Task;
import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dEucl;
import pl.edu.agh.cs.app.simulation.maps.JungleMap;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IStarveObserver;
import pl.edu.agh.cs.app.simulation.utils.PlantSeeder;
import pl.edu.agh.cs.app.simulation.utils.SimulationStatus;
import pl.edu.agh.cs.app.ui.utils.SimulationConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Simulation implements IBreedObserver<Animal>, IStarveObserver<Animal> {
    protected final JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map;
    protected final SimulationStatus status;
    protected final HashSet<Animal> animals;
    protected final PlantSeeder seeder;
    protected final SimulationConfiguration config;

    public Simulation(SimulationConfiguration config) {
        this.config = config;
        map = new JungleMap<>(config.getWidth(), config.getHeight(), config.getJungleWidth(), config.getJungleHeight());
        status = new SimulationStatus();
        seeder = new PlantSeeder(map);
        animals = new HashSet<>();

        if (config.getAnimalCount() > config.getHeight() * config.getWidth()) {
            throw new IllegalArgumentException("Initial animals count cannot be bigger than map area");
        }

        for (int i = 0; i < config.getAnimalCount(); i++) {
            Animal animal = new Animal(map.getRandomEmptyCellPosition().get(),
                                       config.getStartEnergy(),
                                       config.getMoveEnergy(),
                                       Genotype.generateRandomGenotype(),
                                       map);
            animals.add(animal);
            map.place(animal);
            animal.addBreedObserver(this);
            animal.addStarveObserver(this);
        }
    }

    public JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> getMap() {
        return map;
    }

    public HashSet<Animal> getAnimals() {
        return animals;
    }

    public SimulationStatus getStatus() {
        return status;
    }

    public PlantSeeder getSeeder() {
        return seeder;
    }

    public void nextDay() {
        status.nextDay();

        HashSet<Animal> animalsCopy = (HashSet<Animal>) animals.clone();
        for (Animal animal : animalsCopy) {
            animal.rotate();
            animal.move();
        }

        map.feed();

        map.bringTogether();

        seeder.seedPlantJungle(config.getPlantEnergy());
        seeder.seedPlantNotJungle(config.getPlantEnergy());
    }

    @Override
    public void bred(Animal firstElement, Animal secondElement, Animal newElement, int firstEnergyBefore, int secondEnergyBefore, IVector2d position) {
        animals.add(newElement);
        newElement.addBreedObserver(this);
        newElement.addStarveObserver(this);
    }

    @Override
    public void starved(Animal starvedElement, IVector2d position) {
        animals.remove(starvedElement);
        starvedElement.removeBreedObserver(this);
        starvedElement.removeStarveObserver(this);
    }
}
