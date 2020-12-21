package pl.edu.agh.cs.app.simulation.observers;

public interface IStarvePublisher {
    /**
     * Adds new observer object to the collection, so it can be informed of the changes.
     *
     * @param observer Object of the class which implements IStarveObserver interface.
     */
    void addStarveObserver(IStarveObserver observer);

    /**
     * Removes an observer object from the collection, so it is no longer notified of any changes.
     *
     * @param observer Object of the class which implements IStarveObserver interface.
     */
    void removeStarveObserver(IStarveObserver observer);
}
