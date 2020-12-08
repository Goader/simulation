package pl.edu.agh.cs.app.simulation.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dBoundTest {
    @Test
    void testEquals() {
        Vector2dBound vec1 = new Vector2dBound(10, -25, 4, 4);
        Vector2dBound vec2 = new Vector2dBound(10, -25, 4, 4);
        Vector2dBound vec3 = new Vector2dBound(2, 3, 4, 4);
        Vector2dBound vec4 = new Vector2dBound(2, 3, 4, 5);

        assertEquals(vec1, vec2);
        assertEquals(vec1, vec3);
        assertEquals(vec3, vec4);  // it depends how we define equality
    }

    @Test
    void precedes() {
        Vector2dBound vec1 = new Vector2dBound(10, 25, 200, 200);
        Vector2dBound vec2 = new Vector2dBound(50, 100, 200, 200);
        Vector2dBound vec3 = new Vector2dBound(10, 45, 200, 200);

        assertTrue(vec1.precedes(vec2));
        assertTrue(vec1.precedes(vec3));
        assertTrue(vec3.precedes(vec2));
        assertTrue(vec3.precedes(vec3));
    }

    @Test
    void follows() {
        Vector2dBound vec1 = new Vector2dBound(10, 25, 200, 200);
        Vector2dBound vec2 = new Vector2dBound(50, 100, 200, 200);
        Vector2dBound vec3 = new Vector2dBound(10, 45, 200, 200);

        assertTrue(vec2.follows(vec1));
        assertTrue(vec3.follows(vec1));
        assertTrue(vec2.follows(vec3));
        assertTrue(vec3.follows(vec3));
    }

    @Test
    void upperRight() {
        Vector2dBound vec1 = new Vector2dBound(10, 20, 25, 25);
        Vector2dBound vec2 = new Vector2dBound(20, 10, 30, 30);
        Vector2dBound vec3 = new Vector2dBound(40, 40, 50, 50);

        assertEquals(vec1.upperRight(vec2), new Vector2dBound(20, 20, 25, 25));
        assertEquals(vec1.upperRight(vec3), new Vector2dBound(24, 24, 25, 25));
        assertEquals(vec3.upperRight(vec2), new Vector2dBound(40, 40, 50, 50));
    }

    @Test
    void lowerLeft() {
        Vector2dBound vec1 = new Vector2dBound(10, 20, 25, 25);
        Vector2dBound vec2 = new Vector2dBound(20, 10, 30, 30);
        Vector2dEucl vec3 = new Vector2dEucl(-10, -1);
        Vector2dEucl vec4 = new Vector2dEucl(-10, 5);

        assertEquals(vec1.lowerLeft(vec2), new Vector2dBound(10, 10, 25, 25));
        assertEquals(vec1.lowerLeft(vec3), new Vector2dBound(0, 0, 25, 25));
        assertEquals(vec2.lowerLeft(vec4), new Vector2dBound(0, 5, 30, 30));
    }

    @Test
    void add() {
        Vector2dEucl vec1 = new Vector2dEucl(-16, 34);
        Vector2dBound vec2 = new Vector2dBound(2, 10, 20, 20);
        Vector2dEucl vec3 = new Vector2dEucl(-14, 25);

        assertEquals(vec2.add(vec1), new Vector2dBound(6, 4, 20, 20));
        assertEquals(vec2.add(vec3), new Vector2dBound(8, 15, 20, 20));
    }

    @Test
    void subtract() {
        Vector2dEucl vec1 = new Vector2dEucl(-16, 34);
        Vector2dBound vec2 = new Vector2dBound(2, 10, 50, 50);
        Vector2dEucl vec3 = new Vector2dEucl(-14, 25);

        assertEquals(vec2.subtract(vec1), new Vector2dBound(18, 26, 50, 50));
        assertEquals(vec2.subtract(vec3), new Vector2dBound(16, 35, 50, 50));
    }

    @Test
    void opposite() {
        Vector2dBound vec1 = new Vector2dBound(16, 34, 50, 50);
        Vector2dBound vec2 = new Vector2dBound(2, 10, 15, 15);
        Vector2dBound vec3 = new Vector2dBound(14, 25, 26, 26);
        Vector2dBound vec4 = new Vector2dBound(0, 0, 100, 100);

        assertEquals(vec1.opposite(), new Vector2dBound(34, 16, 50, 50));
        assertEquals(vec2.opposite(), new Vector2dBound(13, 5, 15, 15));
        assertEquals(vec3.opposite(), new Vector2dBound(12, 1, 26, 26));
        assertEquals(vec4.opposite(), new Vector2dBound(0, 0, 100, 100));
    }
}