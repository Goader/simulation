package pl.edu.agh.cs.app.simulation.geometry;

public class Vector2dBound extends AbstractVector2d {
    private final int xBound;
    private final int yBound;

    public Vector2dBound(int x, int y, int xBound, int yBound) {
        super(mod(x, xBound), mod(y, yBound));
        this.xBound = xBound;
        this.yBound = yBound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IVector2d vector2d = (IVector2d) o;
        return x == vector2d.getX() &&
                y == vector2d.getY();
    }

    public int getXBound() {
        return xBound;
    }

    public int getYBound() {
        return yBound;
    }

    private static int mod(int dividend, int divisor) {
        return ((dividend % divisor) + divisor) % divisor;
    }

    @Override
    public Vector2dBound upperRight(IVector2d other) {
        return new Vector2dBound(Math.min(Math.max(this.x, other.getX()), xBound - 1),
                                Math.min(Math.max(this.y, other.getY()), yBound - 1),
                                xBound, yBound);
    }

    @Override
    public Vector2dBound lowerLeft(IVector2d other) {
        return new Vector2dBound(Math.max(Math.min(this.x, other.getX()), 0),
                                Math.max(Math.min(this.y, other.getY()), 0),
                                xBound, yBound);
    }

    @Override
    public Vector2dBound add(IVector2d other) {
        return new Vector2dBound(mod((this.x + other.getX()), xBound), mod((this.y + other.getY()), yBound),
                                xBound, yBound);
    }

    @Override
    public Vector2dBound subtract(IVector2d other) {
        return new Vector2dBound(mod((this.x - other.getX()), xBound), mod((this.y - other.getY()), yBound),
                                xBound, yBound);
    }

    @Override
    public Vector2dBound opposite() {
        return new Vector2dBound(mod(-this.x, xBound), mod(-this.y, yBound), xBound, yBound);
    }
}
