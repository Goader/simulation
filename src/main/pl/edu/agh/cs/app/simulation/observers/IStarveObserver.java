package pl.edu.agh.cs.app.simulation.observers;

import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

public interface IStarveObserver<EM extends AbstractJungleMapMovableElement> {
    void starved(EM starvedElement, IVector2d position);
}
