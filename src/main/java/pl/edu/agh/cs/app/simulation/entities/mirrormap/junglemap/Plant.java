package pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap;

import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.ui.elements.PlantView;

public class Plant extends AbstractJungleMapNonMovableElement {
    protected PlantView view;

    public Plant(Vector2dBound initialPosition, int energy) {
        super(initialPosition, energy);
        this.view = new PlantView();
    }

    public PlantView getView() {
        return view;
    }
}
