package pl.edu.agh.cs.app.simulation.geometry;

import java.util.Objects;

abstract public class AbstractVector2d implements IVector2d {
    protected final int x;
    protected final int y;

    public AbstractVector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof IVector2d)) return false;
        IVector2d vector2d = (IVector2d) o;
        return x == vector2d.getX() &&
                y == vector2d.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public boolean precedes(IVector2d other) {
        return this.x <= other.getX() && this.y <= other.getY();
    }

    @Override
    public boolean follows(IVector2d other) {
        return this.x >= other.getX() && this.y >= other.getY();
    }
}
