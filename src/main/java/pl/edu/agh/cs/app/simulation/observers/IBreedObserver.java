package pl.edu.agh.cs.app.simulation.observers;

import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

public interface IBreedObserver<EM extends AbstractJungleMapMovableElement> {
    void bred(EM firstElement, EM secondElement, EM newElement,
              int firstEnergyBefore, int secondEnergyBefore, IVector2d position);
}
