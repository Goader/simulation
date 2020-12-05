package pl.edu.agh.cs.app.simulation.maps;

import pl.edu.agh.cs.app.simulation.cells.IMapCell;
import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.observers.IMoveObserver;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

import java.util.Optional;

public interface IWorldMap<T extends IMapCell, IE extends IMapElement, EM extends IMapMovableElement>
        extends IMoveObserver<EM> {
    /**
     * Indicate if any object can move to the given position.
     *
     * @param element
     *            Element which wants to be moved there.
     * @param position
     *            The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(IE element, IVector2d position);

    /**
     * Place a animal on the map.
     *
     * @param element
     *            The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean place(IE element);

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
    Optional<T> getCell(IVector2d position);
}
