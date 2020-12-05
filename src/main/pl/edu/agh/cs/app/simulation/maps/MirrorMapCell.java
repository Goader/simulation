package pl.edu.agh.cs.app.simulation.maps;

import pl.edu.agh.cs.app.simulation.entities.Animal;
import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.entities.Plant;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

import java.util.*;

public class MirrorMapCell implements IMapCell {
    // maybe there can be better way to store it
    private Optional<IMapElement> plant;
    private TreeSet<IMapMovableElement> movableElements;

    private IVector2d position;
    private boolean normalized;

    private HashSet<IMapMovableElement> maxEnergyElements;
    private int maxEnergy;

    private Optional<IMapMovableElement> secondMaxEnergyElement;
    private int secondMaxEnergy;

    // eventually the same 3rd as with 2nd

    public MirrorMapCell(IVector2d initialPosition) {
        position = initialPosition;
        movableElements = new TreeSet<>();
        plant = Optional.empty();

        normalized = true;
        maxEnergyElements = new HashSet<>();
        secondMaxEnergyElement = Optional.empty();

        maxEnergy = secondMaxEnergy = 0;
    }

    @Override
    public int nonMovableElementsCount() {
        return plant.isPresent() ? 1 : 0;
    }

    @Override
    public int movableElementsCount() {
        return movableElements.size();
    }

    @Override
    public boolean hasNonMovableElements() {
        return plant.isPresent();
    }

    @Override
    public boolean hasMovableElements() {
        return !movableElements.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return !hasNonMovableElements() && !hasMovableElements();
    }

    @Override
    public boolean canMoveTo(IMapElement element) {
        return !element.isMovable() && hasNonMovableElements();
    }

    @Override
    public List<IMapElement> getNonMovableElements() {
        if (plant.isPresent()) {
            return new LinkedList<>(Arrays.asList(plant.get()));
        }
        return new LinkedList<>();
    }

    @Override
    public List<IMapMovableElement> getMovableElements() {
        return new LinkedList<>(movableElements);
    }

    @Override
    public void addElement(IMapElement element) {
        if (element.isMovable()) {
            movableElements.add((IMapMovableElement) element);
        }
        else {
            if (hasNonMovableElements()) {
                throw new IllegalStateException("MirrorMapCell cannot contain more than one plant");
            }
            else {
                plant = Optional.of(element);
            }
        }
    }

    @Override
    public void removeElement(IMapElement element) {
        if (element.isMovable()) {
            movableElements.remove(element);
        }
        else {
            if (plant.isPresent() && plant.get().equals(element)) {
                plant = Optional.empty();
            }
        }
    }

    @Override
    public boolean containsElement(IMapElement element) {
        if (element.isMovable()) {
            return movableElements.contains(element);
        }
        else {
            return plant.isPresent() && plant.get().equals(element);
        }
    }

    @Override
    public void moved(IMapMovableElement movedElement, IVector2d oldPosition, IVector2d newPosition) {
        if (!position.equals(newPosition)) {
            removeElement(movedElement);
        }
    }

    @Override
    public void starved(IMapMovableElement starvedElement, IVector2d position) {
        removeElement(starvedElement);
    }

    @Override
    public void ate(IMapMovableElement ateElement, IMapElement eatenElement, IVector2d position) {
        if (plant.isPresent() && eatenElement.equals(plant.get())) {
            removeElement(eatenElement);
            if (!maxEnergyElements.contains(ateElement)) {
                normalized = false;
            }
        }
    }

    @Override
    public void bred(IMapMovableElement firstElement, IMapMovableElement secondElement,
                      IMapMovableElement newElement, IVector2d position) {
        normalized = false;
    }

    // ----------------------------- below is awful code -------------------------------------------- //
    // it can be written in a better way, still it will remain bad, but if we won't need them, then
    // i won't be disappointed that I spent too much time making it prettier :)

    // awful function, but still provides O(n) for some expected sequence of usage
    public int getEnergyConsumedBy(IMapMovableElement element) {
        if (plant.isEmpty() || (normalized && !maxEnergyElements.contains(element))) {
            return 0;
        }
        if (normalized) {
            Plant plantEl = (Plant)plant.get();
            return plantEl.getEnergy() / maxEnergyElements.size();
        }
        else {
            int maxEnergy = 0;
            int maxEnergyCount = 0;
            for (IMapMovableElement el : movableElements) {
                // is it legal?
                Animal animal = (Animal) el;
                if (animal.getEnergy() > maxEnergy) {
                    maxEnergy = animal.getEnergy();
                    maxEnergyCount = 1;
                }
                else if (animal.getEnergy() == maxEnergy) {
                    maxEnergyCount++;
                }
            }
            Animal animal = (Animal) element;
            if (animal.getEnergy() != maxEnergy) {
                return 0;
            }

            Plant plantEl = (Plant)plant.get();
            return plantEl.getEnergy() / maxEnergyCount;
        }
    }

    public Optional<IMapMovableElement> canBreed(IMapMovableElement element) {
        if (movableElements.size() < 2) {
            return Optional.empty();
        }
        if (normalized) {
            if (maxEnergyElements.size() == 1) {
                if (maxEnergyElements.contains(element)) {
                    normalized = false;
                    return Optional.of(secondMaxEnergyElement.get());
                }
                else if (secondMaxEnergyElement.equals(element)) {
                    for (IMapMovableElement el : maxEnergyElements) {  // we are sure there is only one
                        normalized = false;
                        return Optional.of(el);
                    }
                }
            }
            else {
                if (maxEnergyElements.contains(element)) {
                    for (IMapMovableElement el : maxEnergyElements) {
                        if (!el.equals(element)) {
                            normalized = false;
                            return Optional.of(el);
                        }
                    }
                }
                else {
                    return Optional.empty();
                }
            }
        }
        else {
            LinkedList<IMapMovableElement> maxEnergyElements = new LinkedList<>();
            LinkedList<IMapMovableElement> max2EnergyElements = new LinkedList<>();
            int maxEnergy = 0;
            int maxEnergyCount = 0;
            for (IMapMovableElement el : movableElements) {
                // is it legal?
                Animal animal = (Animal) el;
                if (animal.getEnergy() > maxEnergy) {
                    maxEnergy = animal.getEnergy();
                    maxEnergyCount = 1;
                    maxEnergyElements.clear();
                    maxEnergyElements.add(animal);
                }
                else if (animal.getEnergy() == maxEnergy) {
                    maxEnergyCount++;
                    maxEnergyElements.add(animal);
                }
            }

            int max2Energy = 0;
            int max2EnergyCount = 0;
            for (IMapMovableElement el : movableElements) {
                // is it legal?
                Animal animal = (Animal) el;
                if (animal.getEnergy() > max2Energy && animal.getEnergy() != maxEnergy) {
                    max2Energy = animal.getEnergy();
                    max2EnergyCount = 1;
                    max2EnergyElements.clear();
                    max2EnergyElements.add(animal);
                }
                else if (animal.getEnergy() == max2Energy) {
                    max2EnergyCount++;
                    max2EnergyElements.add(animal);
                }
            }

            Animal animal = (Animal) element;
            if (animal.getEnergy() != maxEnergy) {
                if (animal.getEnergy() != max2Energy || maxEnergyCount > 1) {
                    return Optional.empty();
                }
                return Optional.of(maxEnergyElements.getFirst());
            }
            else {
                if (maxEnergyCount > 1) {
                    IMapMovableElement first = maxEnergyElements.getFirst();
                    if (first.equals(element)) {
                        return Optional.of(maxEnergyElements.getLast());
                    }
                    return Optional.of(first);
                }
                else {
                    return Optional.of(max2EnergyElements.getFirst());
                }
            }
        }
        return Optional.empty();
    }
}
