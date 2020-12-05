package pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;

public class Plant implements IMapElement<Vector2dBound> {
    private final Vector2dBound position;
    private final int energy;  // think whether we should keep it here or handle at the simulation level
    private final boolean PASSABLE = true;
    private final boolean MOVABLE = false;

    public Plant(Vector2dBound position, int energy) {
        this.position = position;
        this.energy = energy;
    }

    @Override
    public Vector2dBound getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public boolean isMovable() {
        return MOVABLE;
    }
}
