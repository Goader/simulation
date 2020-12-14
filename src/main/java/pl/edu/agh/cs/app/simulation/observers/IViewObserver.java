package pl.edu.agh.cs.app.simulation.observers;

import pl.edu.agh.cs.app.simulation.geometry.IVector2d;

public interface IViewObserver {
    void updatedView(IVector2d position);
}
