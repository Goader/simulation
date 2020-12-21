package pl.edu.agh.cs.app.simulation.observers;

public interface ISeedPublisher {
    /**
     * Adds new observer object to the collection, so it can be informed of the changes.
     *
     * @param observer Object of the class which implements ISeedObserver interface.
     */
    void addSeedObserver(ISeedObserver observer);

    /**
     * Removes an observer object from the collection, so it is no longer notified of any changes.
     *
     * @param observer Object of the class which implements ISeedObserver interface.
     */
    void removeSeedObserver(ISeedObserver observer);
}
