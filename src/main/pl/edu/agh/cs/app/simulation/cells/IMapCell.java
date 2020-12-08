package pl.edu.agh.cs.app.simulation.cells;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.observers.IMoveObserver;

import java.util.List;

public interface IMapCell<IE extends IMapElement, E extends IMapElement, EM extends IMapMovableElement> extends IMoveObserver<EM> {
    int nonMovableElementsCount();

    int movableElementsCount();

    boolean hasNonMovableElements();

    boolean hasMovableElements();

    boolean isEmpty();

    boolean canMoveTo(IE element);

    List<E> getNonMovableElements();

    List<EM> getMovableElements();

    boolean addElement(IE element);

    void removeElement(IE element);

    boolean containsElement(IE element);
}
