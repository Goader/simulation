package pl.edu.agh.cs.app.simulation.observers;

import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;

public interface IMovePublisher {
    /**
     * Adds new observer object to the collection, so it can be informed of the changes.
     *
     * @param observer Object of the class which implements IMoveObserver interface.
     */
    void addMoveObserver(IMoveObserver<IMapMovableElement> observer);

    /**
     * Removes an observer object from the collection, so it is no longer notified of any changes.
     *
     * @param observer Object of the class which implements IMoveObserver interface.
     */
    void removeMoveObserver(IMoveObserver<IMapMovableElement> observer);
}
