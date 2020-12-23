package pl.edu.agh.cs.app.simulation;

import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.*;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.maps.JungleMap;

import java.util.*;

public class Engine<T extends JungleMapCell, IE extends IJungleMapElement, E extends AbstractJungleMapNonMovableElement,
        EM extends AbstractJungleMapMovableElement> {
    protected final JungleMap<T, IE, E, EM> map;

    public Engine(JungleMap<T, IE, E, EM> map) {
        this.map = map;
    }

    public void feed() {
        for (Vector2dBound position : map.getActivePositions()) {
            JungleMapCell<IE, E, EM> cell = map.getCell(position).get();
            if (cell.hasMovableElements() && cell.hasNonMovableElements()) {
                LinkedList<EM> elements = new LinkedList<>(cell.getMaxEnergyElements());
                E plant = (E) cell.getNonMovableElements().getFirst();
                int count = elements.size();
                int energyPerEl = plant.getEnergy() / count;
                int energyLeftover = plant.getEnergy() % count;
                for (EM el : elements) {
                    el.eat(plant, energyLeftover > 0 ? energyPerEl + 1 : energyPerEl);
                    energyLeftover--;
                }
            }
        }
    }

    public void bringTogether() {
        for (Vector2dBound position : map.getActivePositions()) {
            JungleMapCell<IE, E, EM> cell = map.getCell(position).get();
            if (cell.movableElementsCount() >= 2) {
                Optional<Map.Entry<EM, EM>> optPair = cell.getBreedPair();
                if (optPair.isEmpty()) continue;
                Map.Entry<EM, EM> pair = optPair.get();
                pair.getKey().breed(pair.getValue());
            }
        }
    }
}
