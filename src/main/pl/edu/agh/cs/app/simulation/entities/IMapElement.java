package pl.edu.agh.cs.app.simulation.entities;

import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.maps.IWorldMap;

public interface IMapElement<V extends IVector2d> {
    /**
     * Finds the position of the object on the map
     *
     * @return Vector2d object indicating current position of the element
     */
    V getPosition();

    boolean isMovable();
}
