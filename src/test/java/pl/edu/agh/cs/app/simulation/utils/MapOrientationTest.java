package pl.edu.agh.cs.app.simulation.utils;

import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dEucl;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapOrientationTest {

    @Test
    void rotate() {
        MapOrientation m1 = MapOrientation.NORTH;
        MapOrientation m2 = MapOrientation.SOUTHWEST;
        MapOrientation m3 = MapOrientation.NORTHEAST;
        MapOrientation m4 = MapOrientation.WEST;
        MapOrientation m5 = MapOrientation.SOUTHEAST;

        assertEquals(m1.rotate(3), MapOrientation.SOUTHEAST);
        assertEquals(m2.rotate(15), MapOrientation.SOUTH);
        assertEquals(m3.rotate(100), MapOrientation.SOUTHWEST);
        assertEquals(m4.rotate(0), MapOrientation.WEST);
        assertEquals(m5.rotate(-5), MapOrientation.WEST);
    }

    @Test
    void toInteger() {
        MapOrientation m1 = MapOrientation.NORTH;
        MapOrientation m2 = MapOrientation.SOUTHWEST;
        MapOrientation m3 = MapOrientation.NORTHEAST;
        MapOrientation m4 = MapOrientation.WEST;
        MapOrientation m5 = MapOrientation.SOUTHEAST;

        assertEquals(MapOrientation.toInteger(m1), 0);
        assertEquals(MapOrientation.toInteger(m2), 5);
        assertEquals(MapOrientation.toInteger(m3), 1);
        assertEquals(MapOrientation.toInteger(m4), 6);
        assertEquals(MapOrientation.toInteger(m5), 3);
    }

    @Test
    void fromInteger() {
        MapOrientation m1 = MapOrientation.NORTH;
        MapOrientation m2 = MapOrientation.SOUTHWEST;
        MapOrientation m3 = MapOrientation.NORTHEAST;
        MapOrientation m4 = MapOrientation.WEST;
        MapOrientation m5 = MapOrientation.SOUTHEAST;

        assertEquals(m1, MapOrientation.fromInteger(0));
        assertEquals(m2, MapOrientation.fromInteger(5));
        assertEquals(m3, MapOrientation.fromInteger(1));
        assertEquals(m4, MapOrientation.fromInteger(6));
        assertEquals(m5, MapOrientation.fromInteger(3));
    }

    @Test
    void toUnitVector() {
        MapOrientation m1 = MapOrientation.NORTH;
        MapOrientation m2 = MapOrientation.SOUTHWEST;
        MapOrientation m3 = MapOrientation.NORTHEAST;
        MapOrientation m4 = MapOrientation.WEST;
        MapOrientation m5 = MapOrientation.SOUTHEAST;

        assertEquals(m1.toUnitVector(), new Vector2dEucl(0, 1));
        assertEquals(m2.toUnitVector(), new Vector2dEucl(-1, -1));
        assertEquals(m3.toUnitVector(), new Vector2dEucl(1, 1));
        assertEquals(m4.toUnitVector(), new Vector2dEucl(-1, 0));
        assertEquals(m5.toUnitVector(), new Vector2dEucl(1, -1));
    }
}