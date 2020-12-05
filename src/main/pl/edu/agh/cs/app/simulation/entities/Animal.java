package pl.edu.agh.cs.app.simulation.entities;

import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.maps.IWorldMap;
import pl.edu.agh.cs.app.simulation.maps.MirrorMap;
import pl.edu.agh.cs.app.simulation.maps.MirrorMapCell;
import pl.edu.agh.cs.app.simulation.utils.*;

import java.util.HashSet;
import java.util.Optional;

public class Animal implements IMapMovableElement {
    private MapOrientation oriented;
    private IVector2d position;
    private IWorldMap map;
    private final boolean PASSABLE = false;
    private final boolean MOVABLE = true;
    private final HashSet<IMoveObserver> observers;

    private final Genotype genotype;

    private int energy;
    private int moveEnergyCost;
    private int breedEnergyThreshold;

    public Animal(IVector2d initialPosition, int startEnergy, int moveEnergyCost, Genotype genotype, IWorldMap map) {
        this(initialPosition, startEnergy, moveEnergyCost, genotype);
        assignMap(map);
    }

    public Animal(IVector2d initialPosition, int startEnergy, int moveEnergyCost, Genotype genotype) {
        position = initialPosition;
        oriented = MapOrientation.NORTH;
        observers = new HashSet<>();
        energy = startEnergy;
        this.moveEnergyCost = moveEnergyCost;
        this.genotype = genotype;
        breedEnergyThreshold = startEnergy / 2;
    }

    public boolean assignMap(IWorldMap map) {
        Optional<IVector2d> optPosition = map.canMoveToVector(this, position);
        if (optPosition.isEmpty()) {
            return false;
        }
        position = optPosition.get();
        this.map = map;
        map.place(this);
        return true;
    }

    @Override
    public void addObserver(IMoveObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IMoveObserver observer) {
        observers.remove(observer);  // doesn't raise an exception if the element isn't present, as in Python
    }

    // ask of iterator, and removing inside
    public void notifyObservers(IVector2d oldPosition, IVector2d newPosition) {
        HashSet<IMoveObserver> observersCopy = (HashSet<IMoveObserver>) observers.clone();
        for (IMoveObserver observer : observersCopy) {
            observer.moved(this, oldPosition, newPosition);
        }
    }

    public MapOrientation getOrientation() {
        return oriented;
    }

    @Override
    public IVector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public void rotate() {
        int angle = genotype.getRandomRotation();
        oriented = oriented.rotate(angle);
    }

    private void validMove(IVector2d move) {
        IVector2d newPosition = position.add(move);
        Optional<IVector2d> optPosition = map.canMoveToVector(this, newPosition);
        if (optPosition.isPresent()) {
            IVector2d oldPosition = position;
            position = optPosition.get();
            notifyObservers(oldPosition, position);
        }
    }

    @Override
    public void move() {
        // we are assuming the Animal will starve if doesn't have enough energy to move
        // still waiting for the answer of the teacher
        if (energy < moveEnergyCost) {
            starve();
        }
        else {
            validMove(oriented.toUnitVector());
        }

    }

    // can be also done like someone asks to starve, if we have <0 energy, we starve, if don't, then we ignore
    private void starve() {
        // notifyObservers();
    }

    public void eat() {
        MirrorMapCell cell = (MirrorMapCell) map.getCell(position).get();
        energy += cell.getEnergyConsumedBy(this);
        // notifyObservers();
    }

    public void breed() {
        MirrorMapCell cell = (MirrorMapCell) map.getCell(position).get();
        Optional<IMapMovableElement> optEl = cell.canBreed(this);
        if (optEl.isEmpty()) {
            return;
        }
        Animal animal = (Animal) optEl.get();

        MirrorMap mapm = (MirrorMap) map;
        Animal child = new Animal(mapm.getFreeNeighbourPosition(position), energy, moveEnergyCost,
                Genotype.fromTwoGenotypes(genotype, animal.genotype), map);

        // notifyObservers();
    }

    @Override
    public boolean isPassable() {
        return PASSABLE;
    }

    @Override
    public boolean isMovable() {
        return MOVABLE;
    }
}
