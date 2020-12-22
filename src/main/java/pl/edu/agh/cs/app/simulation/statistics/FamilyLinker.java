package pl.edu.agh.cs.app.simulation.statistics;

import javafx.util.Pair;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IStarveObserver;
import pl.edu.agh.cs.app.ui.utils.SimulationStatus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FamilyLinker implements IBreedObserver<Animal>,
                                     IStarveObserver<Animal> {
    protected HashMap<Animal, OffspringCounter> offsprings;
    protected SimulationStatus status;

    public FamilyLinker (List<Animal> animals, SimulationStatus status) {
        offsprings = new HashMap<>();
        this.status = status;

        for (Animal animal : animals) {
            offsprings.put(animal, new OffspringCounter(animal, status));
            animal.addBreedObserver(this);
            animal.addStarveObserver(this);
        }
    }

    public LinkedList<Pair<Integer, Integer>> getDirectKids(Animal animal) {
        return offsprings.get(animal).getDirectKids();
    }

    public LinkedList<Pair<Integer, Integer>> getOffspring(Animal animal) {
        return offsprings.get(animal).getOffspring();
    }

    @Override
    public void bred(Animal firstElement, Animal secondElement, Animal newElement, int firstEnergyBefore, int secondEnergyBefore, IVector2d position) {
        offsprings.put(newElement, new OffspringCounter(newElement, status));
        newElement.addBreedObserver(this);
        newElement.addStarveObserver(this);
    }

    @Override
    public void starved(Animal starvedElement, IVector2d position) {
        offsprings.remove(starvedElement);
        starvedElement.removeBreedObserver(this);
        starvedElement.removeStarveObserver(this);
    }
}
