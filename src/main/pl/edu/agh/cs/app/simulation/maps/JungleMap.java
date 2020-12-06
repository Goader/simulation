package pl.edu.agh.cs.app.simulation.maps;

import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapNonMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dEucl;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IEatObserver;
import pl.edu.agh.cs.app.simulation.observers.IStarveObserver;
import pl.edu.agh.cs.app.simulation.utils.MapOrientation;

import java.util.HashSet;
import java.util.Optional;

public class JungleMap<T extends JungleMapCell, IE extends IJungleMapElement, E extends AbstractJungleMapNonMovableElement,
        EM extends AbstractJungleMapMovableElement>
        extends MirrorMap<T, IE, E, EM>
        implements IStarveObserver<EM>, IBreedObserver<EM> {
    protected HashSet<Vector2dBound> emptyJungleCells;
    protected HashSet<Vector2dBound> emptyNonJungleCells;
    protected final Vector2dBound lowerleftJungleCorner;
    protected final Vector2dBound upperrightJungleCorner;


    public JungleMap(int width, int height, int jungleHeight, int jungleWidth) {
        super(width, height);
        if (jungleHeight >= height || jungleWidth >= width) {
            throw new IllegalArgumentException("Jungle height/width cannot exceed or be equal to the height/width of map");
        }

        lowerleftJungleCorner = new Vector2dBound((width - jungleWidth) / 2, (height - jungleHeight) / 2,
                width, height);
        upperrightJungleCorner = new Vector2dBound(lowerleftJungleCorner.getX() + jungleWidth - 1,
                lowerleftJungleCorner.getY() + jungleHeight - 1, width, height);

        // Saving empty cells to select random ones efficiently
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!isJungle(new Vector2dBound(x, y, width, height))) {
                    emptyNonJungleCells.add(new Vector2dBound(x, y, width, height));
                }
            }
        }
        for (int x = lowerleftJungleCorner.getX(); x <= upperrightJungleCorner.getX(); x++) {
            for (int y = lowerleftJungleCorner.getY(); y <= upperrightJungleCorner.getY(); y++) {
                emptyJungleCells.add(new Vector2dBound(x, y, width, height));
            }
        }
    }

    @Override
    protected T buildCell(Vector2dBound position) {
        return (T) new JungleMapCell(position);
    }

    public boolean isJungle(IVector2d position) {
        return position.follows(lowerleftJungleCorner) && position.precedes(upperrightJungleCorner);
    }

    public Optional<Vector2dBound> getRandomEmptyJungleCell() {
        return Optional.empty();
    }

    public Optional<Vector2dBound> getRandomEmptyNonJungleCell() {
        return Optional.empty();
    }

    public int getJungleHeight() {
        return upperrightJungleCorner.getY() - lowerleftJungleCorner.getY();
    }

    public int getJungleWidth() {
        return upperrightJungleCorner.getX() - lowerleftJungleCorner.getX();
    }

    public Vector2dBound getFreeNeighbourPosition(Vector2dBound position) {
        for (MapOrientation neighbour : MapOrientation.values()) {
            Vector2dBound testPosition = position.add(neighbour.toUnitVector());
            if (!containsCellAt(testPosition) || !cells.get(testPosition).hasMovableElements()) {
                return testPosition;
            }
        }
        return position.add(new Vector2dEucl(1, 1));  // can be changed
    }

    @Override
    public void bred(EM firstElement, EM secondElement, EM newElement, IVector2d position) {
        place((IE) newElement);
    }

    @Override
    public void starved(EM starvedElement, IVector2d position) {
        JungleMapCell cell = getCell(position).get();
        starvedElement.removeStarveObserver(cell);
        if (cell.isEmpty() ||
                (cell.nonMovableElementsCount() == 0 && cell.movableElementsCount() == 1
                        && cell.containsElement(starvedElement))) {
            cells.remove(position);
        }
    }
}
