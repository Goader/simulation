package pl.edu.agh.cs.app.simulation.geometry;

public interface IVector2d {
    int getX();

    int getY();

    boolean precedes(IVector2d other);

    boolean follows(IVector2d other);

    IVector2d upperRight(IVector2d other);

    IVector2d lowerLeft(IVector2d other);

    IVector2d add(IVector2d other);

    IVector2d subtract(IVector2d other);

    IVector2d opposite();
}
