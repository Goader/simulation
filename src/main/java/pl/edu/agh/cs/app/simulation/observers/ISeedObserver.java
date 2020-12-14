package pl.edu.agh.cs.app.simulation.observers;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

public interface ISeedObserver<E extends IMapElement> {
    void sowed(E sownElement, IVector2d position);
}
