package pl.edu.agh.cs.app.simulation.observers;

import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapMovableElement;

public interface IEnergyChangeObserver<EM extends AbstractJungleMapMovableElement> {
    void energyChanged(EM element, int energyBefore, int energyAfter);
}
