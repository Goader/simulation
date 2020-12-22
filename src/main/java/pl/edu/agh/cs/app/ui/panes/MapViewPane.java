package pl.edu.agh.cs.app.ui.panes;

import javafx.scene.layout.GridPane;
import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.entities.IMapElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dEucl;
import pl.edu.agh.cs.app.simulation.maps.JungleMap;
import pl.edu.agh.cs.app.simulation.observers.ISeedObserver;
import pl.edu.agh.cs.app.simulation.observers.IViewObserver;
import pl.edu.agh.cs.app.ui.elements.MapCellView;

public class MapViewPane extends GridPane implements IViewObserver, ISeedObserver {
    protected final int width;
    protected final int height;
    protected final int cellSidePixels;
    protected final JungleMap<JungleMapCell<IJungleMapElement, Plant, Animal>, IJungleMapElement, Plant, Animal> map;
    protected MapCellView[][] cellViews;

    public MapViewPane(int width, int height, JungleMap<JungleMapCell<IJungleMapElement, Plant, Animal>, IJungleMapElement, Plant, Animal> map,
                       int cellSidePixels, SimulationViewPane simulationView) {
        super();
        cellViews = new MapCellView[width][height];
        this.width = width;
        this.height = height;
        this.map = map;
        this.cellSidePixels = cellSidePixels;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cellViews[i][j] = new MapCellView(this.cellSidePixels, map.isJungle(new Vector2dEucl(i, j)),
                        simulationView, map, new Vector2dEucl(i, j));
                this.add(cellViews[i][j], i, height - 1 - j);
            }
        }
    }

    protected void updateCell(IVector2d position) {
        if (0 <= position.getX() && position.getX() < width && 0 <= position.getY() && position.getY() < height) {
            cellViews[position.getX()][position.getY()].updated(position);
        }
    }

    @Override
    public void updatedView(IVector2d position) {
        updateCell(position);
    }

    @Override
    public void sowed(IMapElement sownElement, IVector2d position) {
        updateCell(position);
    }
}
