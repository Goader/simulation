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

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Simulation implements IBreedObserver<Animal>, IStarveObserver<Animal> {
    protected final JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map;
    protected final SimulationStatus status;
    protected final HashSet<Animal> animals;
    protected final PlantSeeder seeder;

    public Simulation() {
        map = new JungleMap<>(20, 20, 8, 8);
        status = new SimulationStatus();
        seeder = new PlantSeeder(map);
        animals = new HashSet<>();

        List<Integer> genesCounter = Arrays.asList(4, 4, 4, 4, 4, 4, 4, 4);
        Genotype genotype = new Genotype(genesCounter);

        Vector2dBound vec1 = new Vector2dBound(2, 3, 20, 20);
        Vector2dBound vec2 = new Vector2dBound(5, 5, 20, 20);
        Vector2dBound vec3 = new Vector2dBound(9, 1, 20, 20);

        Animal animal11 = new Animal(vec1, 30, 3, genotype, map);
        Animal animal12 = new Animal(vec1, 30, 5, genotype, map);
        Animal animal13 = new Animal(vec1, 13, 1, genotype, map);
        Animal animal14 = new Animal(vec1, 4, 2, genotype, map);

        Animal animal21 = new Animal(vec2, 10, 4, genotype, map);
        Animal animal22 = new Animal(vec2, 15, 7, genotype, map);
        Animal animal23 = new Animal(vec2, 4, 3, genotype, map);

        Animal animal31 = new Animal(vec3, 10, 2, genotype, map);
        Animal animal32 = new Animal(vec3, 10, 1, genotype, map);
        Animal animal33 = new Animal(vec3, 4, 0, genotype, map);

        Animal b11 = new Animal(new Vector2dBound(2, 2, 20, 20), 100, 56, genotype, map);
        Animal b12 = new Animal(new Vector2dBound(2, 4, 20, 20), 100, 24, genotype, map);
        Animal b13 = new Animal(new Vector2dBound(1, 2, 20, 20), 100, 11, genotype, map);
        Animal b14 = new Animal(new Vector2dBound(1, 3, 20, 20), 100, 4, genotype, map);
        Animal b15 = new Animal(new Vector2dBound(1, 4, 20, 20), 100, 4, genotype, map);
        Animal b16 = new Animal(new Vector2dBound(3, 2, 20, 20), 100, 4, genotype, map);
        Animal b17 = new Animal(new Vector2dBound(3, 4, 20, 20), 100, 4, genotype, map);
        // (3, 2) le2

        Animal b21 = new Animal(new Vector2dBound(4, 4, 20, 20), 100, 4, genotype, map);
        Animal b22 = new Animal(new Vector2dBound(5, 4, 20, 20), 100, 4, genotype, map);
        Animal b23 = new Animal(new Vector2dBound(6, 4, 20, 20), 100, 4, genotype, map);
        Animal b24 = new Animal(new Vector2dBound(4, 5, 20, 20), 100, 4, genotype, map);
        Animal b25 = new Animal(new Vector2dBound(6, 5, 20, 20), 100, 4, genotype, map);
        Animal b26 = new Animal(new Vector2dBound(4, 6, 20, 20), 100, 4, genotype, map);
        Animal b27 = new Animal(new Vector2dBound(6, 6, 20, 20), 100, 4, genotype, map);
        // (5, 2) le2

        Animal b31 = new Animal(new Vector2dBound(8, 0, 20, 20), 100, 4, genotype, map);
        Animal b32 = new Animal(new Vector2dBound(9, 0, 20, 20), 100, 4, genotype, map);
        Animal b33 = new Animal(new Vector2dBound(0, 0, 20, 20), 100, 4, genotype, map);
        Animal b34 = new Animal(new Vector2dBound(8, 1, 20, 20), 100, 4, genotype, map);
        Animal b35 = new Animal(new Vector2dBound(0, 1, 20, 20), 100, 4, genotype, map);
        Animal b36 = new Animal(new Vector2dBound(8, 2, 20, 20), 100, 4, genotype, map);
        Animal b37 = new Animal(new Vector2dBound(9, 2, 20, 20), 100, 4, genotype, map);

        LinkedList<Animal> activeAnimals = new LinkedList<>(Arrays.asList(animal11, animal12, animal13, animal14,
                animal21, animal22, animal23, animal31, animal32, animal33));
        LinkedList<Animal> boundsAnimals = new LinkedList<>(Arrays.asList(b11, b12, b13, b14, b15, b16, b17,
                b21, b22, b23, b24, b25, b26, b27, b31, b32, b33, b34, b35, b36, b37));

        for (Animal anim : activeAnimals) map.place(anim);
        for (Animal anim: boundsAnimals) map.place(anim);

        animals.addAll(activeAnimals);
        animals.addAll(boundsAnimals);

        for (Animal animal : animals) {
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

        // change magic energy
        seeder.seedPlantJungle(20);
        seeder.seedPlantNotJungle(20);
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
