package pl.edu.agh.cs.app.simulation.maps;

import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapNonMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IMoveObserver;
import pl.edu.agh.cs.app.simulation.observers.IStarveObserver;
import pl.edu.agh.cs.app.simulation.utils.MapOrientation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
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

        emptyJungleCells = new HashSet<>();
        emptyNonJungleCells = new HashSet<>();

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

    @Override
    public boolean place(IE element) {
        boolean wasEmptyCell = false;
        if (!cells.containsKey(element.getPosition())) wasEmptyCell = true;
        if (super.place(element)) {
            if (element.isMovable()) {
                T cell = cells.get(element.getPosition());
                EM movableElement = (EM) element;
                movableElement.addBreedObserver(cell);
                movableElement.addBreedObserver(this);
                movableElement.addEatObserver(cell);
                movableElement.addStarveObserver(cell);
                movableElement.addStarveObserver(this);
            }
            if (wasEmptyCell) {
                if (isJungle(element.getPosition())) {
                    emptyJungleCells.remove(element.getPosition());
                }
                else {
                    emptyNonJungleCells.remove(element.getPosition());
                }
            }
            return true;
        }
        return false;
    }

    public void feed() {
        for (T cell : cells.values()) {
            if (cell.hasMovableElements() && cell.hasNonMovableElements()) {
                LinkedList<EM> elements = new LinkedList<>(cell.getMaxEnergyElements());
                E plant = (E) cell.getNonMovableElements().getFirst();
                int count = elements.size();
                int energyPerEl = plant.getEnergy() / count;
                int energyLeftover = plant.getEnergy() % count;
                for (EM el : elements) {
                    el.eat(plant, energyLeftover > 0 ? energyPerEl + 1 : energyLeftover);
                    energyLeftover--;
                }
            }
        }
    }

    public void bringTogether() {
        for (T cell : cells.values()) {
            if (cell.movableElementsCount() >= 2) {
                Optional<Map.Entry<EM, EM>> optPair = cell.getBreedPair();
                if (optPair.isEmpty()) continue;
                Map.Entry<EM, EM> pair = optPair.get();
                pair.getKey().breed(pair.getValue());
            }
        }
    }

    public boolean isJungle(IVector2d position) {
        return position.follows(lowerleftJungleCorner) && position.precedes(upperrightJungleCorner);
    }

    public Optional<Vector2dBound> getRandomEmptyJungleCellPosition() {
        int size = emptyJungleCells.size();
        if (size == 0) {
            return Optional.empty();
        }
        return Optional.of(emptyJungleCells.toArray(new Vector2dBound[0])[(int) (Math.random() * size)]); // check places for (int) near the rand
    }

    public Optional<Vector2dBound> getRandomEmptyNonJungleCellPosition() {
        int size = emptyNonJungleCells.size();
        if (size == 0) {
            return Optional.empty();
        }
        return Optional.of(emptyNonJungleCells.toArray(new Vector2dBound[0])[(int) (Math.random() * size)]);
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
        return position.add(MapOrientation.fromInteger((int) Math.random() * MapOrientation.values().length).toUnitVector());
    }

    @Override
    public void moved(EM movedElement, IVector2d oldPosition, IVector2d newPosition) {
        T cell = cells.get(oldPosition);
        movedElement.removeBreedObserver(cell);
        movedElement.removeEatObserver(cell);
        movedElement.removeStarveObserver(cell);
        super.moved(movedElement, oldPosition, newPosition);
        if (!cells.containsKey(oldPosition)) {
            if (isJungle(oldPosition)) {
                emptyJungleCells.add((Vector2dBound) oldPosition);
            }
            else {
                emptyNonJungleCells.add((Vector2dBound) oldPosition);
            }
        }
    }

    @Override
    public void bred(EM firstElement, EM secondElement, EM newElement,
                     int firstEnergyBefore, int secondEnergyBefore, IVector2d position) {
        place((IE) newElement);
    }

    @Override
    public void starved(EM starvedElement, IVector2d position) {
        JungleMapCell cell = getCell(position).get();
        starvedElement.removeStarveObserver(cell);
        starvedElement.removeEatObserver(cell);
        starvedElement.removeBreedObserver(cell);
        starvedElement.removeMoveObserver(cell);
        starvedElement.removeMoveObserver((IMoveObserver) this);
        starvedElement.removeBreedObserver(this);
        starvedElement.removeStarveObserver(this);
        if (cell.isEmpty() ||
                (cell.nonMovableElementsCount() == 0 && cell.movableElementsCount() == 1
                        && cell.containsElement(starvedElement))) {
            cells.remove(position);
            if (isJungle(position)) {
                emptyJungleCells.add((Vector2dBound) position);
            }
            else {
                emptyNonJungleCells.add((Vector2dBound) position);
            }
        }
    }
}
