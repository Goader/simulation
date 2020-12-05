package pl.edu.agh.cs.app.simulation.maps;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.utils.IMoveObserver;

import java.util.List;

public interface IMapCell extends IMoveObserver {
    int nonMovableElementsCount();

    int movableElementsCount();

    boolean hasNonMovableElements();

    boolean hasMovableElements();

    boolean isEmpty();

    boolean canMoveTo(IMapElement element);

    List<IMapElement> getNonMovableElements();

    List<IMapMovableElement> getMovableElements();

    void addElement(IMapElement element);

    void removeElement(IMapElement element);

    boolean containsElement(IMapElement element);
}
