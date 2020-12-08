package pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap;

import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.maps.JungleMap;

public class Animal extends AbstractJungleMapMovableElement {
    protected JungleMap map;

    public Animal(Vector2dBound initialPosition, int startEnergy, int moveEnergyCost, Genotype genotype, JungleMap map) {
        super(initialPosition, startEnergy, moveEnergyCost, genotype);
        this.map = map;
    }

    @Override
    public void eat(AbstractJungleMapNonMovableElement eatenElement, int eatenEnergy) {
        energy += eatenEnergy;
        notifyEatObservers(eatenElement, eatenEnergy, position);
    }

    @Override
    public void breed(AbstractJungleMapMovableElement mate) {
        int childEnergy = this.energy / 4 + mate.getEnergy() / 4;
        int mateOriginalEnergy = mate.getEnergy();
        int thisOriginalEnergy = this.energy;
        mate.takeEnergy(mate.getEnergy() / 4);
        this.takeEnergy(this.energy / 4);

        Genotype newGenotype = Genotype.fromTwoGenotypes(this.genotype, mate.genotype);

        Animal child = new Animal(map.getFreeNeighbourPosition(position), startEnergy, moveEnergyCost, newGenotype, map);
        child.takeEnergy(startEnergy - childEnergy);

        notifyBreedObservers(mate, child, thisOriginalEnergy, mateOriginalEnergy, position);
    }

    public void starve() {
        if (energy <= 0) {
            notifyStarveObservers(position);
        }
    }

    // when energy is subtracted??
    @Override
    public void move() {
        starve();
        Vector2dBound newPosition = position.add(oriented.toUnitVector());
        if (map.canMoveTo(this, newPosition)) {
            Vector2dBound oldPosition = position;
            position = newPosition;
            notifyMoveObservers(oldPosition, newPosition);
        }
    }
}
