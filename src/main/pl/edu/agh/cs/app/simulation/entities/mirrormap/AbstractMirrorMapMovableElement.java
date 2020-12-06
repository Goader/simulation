package pl.edu.agh.cs.app.simulation.entities.mirrormap;

import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.observers.IMoveObserver;
import pl.edu.agh.cs.app.simulation.utils.MapOrientation;

import java.util.HashSet;

abstract public class AbstractMirrorMapMovableElement
        implements IMirrorMapElement, IMapMovableElement<Vector2dBound> {
    protected Vector2dBound position;
    protected final boolean MOVABLE = true;
    protected MapOrientation oriented;
    protected final HashSet<IMoveObserver> moveObservers;

    public AbstractMirrorMapMovableElement(Vector2dBound initialPosition) {
        position = initialPosition;
        moveObservers = new HashSet<>();
        oriented = MapOrientation.fromInteger((int) Math.random() * MapOrientation.values().length);
    }

    public void notifyMoveObservers(Vector2dBound oldPosition, Vector2dBound newPosition) {
        HashSet<IMoveObserver> observersCopy = (HashSet<IMoveObserver>) moveObservers.clone();
        for (IMoveObserver observer : observersCopy) {
            observer.moved(this, oldPosition, newPosition);
        }
    }

    @Override
    public void addMoveObserver(IMoveObserver<IMapMovableElement> observer) {
        moveObservers.add(observer);
    }

    @Override
    public void removeMoveObserver(IMoveObserver<IMapMovableElement> observer) {
        moveObservers.remove(observer);
    }

    public Vector2dBound getPosition() {
        return position;
    }

    public MapOrientation getOrientation() {
        return oriented;
    }

    @Override
    public boolean isMovable() {
        return MOVABLE;
    }
}
