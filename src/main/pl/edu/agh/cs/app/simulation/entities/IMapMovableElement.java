package pl.edu.agh.cs.app.simulation.entities;

import pl.edu.agh.cs.app.simulation.utils.IMovePublisher;

public interface IMapMovableElement extends IMapElement, IMovePublisher {
    /**
     * Changes current position of the object on the map.
     */
    void move();
}
