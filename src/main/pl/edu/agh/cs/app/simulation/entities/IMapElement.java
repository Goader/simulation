package pl.edu.agh.cs.app.simulation.entities;

import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

public interface IMapElement {
    /**
     * Finds the position of the object on the map
     *
     * @return Vector2d object indicating current position of the element
     */
    IVector2d getPosition();

    boolean isPassable();

    boolean isMovable();
}
