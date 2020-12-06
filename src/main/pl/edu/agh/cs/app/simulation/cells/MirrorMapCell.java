package pl.edu.agh.cs.app.simulation.cells;

import pl.edu.agh.cs.app.simulation.entities.mirrormap.AbstractMirrorMapNonMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.AbstractMirrorMapMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.IMirrorMapElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

import java.util.*;

public class MirrorMapCell<IE extends IMirrorMapElement,
        E extends AbstractMirrorMapNonMovableElement, EM extends AbstractMirrorMapMovableElement>
        implements IMapCell<IE, E, EM> {
    protected HashSet<E> nonMovableElements;
    protected HashSet<EM> movableElements;

    protected IVector2d position;

    public MirrorMapCell(IVector2d initialPosition) {
        position = initialPosition;
        movableElements = new HashSet<>();
        nonMovableElements = new HashSet<>();
    }

    @Override
    public int nonMovableElementsCount() {
        return nonMovableElements.size();
    }

    @Override
    public int movableElementsCount() {
        return movableElements.size();
    }

    @Override
    public boolean hasNonMovableElements() {
        return !nonMovableElements.isEmpty();
    }

    @Override
    public boolean hasMovableElements() {
        return !movableElements.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return !hasNonMovableElements() && !hasMovableElements();
    }

    @Override
    public boolean canMoveTo(IE element) {
        return element.isMovable();
    }

    @Override
    public List<E> getNonMovableElements() {
        return new LinkedList<>(nonMovableElements);
    }

    @Override
    public List<EM> getMovableElements() {
        return new LinkedList<>(movableElements);
    }

    @Override
    public void addElement(IE element) {
        if (element.isMovable()) {
            movableElements.add((EM) element);
        }
        else {
            nonMovableElements.add((E) element);
        }
    }

    @Override
    public void removeElement(IE element) {
        if (element.isMovable()) {
            movableElements.remove(element);
        }
        else {
            nonMovableElements.remove(element);
        }
    }

    @Override
    public boolean containsElement(IE element) {
        if (element.isMovable()) {
            return movableElements.contains(element);
        }
        else {
            return nonMovableElements.contains(element);
        }
    }

    @Override
    public void moved(EM movedElement, IVector2d oldPosition, IVector2d newPosition) {
        removeElement((IE) movedElement);
    }
}
