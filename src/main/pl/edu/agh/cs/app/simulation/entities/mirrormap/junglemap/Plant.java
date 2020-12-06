package pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap;

import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;

public class Plant extends AbstractJungleMapNonMovableElement {

    public Plant(Vector2dBound initialPosition, int energy) {
        super(initialPosition, energy);
    }
}
