package pl.edu.agh.cs.app.simulation.statistics;

import javafx.util.Pair;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IStarveObserver;
import pl.edu.agh.cs.app.ui.utils.SimulationStatus;

import java.util.HashSet;
import java.util.LinkedList;

public class OffspringCounter implements IBreedObserver<Animal>,
                                         IStarveObserver<Animal> {
    protected Animal animal;
    protected SimulationStatus status;

    protected LinkedList<Pair<Integer, Integer>> directKids;
    protected LinkedList<Pair<Integer, Integer>> offspring;

    protected HashSet<Animal> offspringSet;

    public OffspringCounter(Animal animal, SimulationStatus status) {
        this.animal = animal;
        this.status = status;

        directKids = new LinkedList<>();
        offspring = new LinkedList<>();
        offspringSet = new HashSet<>();

        directKids.add(new Pair<>(status.getDay(), 0));
        offspring.add(new Pair<>(status.getDay(), 0));

        animal.addBreedObserver(this);
        animal.addStarveObserver(this);
    }

    public LinkedList<Pair<Integer, Integer>> getDirectKids() {
        return (LinkedList<Pair<Integer, Integer>>) directKids.clone();
    }

    public LinkedList<Pair<Integer, Integer>> getOffspring() {
        return (LinkedList<Pair<Integer, Integer>>) offspring.clone();
    }

    private void updateDayCountCollection(LinkedList<Pair<Integer, Integer>> coll) {
        if (coll.getLast().getKey() == status.getDay()) {  // increasing count for the same day
            Pair<Integer, Integer> last = coll.pollLast();
            coll.add(new Pair<>(status.getDay(), last.getValue() + 1));
        } else {  // adding new day with increased count
            coll.add(new Pair<>(status.getDay(), coll.getLast().getValue() + 1));
        }
    }

    @Override
    public void bred(Animal firstElement, Animal secondElement, Animal newElement, int firstEnergyBefore, int secondEnergyBefore, IVector2d position) {
        if (firstElement == animal || secondElement == animal) {
            updateDayCountCollection(directKids);
        }
        updateDayCountCollection(offspring);

        offspringSet.add(newElement);

        newElement.addBreedObserver(this);
        newElement.addStarveObserver(this);
    }

    @Override
    public void starved(Animal starvedElement, IVector2d position) {
        if (starvedElement == animal) {
            for (Animal successor : offspringSet) {
                successor.removeStarveObserver(this);
                successor.removeBreedObserver(this);
            }
        } else {
            offspringSet.remove(starvedElement);
        }
        starvedElement.removeBreedObserver(this);
        starvedElement.removeStarveObserver(this);
    }
}
