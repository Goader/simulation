package pl.edu.agh.cs.app.simulation.maps;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.utils.MapOrientation;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dEucl;

import java.util.HashMap;

// should I make it as a more general implementation, and create additionally MirrorSavannaJungleMap or smth like that
// which would extend this map
public class MirrorMap extends AbstractWorldMap {
    protected HashMap<IVector2d, MirrorMapCell> cells;
    private final int width;
    private final int height;
    private final int maxX;
    private final int maxY;

    public MirrorMap(int width, int height) {
        // what if in generic map we have generic cell, but for more specific map we use more specific cells
        // should we always cast them to the more specific? (we know it's always instanceof it)
        super();  // what to do with max?
        this.width = width;
        this.height = height;
        maxX = width - 1;
        maxY = height - 1;
    }

    @Override
    public boolean place(IMapElement element) {
        IVector2d position = element.getPosition();
        if (canMoveTo(element, position)) {
            if (!containsCellAt(position)) {
                cells.put(position, new MirrorMapCell(position));
            }
            cells.get(position).addElement(element);
            return true;
        }
        return false;
    }

    @Override
    protected IVector2d legalizePosition(IVector2d position) {
        return (IVector2d) new Vector2dEucl(position.getX() % maxX, position.getY() % maxY);
    }

    @Override
    public void moved(IMapMovableElement movedElement, IVector2d oldPosition, IVector2d newPosition) {
        movedElement.removeObserver(cells.get(oldPosition));  // we are sure it still exists
        place(movedElement);  // creates MapCell at the newPosition if there is null
        movedElement.addObserver(cells.get(newPosition));  // we are sure it is created
    }

    @Override
    public void starved(IMapMovableElement starvedElement, IVector2d position) {
        starvedElement.removeObserver(cells.get(position));
    }

    @Override
    public void ate(IMapMovableElement ateElement, IMapElement eatenElement, IVector2d position) {
        // map isn't interested in it, so we just skip
    }

    public IVector2d getFreeNeighbourPosition(IVector2d position) {
        for (MapOrientation neighbour : MapOrientation.values()) {
            IVector2d testPosition = legalizePosition(position.add(neighbour.toUnitVector()));
            if (!containsCellAt(testPosition) || !cells.get(testPosition).hasMovableElements()) {
                return testPosition;
            }
        }
        return legalizePosition(position.add((IVector2d) new Vector2dEucl(1, 1)));  // can be changed
    }

    @Override
    public void bred(IMapMovableElement firstElement, IMapMovableElement secondElement,
                     IMapMovableElement newElement, IVector2d position) {
        place(newElement);
    }
}
