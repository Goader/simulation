package pl.edu.agh.cs.app.simulation.maps;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.utils.IMoveObserver;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

import java.util.Optional;

public interface IWorldMap extends IMoveObserver {
    /**
     * Indicate if any object can move to the given position.
     *
     * @param element
     *            Element which wants to be moved there.
     * @param position
     *            The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(IMapElement element, IVector2d position);

    // need a better name for it
    Optional<IVector2d> canMoveToVector(IMapElement element, IVector2d position);

    /**
     * Place a animal on the map.
     *
     * @param element
     *            The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean place(IMapElement element);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position
     *            Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(IVector2d position);

    /**
     * Return a Cell object at a given position.
     *
     * @param position
     *            The position of the Cell object.
     * @return Cell object or empty Optional if the position is not occupied.
     */
    Optional<IMapCell> getCell(IVector2d position);
}
