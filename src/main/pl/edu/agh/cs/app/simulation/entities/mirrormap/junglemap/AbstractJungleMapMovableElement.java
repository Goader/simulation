package pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap;

import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.AbstractMirrorMapMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.observers.*;

import java.util.HashSet;

abstract public class AbstractJungleMapMovableElement
        extends AbstractMirrorMapMovableElement
        implements IJungleMapElement, IEatPublisher, IStarvePublisher, IBreedPublisher {
    protected int energy;
    protected final int startEnergy;
    protected final int moveEnergyCost;
    protected final int breedEnergyThreshold;

    protected final Genotype genotype;

    protected final HashSet<IEatObserver> eatObservers;
    protected final HashSet<IStarveObserver> starveObservers;
    protected final HashSet<IBreedObserver> breedObservers;

    public AbstractJungleMapMovableElement(Vector2dBound initialPosition, int startEnergy, int moveEnergyCost, Genotype genotype) {
        super(initialPosition);

        eatObservers = new HashSet<>();
        starveObservers = new HashSet<>();
        breedObservers = new HashSet<>();

        breedEnergyThreshold = startEnergy / 2;
        energy = startEnergy;
        this.startEnergy = startEnergy;
        this.moveEnergyCost = moveEnergyCost;

        this.genotype = genotype;
    }

    abstract public void eat(AbstractJungleMapNonMovableElement eatenElement, int eatenEnergy);

    abstract public void breed(AbstractJungleMapMovableElement mate);

    public void rotate() {
        int angle = genotype.getRandomRotation();
        oriented = oriented.rotate(angle);
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    protected void takeEnergy(int energy) {
        this.energy -= energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public boolean canBreed() {
        return energy > breedEnergyThreshold;
    }

    public void notifyEatObservers(AbstractJungleMapNonMovableElement element, int eatenEnergy, Vector2dBound position) {
        HashSet<IEatObserver> observersCopy = (HashSet<IEatObserver>) eatObservers.clone();
        for (IEatObserver observer : observersCopy) {
            observer.ate(this, element, eatenEnergy, position);
        }
    }

    public void notifyStarveObservers(Vector2dBound position) {
        HashSet<IStarveObserver> observersCopy = (HashSet<IStarveObserver>) starveObservers.clone();
        for (IStarveObserver observer : observersCopy) {
            observer.starved(this, position);
        }
    }

    public void notifyBreedObservers(AbstractJungleMapMovableElement mate, AbstractJungleMapMovableElement child,
                                     int firstEnergyBefore, int secondEnergyBefore, Vector2dBound position) {
        HashSet<IBreedObserver> observersCopy = (HashSet<IBreedObserver>) breedObservers.clone();
        for (IBreedObserver observer : observersCopy) {
            observer.bred(this, mate, child, firstEnergyBefore, secondEnergyBefore, position);
        }
    }

    @Override
    public void addBreedObserver(IBreedObserver observer) {
        breedObservers.add(observer);
    }

    @Override
    public void addStarveObserver(IStarveObserver observer) {
        starveObservers.add(observer);
    }

    @Override
    public void addEatObserver(IEatObserver observer) {
        eatObservers.add(observer);
    }

    @Override
    public void removeBreedObserver(IBreedObserver observer) {
        breedObservers.remove(observer);
    }

    @Override
    public void removeStarveObserver(IStarveObserver observer) {
        starveObservers.remove(observer);
    }

    @Override
    public void removeEatObserver(IEatObserver observer) {
        eatObservers.remove(observer);
    }
}
