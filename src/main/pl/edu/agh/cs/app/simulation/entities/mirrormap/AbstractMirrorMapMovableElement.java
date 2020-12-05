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
    protected HashSet<IMoveObserver> observers;

    public AbstractMirrorMapMovableElement(Vector2dBound initialPosition) {
        position = initialPosition;
        observers = new HashSet<>();
        oriented = MapOrientation.fromInteger((int) Math.random() * MapOrientation.values().length);
    }

    @Override
    public void addMoveObserver(IMoveObserver<IMapMovableElement> observer) {
        observers.add(observer);
    }

    @Override
    public void removeMoveObserver(IMoveObserver<IMapMovableElement> observer) {
        observers.remove(observer);
    }

    public Vector2dBound getPosition() {
        return position;
    }

    @Override
    public boolean isMovable() {
        return MOVABLE;
    }
}
