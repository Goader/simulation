package pl.edu.agh.cs.app.simulation.maps;

import pl.edu.agh.cs.app.simulation.cells.MirrorMapCell;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.AbstractMirrorMapNonMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.AbstractMirrorMapMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.IMirrorMapElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;

public class MirrorMap
        <T extends MirrorMapCell, IE extends IMirrorMapElement, E extends AbstractMirrorMapNonMovableElement,
                EM extends AbstractMirrorMapMovableElement>
        extends AbstractWorldMap<T, Vector2dBound, IE, EM> {
    private final int width;
    private final int height;
    private final int maxX;
    private final int maxY;

    public MirrorMap(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        maxX = width - 1;
        maxY = height - 1;
    }

    @Override
    protected T buildCell(Vector2dBound position) {
        return (T) new MirrorMapCell<IE, E, EM>(position);
    }

    @Override
    public boolean place(IE element) {
        Vector2dBound position = element.getPosition();
        if (position.getXBound() != maxX + 1 || position.getYBound() != maxY) {
            return false;
        }
        return super.place(element);
    }

    @Override
    public void moved(EM movedElement, IVector2d oldPosition, IVector2d newPosition) {
        movedElement.removeMoveObserver(cells.get(oldPosition));  // we are sure it still exists
        place((IE) movedElement);  // creates MapCell at the newPosition if there is null
        movedElement.addMoveObserver(cells.get(newPosition));  // we are sure it is created
        MirrorMapCell cell = getCell(oldPosition).get();
        // If there is no elements in cell or there is left only movedElement, but the cell hasn't deleted it yet,
        // then we just remove the cell from the HashMap, because after notifying all the observers it will be empty
        if (cell.isEmpty() ||
                (cell.nonMovableElementsCount() == 0 && cell.movableElementsCount() == 1
                        && cell.containsElement(movedElement))) {
            cells.remove(oldPosition);
        }
    }
}
