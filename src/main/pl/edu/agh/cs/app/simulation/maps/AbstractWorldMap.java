package pl.edu.agh.cs.app.simulation.maps;

import pl.edu.agh.cs.app.simulation.cells.IMapCell;
import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.IMoveObserver;

import java.util.HashMap;
import java.util.Optional;

abstract public class AbstractWorldMap
        <T extends IMapCell, V extends IVector2d, IE extends IMapElement<V>, EM extends IMapMovableElement>
        implements IWorldMap<T, IE, EM> {
    // there is a problem, EM should extend IE too, that will be true for all the levels of abstraction on the way down
    // still, i couldn't make it happen as Java does not support multiple bounds if there is another type parameter among them
    // so, as in Python, we assume programmers do read documentation and won't fail that little moment :)
    protected HashMap<V, T> cells;

    public AbstractWorldMap() {
        cells = new HashMap<>();
    }

    abstract protected T buildCell(V position);

    public boolean place(IE element) {
        V position = element.getPosition();

        if (canMoveTo(element, position)) {
            if (!containsCellAt(position)) {
                // problem with instanciating T, builder method used, that needs to be implemented
                cells.put(position, buildCell(position));
            }
            cells.get(position).addElement(element);
            if (element.isMovable()) {
                EM movableElement = (EM) element;
                movableElement.addMoveObserver(cells.get(position));
                movableElement.addMoveObserver((IMoveObserver<IMapMovableElement>) this);
            }
            return true;
        }
        return false;
    }

    protected boolean containsCellAt(IVector2d position) {
        return cells.containsKey(position);
    }

    @Override
    public boolean canMoveTo(IE element, IVector2d position) {
        return !containsCellAt(position) || cells.get(position).canMoveTo(element);
    }

    @Override
    public boolean isOccupied(IVector2d position) {
        return containsCellAt(position) && !cells.get(position).isEmpty();
    }

    // not the best idea, it does not follow encapsulation, may be fixed later
    @Override
    public Optional<T> getCell(IVector2d position) {
        return Optional.ofNullable(cells.get(position));
    }
}
