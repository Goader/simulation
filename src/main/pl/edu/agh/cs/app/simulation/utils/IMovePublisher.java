package pl.edu.agh.cs.app.simulation.utils;

public interface IMovePublisher {
    /**
     * Adds new observer object to the collection, so it can be informed of the changes.
     *
     * @param observer
     *            Object of the class which implements IMoveObserver interface.
     */
    void addObserver(IMoveObserver observer);

    /**
     * Removes an observer object from the collection, so it is no longer notified of any changes.
     *
     * @param observer
     *            Object of the class which implements IMoveObserver interface.
     */
    void removeObserver(IMoveObserver observer);
}
