package pl.edu.agh.cs.app.simulation.utils;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

// is it okay to create such a big observer? or should we divide it into 4 different ones?
public interface IMoveObserver {
    /**
     * By getting the context of changes and making appropriate changes inside, provides the cohesion of the states.
     *
     * @param movedElement
     *            Object that has been moved outside, so it is a part of the context here.
     * @param oldPosition
     *            The previous position of the moved object.
     * @param newPosition
     *            The position object has been moved to.
     */
    void moved(IMapMovableElement movedElement, IVector2d oldPosition, IVector2d newPosition);

    void starved(IMapMovableElement starvedElement, IVector2d position);

    void ate(IMapMovableElement ateElement, IMapElement eatenElement, IVector2d position);

    void bred(IMapMovableElement firstElement, IMapMovableElement secondElement,
               IMapMovableElement newElement, IVector2d position);
}
