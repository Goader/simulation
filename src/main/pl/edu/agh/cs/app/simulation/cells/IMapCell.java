package pl.edu.agh.cs.app.simulation.cells;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.observers.IMoveObserver;

import java.util.List;

public interface IMapCell<E extends IMapElement, EM extends IMapMovableElement> extends IMoveObserver<EM> {
    int nonMovableElementsCount();

    int movableElementsCount();

    boolean hasNonMovableElements();

    boolean hasMovableElements();

    boolean isEmpty();

    boolean canMoveTo(E element);

    List<E> getNonMovableElements();

    List<EM> getMovableElements();

    void addElement(E element);

    void removeElement(E element);

    boolean containsElement(E element);
}
