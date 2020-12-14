package pl.edu.agh.cs.app.simulation.observers;

public interface IViewPublisher {
    /**
     * Adds new observer object to the collection, so it can be informed of the changes.
     *
     * @param observer
     *            Object of the class which implements IDaySwitchObserver interface.
     */
    void addDaySwitchObserver(IViewObserver observer);

    /**
     * Removes an observer object from the collection, so it is no longer notified of any changes.
     *
     * @param observer
     *            Object of the class which implements IDaySwitchObserver interface.
     */
    void removeDaySwitchObserver(IViewObserver observer);
}
