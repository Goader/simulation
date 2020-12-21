package pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap;

import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.maps.JungleMap;
import pl.edu.agh.cs.app.simulation.observers.IViewObserver;
import pl.edu.agh.cs.app.simulation.observers.IViewPublisher;
import pl.edu.agh.cs.app.ui.elements.AnimalView;

import java.util.HashSet;

public class Animal extends AbstractJungleMapMovableElement implements IViewPublisher {
    protected JungleMap map;
    protected AnimalView view;
    protected HashSet<IViewObserver> viewObservers;

    public Animal(Vector2dBound initialPosition, int startEnergy, int moveEnergyCost, Genotype genotype, JungleMap map) {
        super(initialPosition, startEnergy, moveEnergyCost, genotype);
        this.map = map;
        this.view = new AnimalView(this);
        this.viewObservers = new HashSet<>();
    }

    public AnimalView getView() {
        return view;
    }

    public int getMoveEnergyCost() {
        return moveEnergyCost;
    }

    @Override
    public void eat(AbstractJungleMapNonMovableElement eatenElement, int eatenEnergy) {
        energy += eatenEnergy;
        notifyEatObservers(eatenElement, eatenEnergy, position);
        notifyViewObservers(position);
    }

    @Override
    public void breed(AbstractJungleMapMovableElement mate) {
        int childEnergy = this.energy / 4 + mate.getEnergy() / 4;
        int mateOriginalEnergy = mate.getEnergy();
        int thisOriginalEnergy = this.energy;
        mate.takeEnergy(mate.getEnergy() / 4);
        this.takeEnergy(this.energy / 4);

        Genotype newGenotype = Genotype.fromTwoGenotypes(this.genotype, mate.genotype);
        int childEnergyCost = (moveEnergyCost + mate.moveEnergyCost) / 2;

        Animal child = new Animal(map.getFreeNeighbourPosition(position), startEnergy, childEnergyCost, newGenotype, map);
        child.takeEnergy(startEnergy - childEnergy);

        notifyBreedObservers(mate, child, thisOriginalEnergy, mateOriginalEnergy, position);

        notifyViewObservers(position);
        notifyViewObservers(child.position);
    }

    protected boolean willStarve() {
        return energy <= 0;
    }

    public void starve() {
        if (willStarve()) {
            // notifying view observers must be done here, so that view elements doesnt lose contact with element
            HashSet<IViewObserver> observersCopy = (HashSet<IViewObserver>) viewObservers.clone();
            notifyStarveObservers(position);
            for (IViewObserver observer : observersCopy) {
                observer.updatedView(position);
            }
        }
    }

    // when energy is subtracted??
    @Override
    public void move() {
        if (willStarve()) {
            starve();
        } else {
            Vector2dBound newPosition = position.add(oriented.toUnitVector());
            if (map.canMoveTo(this, newPosition)) {
                Vector2dBound oldPosition = position;
                position = newPosition;
                notifyMoveObservers(oldPosition, newPosition);

                int energyBefore = energy;
                energy -= moveEnergyCost;
                notifyEnergyChangeObservers(energyBefore, energy);

                notifyViewObservers(oldPosition);
                notifyViewObservers(newPosition);
            }
        }
    }

    protected void notifyViewObservers(IVector2d position) {
        HashSet<IViewObserver> observersCopy = (HashSet<IViewObserver>) viewObservers.clone();
        for (IViewObserver observer : observersCopy) {
            observer.updatedView(position);
        }
    }

    @Override
    public void addViewObserver(IViewObserver observer) {
        viewObservers.add(observer);
    }

    @Override
    public void removeViewObserver(IViewObserver observer) {
        viewObservers.remove(observer);
    }
}
