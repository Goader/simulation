package pl.edu.agh.cs.app.simulation.observers;

import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

public interface IMoveObserver<EM extends IMapMovableElement> {
    /**
     * By getting the context of changes and making appropriate changes inside, provides the cohesion of the states.
     *
     * @param movedElement Object that has been moved outside, so it is a part of the context here.
     * @param oldPosition  The previous position of the moved object.
     * @param newPosition  The position object has been moved to.
     */
    void moved(EM movedElement, IVector2d oldPosition, IVector2d newPosition);
}
