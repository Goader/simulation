package pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap;

import pl.edu.agh.cs.app.simulation.entities.mirrormap.AbstractMirrorMapNonMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;

abstract public class AbstractJungleMapNonMovableElement
        extends AbstractMirrorMapNonMovableElement
        implements IJungleMapElement {
    protected int energy;

    public AbstractJungleMapNonMovableElement(Vector2dBound initialPosition, int energy) {
        super(initialPosition);
        this.energy = energy;
    }

    @Override
    public int getEnergy() {
        return energy;
    }
}
