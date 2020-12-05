package pl.edu.agh.cs.app.simulation.entities;

import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.IMovePublisher;

public interface IMapMovableElement<V extends IVector2d> extends IMapElement<V>, IMovePublisher {
    /**
     * Changes current position of the object on the map.
     */
    void move();
}
