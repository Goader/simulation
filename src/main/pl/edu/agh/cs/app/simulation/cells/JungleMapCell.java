package pl.edu.agh.cs.app.simulation.cells;

import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.IMapMovableElement;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

import java.util.Optional;

public class JungleMapCell extends MirrorMapCell {
    public JungleMapCell(IVector2d initialPosition) {
        super(initialPosition);
    }

//    @Override
//    public void moved(IMapMovableElement movedElement, IVector2d oldPosition, IVector2d newPosition) {
//        if (!position.equals(newPosition)) {
//            removeElement(movedElement);
//        }
//    }
//
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
