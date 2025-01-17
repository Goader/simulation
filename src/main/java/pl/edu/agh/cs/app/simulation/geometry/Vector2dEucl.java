package pl.edu.agh.cs.app.simulation.geometry;

public class Vector2dEucl extends AbstractVector2d {
    public Vector2dEucl(int x, int y) {
        super(x, y);
    }

    @Override
    public Vector2dEucl upperRight(IVector2d other) {
        return new Vector2dEucl(Math.max(this.x, other.getX()), Math.max(this.y, other.getY()));
    }

    @Override
    public Vector2dEucl lowerLeft(IVector2d other) {
        return new Vector2dEucl(Math.min(this.x, other.getX()), Math.min(this.y, other.getY()));
    }

    @Override
    public Vector2dEucl add(IVector2d other) {
        return new Vector2dEucl(this.x + other.getX(), this.y + other.getY());
    }

    @Override
    public Vector2dEucl subtract(IVector2d other) {
        return new Vector2dEucl(this.x - other.getX(), this.y - other.getY());
    }

    @Override
    public Vector2dEucl opposite() {
        return new Vector2dEucl(-this.x, -this.y);
    }
}
