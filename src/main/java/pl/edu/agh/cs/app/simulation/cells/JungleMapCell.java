package pl.edu.agh.cs.app.simulation.cells;

import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapNonMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IEatObserver;
import pl.edu.agh.cs.app.simulation.observers.IStarveObserver;

import java.util.*;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

public class JungleMapCell<IE extends IJungleMapElement,
        E extends AbstractJungleMapNonMovableElement, EM extends AbstractJungleMapMovableElement>
        extends MirrorMapCell<IE, E, EM>
        implements IEatObserver<EM, E>, IStarveObserver<EM>, IBreedObserver<EM> {
    protected TreeMap<Integer, HashSet<EM>> energyTree;

    public JungleMapCell(IVector2d initialPosition) {
        super(initialPosition);
        energyTree = new TreeMap<>();
    }

    // can be organised such way, that each time the same pair is returned, so there is no need for method in the map
    // but it can easily be done from the inside of Animal class
    public Optional<Entry<EM, EM>> getBreedPair() {
        if (movableElementsCount() < 2) {
            return Optional.empty();
        }
        HashSet<EM> maxEnergyElements = getMaxEnergyElements();
        LinkedList<EM> pair = new LinkedList<>();
        for (EM el : maxEnergyElements) {
            if (el.canBreed()) {
                pair.add(el);
            }
            if (pair.size() == 2) {
                return Optional.of(new SimpleEntry<>(pair.getFirst(), pair.getLast()));
            }
        }
        if (pair.isEmpty() || maxEnergyElements.size() != 1) {
            return Optional.empty();
        }
        for (EM el : energyTree.lowerEntry(energyTree.lastKey()).getValue()) {
            if (el.canBreed()) {
                pair.add(el);
            }
            if (pair.size() == 2) {
                return Optional.of(new SimpleEntry<>(pair.getFirst(), pair.getLast()));
            }
        }
        return Optional.empty();
    }

    public HashSet<EM> getMaxEnergyElements() {
        if (!hasMovableElements()) {
            throw new IllegalStateException("Tried to get movable element when there are no movable elements inside the cell");
        }
        return (HashSet<EM>) energyTree.lastEntry().getValue().clone();
    }

    public EM getMaxEnergyElement() {
        for (EM el : getMaxEnergyElements()) {
            return el;
        }
        throw new IllegalStateException("Tried to get movable element when there are no movable elements inside the cell");
    }

    @Override
    public boolean addElement(IE element) {
        if (!element.isMovable() && hasNonMovableElements()) {
            return false;
        }
        boolean set = super.addElement(element);
        if (set && element.isMovable()) {
            if (!energyTree.containsKey(element.getEnergy())) {
                energyTree.put(element.getEnergy(), new HashSet<>());
            }
            energyTree.get(element.getEnergy()).add((EM) element);
        }
        return set;
    }

    private void removeNodeIfNeeded(int energy) {
        if (energyTree.containsKey(energy) && energyTree.get(energy).isEmpty()) {
            energyTree.remove(energy);
        }
    }

    @Override
    public void removeElement(IE element) {
        super.removeElement(element);
        if (element.isMovable()) {
            energyTree.get(element.getEnergy()).remove(element);
            removeNodeIfNeeded(element.getEnergy());
        }
    }

    @Override
    public void bred(EM firstElement, EM secondElement, EM newElement,
                     int firstEnergyBefore, int secondEnergyBefore, IVector2d position) {
        energyTree.get(firstEnergyBefore).remove(firstElement);
        energyTree.get(secondEnergyBefore).remove(secondElement);
        removeNodeIfNeeded(firstEnergyBefore);
        removeNodeIfNeeded(secondEnergyBefore);
        addElement((IE) firstElement);
        addElement((IE) secondElement);
    }

    @Override
    public void ate(EM ateElement, E eatenElement, int eatenEnergy, IVector2d position) {
        int originalEnergy = ateElement.getEnergy() - eatenEnergy;
        energyTree.get(originalEnergy).remove(ateElement);
        removeNodeIfNeeded(originalEnergy);
        addElement((IE) ateElement);

        if (containsElement((IE) eatenElement)) {
            removeElement((IE) eatenElement);
        }
    }

    @Override
    public void starved(EM starvedElement, IVector2d position) {
        removeElement((IE) starvedElement);
    }
}
