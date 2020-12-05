package pl.edu.agh.cs.app.simulation.observers;

import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapNonMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

public interface IEatObserver<EM extends AbstractJungleMapMovableElement, EE extends AbstractJungleMapNonMovableElement> {
    void ate(EM ateElement, EE eatenElement, IVector2d position);
}
