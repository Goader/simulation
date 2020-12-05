package pl.edu.agh.cs.app.simulation.entities;

import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

public class Plant implements IMapElement {
    private final IVector2d position;
    private final int energy;  // think whether we should keep it here or handle at the simulation level
    private final boolean PASSABLE = true;
    private final boolean MOVABLE = false;

    public Plant(IVector2d position, int energy) {
        this.position = position;
        this.energy = energy;
    }

    @Override
    public IVector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public boolean isPassable() {
        return PASSABLE;
    }

    @Override
    public boolean isMovable() {
        return MOVABLE;
    }
}
