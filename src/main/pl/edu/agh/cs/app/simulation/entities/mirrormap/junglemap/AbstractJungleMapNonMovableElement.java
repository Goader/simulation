package pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap;

import pl.edu.agh.cs.app.simulation.entities.mirrormap.AbstractMirrorMapNonMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;

abstract public class AbstractJungleMapNonMovableElement
        extends AbstractMirrorMapNonMovableElement
        implements IJungleMapElement {
    public AbstractJungleMapNonMovableElement(Vector2dBound initialPosition) {
        super(initialPosition);
    }
}
