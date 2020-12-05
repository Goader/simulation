package pl.edu.agh.cs.app.simulation.utils;

import pl.edu.agh.cs.app.simulation.geometry.Vector2dEucl;

public enum MapOrientation {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "North";
            case NORTHEAST -> "North-east";
            case EAST -> "East";
            case SOUTHEAST -> "South-east";
            case SOUTH -> "South";
            case SOUTHWEST -> "South-west";
            case WEST -> "West";
            case NORTHWEST -> "North-west";
        };
    }

    public MapOrientation rotate(int angle) {
        int current_angle = toInteger(this);
        int new_angle = (current_angle + angle) % MapOrientation.values().length;
        return fromInteger(new_angle);
    }

    public static int toInteger(MapOrientation orientation) {
        return switch (orientation) {
            case NORTH -> 0;
            case NORTHEAST -> 1;
            case EAST -> 2;
            case SOUTHEAST -> 3;
            case SOUTH -> 4;
            case SOUTHWEST -> 5;
            case WEST -> 6;
            case NORTHWEST -> 7;
        };
    }

    public static MapOrientation fromInteger(int orientation) {
        return switch (orientation) {
            case 0 -> NORTH;
            case 1 -> NORTHEAST;
            case 2 -> EAST;
            case 3 -> SOUTHEAST;
            case 4 -> SOUTH;
            case 5 -> SOUTHWEST;
            case 6 -> WEST;
            case 7 -> NORTHWEST;
            default -> throw new IllegalArgumentException("Unexpected value: " + orientation
                    + ". Must be between 0 and 7");
        };
    }

    public Vector2dEucl toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2dEucl(0, 1);
            case NORTHEAST -> new Vector2dEucl(1, 1);
            case EAST -> new Vector2dEucl(1, 0);
            case SOUTHEAST -> new Vector2dEucl(1, -1);
            case SOUTH -> new Vector2dEucl(0, -1);
            case SOUTHWEST -> new Vector2dEucl(-1, -1);
            case WEST -> new Vector2dEucl(-1, 0);
            case NORTHWEST -> new Vector2dEucl(-1, 1);
        };
    }
}
