package pl.edu.agh.cs.app.simulation.observers;

import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;

public interface IEnergyChangePublisher {
    /**
     * Adds new observer object to the collection, so it can be informed of the changes.
     *
     * @param observer
     *            Object of the class which implements IEnergyChangeObserver interface.
     */
    void addEnergyChangeObserver(IEnergyChangeObserver observer);

    /**
     * Removes an observer object from the collection, so it is no longer notified of any changes.
     *
     * @param observer
     *            Object of the class which implements IMoveObserver interface.
     */
    void removeEnergyChangeObserver(IEnergyChangeObserver observer);
}
