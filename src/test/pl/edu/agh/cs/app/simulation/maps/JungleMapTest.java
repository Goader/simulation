package pl.edu.agh.cs.app.simulation.maps;

import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.utils.MapOrientation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JungleMapTest {
    List<Integer> genesCounter = Arrays.asList(4, 4, 4, 4, 4, 4, 4, 4);
    Genotype genotype = new Genotype(genesCounter);

    @Test
    void placeTest() {
        JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map = new JungleMap<>(10, 10, 4, 4);
        Animal animal1 = new Animal(new Vector2dBound(4, 4, 10, 10), 5, 0, null, map);
        Animal animal2 = new Animal(new Vector2dBound(4, 4, 10, 10), 1, 0, null, map);
        Plant plant1 = new Plant(new Vector2dBound(4, 4, 10, 10), 1);
        Plant plant2 = new Plant(new Vector2dBound(4, 4, 10, 10), 1);

        assertTrue(map.place(plant1));
        assertTrue(map.place(animal1));
        assertTrue(map.place(animal2));
        assertFalse(map.place(plant1));
        assertFalse(map.place(plant2));

        Optional<JungleMapCell> optCell = map.getCell(new Vector2dBound(4, 4, 10, 10));
        assertTrue(optCell.isPresent());

        JungleMapCell cell = optCell.get();

        assertTrue(cell.hasMovableElements());
        assertTrue(cell.hasNonMovableElements());

        assertTrue(cell.containsElement(plant1));

        assertTrue(cell.containsElement(animal1));
        assertTrue(cell.containsElement(animal2));

        assertTrue(cell.canMoveTo(animal2));

        assertEquals(cell.getMaxEnergyElement(), animal1);
    }

    @Test
    void movedTest() {
        JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map = new JungleMap<>(10, 10, 4, 4);
        Vector2dBound vec1 = new Vector2dBound(0, 0, 10, 10);
        Vector2dBound vec2 = new Vector2dBound(9, 9, 10, 10);
        Animal animal1 = new Animal(vec1, 5, 0, genotype, map);
        Animal animal2 = new Animal(vec2, 1, 0, genotype, map);

        map.place(animal1);
        map.place(animal2);

        animal1.rotate();
        animal2.rotate();

        MapOrientation orient1 = animal1.getOrientation();
        MapOrientation orient2 = animal2.getOrientation();

        animal1.move();
        animal1.move();
        animal2.move();
        animal2.move();

        animal1.rotate();
        animal2.rotate();

        MapOrientation newOrient1 = animal1.getOrientation();
        MapOrientation newOrient2 = animal2.getOrientation();

        animal1.move();
        animal2.move();

        vec1 = vec1.add(orient1.toUnitVector());
        vec1 = vec1.add(orient1.toUnitVector());
        vec1 = vec1.add(newOrient1.toUnitVector());

        vec2 = vec2.add(orient2.toUnitVector());
        vec2 = vec2.add(orient2.toUnitVector());
        vec2 = vec2.add(newOrient2.toUnitVector());

        assertEquals(animal1.getPosition(), vec1);
        assertEquals(animal2.getPosition(), vec2);

        if (vec1.equals(vec2)) {
            animal2.move();
            vec2.add(newOrient2.toUnitVector());
        }

        assertTrue(map.getCell(vec1).isPresent());
        assertTrue(map.getCell(vec2).isPresent());

        JungleMapCell cell1 = map.getCell(vec1).get();
        JungleMapCell cell2 = map.getCell(vec2).get();

        assertEquals(cell1.getMaxEnergyElement(), animal1);
        assertEquals(cell2.getMaxEnergyElement(), animal2);
    }
}