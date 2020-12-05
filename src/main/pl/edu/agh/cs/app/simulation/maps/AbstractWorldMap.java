package pl.edu.agh.cs.app.simulation.maps;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

import java.util.HashMap;
import java.util.Optional;

abstract public class AbstractWorldMap implements IWorldMap {
    protected HashMap<IVector2d, IMapCell> cells;

    public AbstractWorldMap() {
        cells = new HashMap<>();
    }

    protected boolean containsCellAt(IVector2d position) {
        return cells.containsKey(position);
    }

    @Override
    public boolean canMoveTo(IMapElement element, IVector2d position) {
        return canMoveToVector(element, position).isPresent();
    }

    protected IVector2d legalizePosition(IVector2d position) {
        // can support different behaviour depending on whether our map contains
        // some kind of holes/teleports and so on
        return position;
    }

    @Override
    public Optional<IVector2d> canMoveToVector(IMapElement element, IVector2d position) {
        IVector2d legalPosition = legalizePosition(position);
        return !containsCellAt(legalPosition) || cells.get(legalPosition).canMoveTo(element) ?
                Optional.of(legalPosition) : Optional.empty();
    }

    @Override
    public boolean isOccupied(IVector2d position) {
        return containsCellAt(position) && !cells.get(position).isEmpty();
    }

    @Override
    public Optional<IMapCell> getCell(IVector2d position) {
        return Optional.ofNullable(cells.get(position));
    }
}
