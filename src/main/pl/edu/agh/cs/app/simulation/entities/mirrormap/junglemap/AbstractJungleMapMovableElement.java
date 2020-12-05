package pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap;

import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.AbstractMirrorMapMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;

abstract public class AbstractJungleMapMovableElement
        extends AbstractMirrorMapMovableElement
        implements IJungleMapElement {
    public AbstractJungleMapMovableElement(Vector2dBound initialPosition) {
        super(initialPosition);
    }
}
