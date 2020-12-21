package pl.edu.agh.cs.app.simulation.utils;

import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.maps.JungleMap;
import pl.edu.agh.cs.app.simulation.observers.ISeedObserver;
import pl.edu.agh.cs.app.simulation.observers.ISeedPublisher;

import java.util.HashSet;
import java.util.Optional;

public class PlantSeeder implements ISeedPublisher {
    HashSet<ISeedObserver> seedObservers;
    JungleMap map;

    public PlantSeeder(JungleMap map) {
        this.seedObservers = new HashSet<>();
        this.map = map;
    }

    private void seedPlant(int energy, Vector2dBound position) {
        Plant plant = new Plant(position, energy);
        if (map.place(plant)) {
            notifySeedObservers(plant, position);
        }
    }

    public void seedPlantJungle(int energy) {
        Optional<Vector2dBound> optPos = map.getRandomEmptyJungleCellPosition();
        optPos.ifPresent(vector2dBound -> seedPlant(energy, vector2dBound));
    }

    public void seedPlantNotJungle(int energy) {
        Optional<Vector2dBound> optPos = map.getRandomEmptyNonJungleCellPosition();
        optPos.ifPresent(vector2dBound -> seedPlant(energy, vector2dBound));
    }

    protected void notifySeedObservers(Plant plant, Vector2dBound position) {
        HashSet<ISeedObserver> observersCopy = (HashSet<ISeedObserver>) seedObservers.clone();
        for (ISeedObserver observer : observersCopy) {
            observer.sowed(plant, position);
        }
    }

    @Override
    public void addSeedObserver(ISeedObserver observer) {
        seedObservers.add(observer);
    }

    @Override
    public void removeSeedObserver(ISeedObserver observer) {
        seedObservers.remove(observer);
    }
}
