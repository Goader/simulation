package pl.edu.agh.cs.app.simulation;

import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.maps.JungleMap;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IStarveObserver;
import pl.edu.agh.cs.app.simulation.utils.PlantSeeder;
import pl.edu.agh.cs.app.simulation.utils.SimulationStatus;

import java.util.HashSet;

public class Simulation implements IBreedObserver<Animal>, IStarveObserver<Animal>, Runnable {
    protected final JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map;
    protected final SimulationStatus status;
    protected final HashSet<Animal> animals;
    protected final PlantSeeder seeder;

    public Simulation() {
        map = new JungleMap<>(20, 20, 8, 8);
        status = new SimulationStatus();
        seeder = new PlantSeeder(map);
        animals = new HashSet<>();
    }

    public JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> getMap() {
        return map;
    }

    public SimulationStatus getStatus() {
        return status;
    }

    public void nextDay() {
        status.nextDay();

//        for (Animal animal : animals) {
//            animal.rotate();
//            animal.move();
//        }
//
//        map.feed();
//
//        map.bringTogether();

        seeder.seedPlantJungle(10);
        seeder.seedPlantNotJungle(10);
    }

    @Override
    public void bred(Animal firstElement, Animal secondElement, Animal newElement, int firstEnergyBefore, int secondEnergyBefore, IVector2d position) {
        animals.add(newElement);
    }

    @Override
    public void starved(Animal starvedElement, IVector2d position) {
        animals.remove(starvedElement);
    }

    @Override
    public void run() {
        while (!status.isStopped()) {
            while (!status.isRunning()) {
                nextDay();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    status.setStopped(true);
                }
            }
        }
    }
}
