package pl.edu.agh.cs.app.simulation.cells;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.AbstractMirrorMapMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.AbstractMirrorMapNonMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.IMirrorMapElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapNonMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IEatObserver;
import pl.edu.agh.cs.app.simulation.observers.IStarveObserver;

import java.util.HashSet;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;

public class JungleMapCell<IE extends IJungleMapElement,
        E extends AbstractJungleMapNonMovableElement, EM extends AbstractJungleMapMovableElement>
        extends MirrorMapCell<IE, E, EM>
        implements IEatObserver<EM, E>, IStarveObserver<EM>, IBreedObserver<EM> {
    protected TreeMap<Integer, HashSet<EM>> energyTree;

    public JungleMapCell(IVector2d initialPosition) {
        super(initialPosition);
        energyTree = new TreeMap<>();
    }

    @Override
    public void bred(EM firstElement, EM secondElement, EM newElement, IVector2d position) {

    }

    @Override
    public void ate(EM ateElement, E eatenElement, IVector2d position) {

    }

    @Override
    public void starved(EM starvedElement, IVector2d position) {

    }

//    @Override
//    public void starved(IMapMovableElement starvedElement, IVector2d position) {
//        removeElement(starvedElement);
//    }
//
//    @Override
//    public void ate(IMapMovableElement ateElement, IMapElement eatenElement, IVector2d position) {
//        if (plant.isPresent() && eatenElement.equals(plant.get())) {
//            removeElement(eatenElement);
//            if (!maxEnergyElements.contains(ateElement)) {
//                normalized = false;
//            }
//        }
//    }
//
//    @Override
//    public void bred(IMapMovableElement firstElement, IMapMovableElement secondElement,
//                     IMapMovableElement newElement, IVector2d position) {
//        normalized = false;
//    }
//
//    public int getEnergyConsumedBy(IMapMovableElement element) {
//    }
//
//    public Optional<IMapMovableElement> canBreed(IMapMovableElement element) {
//
//    }
}
