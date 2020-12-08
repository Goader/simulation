package pl.edu.agh.cs.app.simulation.maps;


import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dEucl;
import pl.edu.agh.cs.app.simulation.utils.MapOrientation;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.anyOf;

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

    @Test
    void eatTest() {
        JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map = new JungleMap<>(10, 10, 4, 4);
        Vector2dBound vec = new Vector2dBound(4, 4, 10, 10);
        Animal animal1 = new Animal(vec, 5, 0, genotype, map);
        Animal animal2 = new Animal(vec, 6, 0, genotype, map);
        Animal animal3 = new Animal(vec, 6, 0, genotype, map);
        Plant plant = new Plant(vec, 10);

        map.place(plant);
        map.place(animal1);
        map.place(animal2);
        map.place(animal3);

        JungleMapCell cell = map.getCell(vec).get();

        assertEquals(cell.getMaxEnergyElements().size(), 2);
        assertFalse(cell.getMaxEnergyElements().contains(animal1));

        assertTrue(cell.hasNonMovableElements());

        animal1.eat(plant, plant.getEnergy());

        assertEquals(cell.getMaxEnergyElement(), animal1);

        assertFalse(cell.containsElement(plant));
        assertFalse(cell.hasNonMovableElements());

        animal1.eat(plant, plant.getEnergy());

        assertFalse(cell.containsElement(plant));
        assertFalse(cell.hasNonMovableElements());
    }

    @Test
    void starveTest() {
        JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map = new JungleMap<>(10, 10, 4, 4);
        Vector2dBound vec = new Vector2dBound(4, 4, 10, 10);
        Animal animal1 = new Animal(vec, 10, 0, genotype, map);
        Animal animal2 = new Animal(vec, 7, 0, genotype, map);
        Animal animal3 = new Animal(vec, 4, 0, genotype, map);
        Plant plant = new Plant(vec, 10);

        map.place(animal1);
        map.place(animal2);
        map.place(animal3);

        JungleMapCell cell = map.getCell(vec).get();

        assertEquals(cell.movableElementsCount(), 3);

        animal1.starve();
        assertEquals(cell.movableElementsCount(), 3);

        animal1.eat(plant, -10);
        animal1.starve();
        assertEquals(cell.movableElementsCount(), 2);

        animal2.eat(plant, -1000);
        animal2.eat(plant, 2000);
        animal2.starve();
        assertEquals(cell.movableElementsCount(), 2);

        assertEquals(cell.getMaxEnergyElement(), animal2);
        animal2.eat(plant, -10000);
        animal2.starve();
        assertEquals(cell.getMaxEnergyElement(), animal3);
        assertEquals(cell.movableElementsCount(), 1);

        animal3.eat(plant, -100);
        animal3.starve();
        assertTrue(cell.isEmpty());
    }

    @Test
    void breedPairTest() {
        JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map = new JungleMap<>(10, 10, 4, 4);
        Vector2dBound vec = new Vector2dBound(4, 4, 10, 10);
        Plant plant = new Plant(vec, -6);
        Animal animal1 = new Animal(vec, 10, 0, genotype, map);
        Animal animal2 = new Animal(vec, 4, 0, genotype, map);
        Animal animal3 = new Animal(vec, 4, 0, genotype, map);

        map.place(animal1);

        JungleMapCell cell = map.getCell(vec).get();

        assertTrue(cell.getBreedPair().isEmpty());

        map.place(animal2);
        map.place(animal3);

        animal1.eat(plant, -6);

        assertTrue(cell.getBreedPair().isPresent());
        Map.Entry<Animal, Animal> entry = (Map.Entry<Animal, Animal>) cell.getBreedPair().get();
        if (entry.getKey().equals(animal2)) {
            assertEquals(entry.getKey(), animal2);
            assertEquals(entry.getValue(), animal3);
        }
        else {
            assertEquals(entry.getKey(), animal3);
            assertEquals(entry.getValue(), animal2);
        }

        animal3.eat(plant, 1);

        assertTrue(cell.getBreedPair().isPresent());
        entry = (Map.Entry<Animal, Animal>) cell.getBreedPair().get();
        assertEquals(entry.getKey(), animal3);
        assertEquals(entry.getValue(), animal2);

        animal1.eat(plant, 2);
        animal2.eat(plant, 2);

        entry = (Map.Entry<Animal, Animal>) cell.getBreedPair().get();
        if (entry.getKey().equals(animal1)) {
            assertEquals(entry.getKey(), animal1);
            assertEquals(entry.getValue(), animal2);
        }
        else {
            assertEquals(entry.getKey(), animal2);
            assertEquals(entry.getValue(), animal1);
        }

        animal1.eat(plant, -1);
        animal3.eat(plant, -3);

        assertTrue(cell.getBreedPair().isEmpty());

        animal1.eat(plant, 1);

        assertTrue(cell.getBreedPair().isPresent());

        animal1.eat(plant, -100);
        animal1.starve();

        assertTrue(cell.getBreedPair().isEmpty());
    }

    @Test
    void emptyCellsTest() {
        JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map1 = new JungleMap<>(8, 8, 2, 2);
        JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map2 = new JungleMap<>(2, 2, 1, 1);
        Vector2dBound vec11 = new Vector2dBound(3, 3, 8, 8);
        Vector2dBound vec12 = new Vector2dBound(3, 4, 8, 8);
        Vector2dBound vec13 = new Vector2dBound(4, 3, 8, 8);
        Vector2dBound vec14 = new Vector2dBound(4, 4, 8, 8);
        Vector2dBound vec15 = vec14;

        Vector2dBound vec21 = new Vector2dBound(0, 1, 2, 2);
        Vector2dBound vec22 = new Vector2dBound(1, 0, 2, 2);
        Vector2dBound vec23 = new Vector2dBound(1, 1, 2, 2);
        Vector2dBound vec24 = vec23;

        Plant plant11 = new Plant(vec11, 0);
        Plant plant12 = new Plant(vec12, 0);
        Plant plant13 = new Plant(vec13, 0);
        Animal animal14 = new Animal(vec14, 10, 0, genotype, map1);
        Animal animal15 = new Animal(vec15, 10, 0, genotype, map1);

        Plant plant21 = new Plant(vec21, 0);
        Plant plant22 = new Plant(vec22, 0);
        Plant plant23 = new Plant(vec23, 0);
        Animal animal24 = new Animal(vec24, 10, 0, genotype, map2);

        // first map testing
        assertTrue(map1.getRandomEmptyJungleCellPosition().isPresent());
        assertThat(map1.getRandomEmptyJungleCellPosition().get(), anyOf(is(vec11), is(vec12), is(vec13), is(vec14)));

        map1.place(plant11);
        map1.place(plant12);

        assertTrue(map1.getRandomEmptyJungleCellPosition().isPresent());
        assertThat(map1.getRandomEmptyJungleCellPosition().get(), anyOf(is(vec13), is(vec14)));

        map1.place(plant13);
        map1.place(animal14);

        assertTrue(map1.getRandomEmptyJungleCellPosition().isEmpty());

        animal14.move();

        assertTrue(map1.getRandomEmptyJungleCellPosition().isPresent());
        assertEquals(map1.getRandomEmptyJungleCellPosition().get(), vec14);

        map1.place(animal15);

        assertTrue(map1.getRandomEmptyJungleCellPosition().isEmpty());

        animal15.eat(plant11, -100);
        animal15.starve();

        assertTrue(map1.getRandomEmptyJungleCellPosition().isPresent());
        assertEquals(map1.getRandomEmptyJungleCellPosition().get(), vec15);

        // second map testing
        assertTrue(map2.getRandomEmptyNonJungleCellPosition().isPresent());
        assertThat(map2.getRandomEmptyNonJungleCellPosition().get(), anyOf(is(vec21), is(vec22), is(vec23)));

        map2.place(animal24);

        assertTrue(map2.getRandomEmptyNonJungleCellPosition().isPresent());
        assertThat(map2.getRandomEmptyNonJungleCellPosition().get(), anyOf(is(vec21), is(vec22)));

        map2.place(plant21);
        map2.place(plant22);

        animal24.move();

        assertTrue(map2.getRandomEmptyNonJungleCellPosition().isPresent());
        assertEquals(map2.getRandomEmptyNonJungleCellPosition().get(), vec24);

        animal24.move();

        assertTrue(map2.getRandomEmptyNonJungleCellPosition().isEmpty());

        animal24.eat(plant11, -100);
        animal24.starve();

        assertTrue(map2.getRandomEmptyNonJungleCellPosition().isPresent());
        assertEquals(map2.getRandomEmptyNonJungleCellPosition().get(), vec23);

        map2.place(plant23);

        assertTrue(map2.getRandomEmptyNonJungleCellPosition().isEmpty());
    }

    @Test
    void freeNeighbourCellTest() {
        JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map = new JungleMap<>(10, 10, 4, 4);
        Vector2dBound vec1 = new Vector2dBound(1, 0, 10, 10);
        Vector2dBound vec2 = new Vector2dBound(0, 1, 10, 10);
        Vector2dBound vec3 = new Vector2dBound(1, 1, 10, 10);
        Vector2dBound vec4 = new Vector2dBound(9, 9, 10, 10);
        Vector2dBound vec5 = new Vector2dBound(1, 9, 10, 10);
        Vector2dBound vec6 = new Vector2dBound(0, 9, 10, 10);
        Vector2dBound vec7 = new Vector2dBound(9, 0, 10, 10);
        Vector2dBound vec8 = new Vector2dBound(9, 1, 10, 10);
        Plant plant = new Plant(vec4, -6);
        Animal animal1 = new Animal(vec1, 4, 0, genotype, map);
        Animal animal2 = new Animal(vec2, 4, 0, genotype, map);
        Animal animal3 = new Animal(vec3, 4, 0, genotype, map);
        Animal animal4 = new Animal(vec4, 4, 0, genotype, map);
        Animal animal5 = new Animal(vec5, 4, 0, genotype, map);
        Animal animal6 = new Animal(vec6, 4, 0, genotype, map);
        Animal animal7 = new Animal(vec7, 4, 0, genotype, map);
        Animal animal8 = new Animal(vec8, 4, 0, genotype, map);

        map.place(animal1);
        map.place(animal2);
        map.place(animal3);
        map.place(animal5);
        map.place(animal6);
        map.place(animal7);
        map.place(animal8);

        Vector2dBound pos = new Vector2dBound(0, 0, 10, 10);

        assertEquals(map.getFreeNeighbourPosition(pos), vec4);

        map.place(animal4);

        assertThat(map.getFreeNeighbourPosition(pos),
                anyOf(is(vec1), is(vec2), is(vec3), is(vec4), is(vec5), is(vec6), is(vec7), is(vec8)));

        animal7.eat(plant, -100);
        animal7.starve();

        assertEquals(map.getFreeNeighbourPosition(pos), vec7);
    }

    @Test
    void feedTest() {

    }

    @Test
    void bringTogetherTest() {

    }
}