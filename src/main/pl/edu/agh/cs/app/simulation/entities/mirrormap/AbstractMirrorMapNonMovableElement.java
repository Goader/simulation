package pl.edu.agh.cs.app.simulation.entities.mirrormap;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.maps.IWorldMap;
import pl.edu.agh.cs.app.simulation.maps.MirrorMap;
import pl.edu.agh.cs.app.simulation.utils.MapOrientation;

abstract public class AbstractMirrorMapNonMovableElement implements IMirrorMapElement {
    protected Vector2dBound position;
    protected final boolean MOVABLE = false;

    public AbstractMirrorMapNonMovableElement(Vector2dBound initialPosition) {
        position = initialPosition;
    }

    public Vector2dBound getPosition() {
        return position;
    }

    @Override
    public boolean isMovable() {
        return MOVABLE;
    }
}
