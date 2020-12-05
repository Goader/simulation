package pl.edu.agh.cs.app.simulation.maps;

import pl.edu.agh.cs.app.simulation.cells.IMapCell;
import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

import java.util.HashMap;
import java.util.Optional;

abstract public class AbstractWorldMap
        <T extends IMapCell, V extends IVector2d, E extends IMapElement, EM extends IMapMovableElement>
        implements IWorldMap<T, E, EM> {
    protected HashMap<V, T> cells;

    public AbstractWorldMap() {
        cells = new HashMap<>();
    }

    protected boolean containsCellAt(IVector2d position) {
        return cells.containsKey(position);
    }

    @Override
    public boolean canMoveTo(E element, IVector2d position) {
        return !containsCellAt(position) || cells.get(position).canMoveTo(element);
    }

    @Override
    public boolean isOccupied(IVector2d position) {
        return containsCellAt(position) && !cells.get(position).isEmpty();
    }

    @Override
    public Optional<T> getCell(IVector2d position) {
        return Optional.ofNullable(cells.get(position));
    }
}
