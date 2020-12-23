package pl.edu.agh.cs.app.simulation.maps;


import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.app.simulation.Engine;
import pl.edu.agh.cs.app.simulation.cells.JungleMapCell;
import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.IJungleMapElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dBound;
import pl.edu.agh.cs.app.simulation.geometry.Vector2dEucl;
import pl.edu.agh.cs.app.simulation.utils.MapOrientation;

import java.util.*;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
        } else {
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
        } else {
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
        JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map = new JungleMap<>(10, 10, 4, 4);
        Engine engine = new Engine(map);
        Vector2dBound vec1 = new Vector2dBound(2, 3, 10, 10);
        Vector2dBound vec2 = new Vector2dBound(4, 5, 10, 10);
        Vector2dBound vec3 = new Vector2dBound(9, 1, 10, 10);

        Plant plant11 = new Plant(vec1, 4);
        Plant plant12 = new Plant(vec1, 5);
        Plant plant13 = new Plant(vec1, 11);

        Plant plant21 = new Plant(vec2, 5);
        Plant plant22 = new Plant(vec2, 4);
        Plant plant23 = new Plant(vec2, 111);
        Plant plant24 = new Plant(vec2, 1);

        Plant plant31 = new Plant(vec3, 10000);
        Plant plant32 = new Plant(vec3, 14);

        Animal animal11 = new Animal(vec1, 10, 0, genotype, map);
        Animal animal12 = new Animal(vec1, 10, 0, genotype, map);
        Animal animal13 = new Animal(vec1, 13, 0, genotype, map);
        Animal animal14 = new Animal(vec1, 4, 0, genotype, map);

        Animal animal21 = new Animal(vec2, 10, 0, genotype, map);
        Animal animal22 = new Animal(vec2, 15, 0, genotype, map);
        Animal animal23 = new Animal(vec2, 4, 0, genotype, map);

        Animal animal31 = new Animal(vec3, 10, 0, genotype, map);
        Animal animal32 = new Animal(vec3, 10, 0, genotype, map);
        Animal animal33 = new Animal(vec3, 4, 0, genotype, map);

        LinkedList<Animal> activeAnimals = new LinkedList<>(Arrays.asList(animal11, animal12, animal14, animal21, animal23, animal31, animal32, animal33));

        // first day
        map.place(plant11);
        map.place(plant21);
        map.place(plant31);

        for (Animal anim : activeAnimals) map.place(anim);

        engine.feed();

        assertEquals(animal11.getEnergy(), 12);
        assertEquals(animal12.getEnergy(), 12);
        assertEquals(animal14.getEnergy(), 4);

        assertEquals(animal21.getEnergy(), 15);
        assertEquals(animal23.getEnergy(), 4);

        assertEquals(animal31.getEnergy(), 5010);
        assertEquals(animal32.getEnergy(), 5010);
        assertEquals(animal33.getEnergy(), 4);


        // second day:   making cycle-moves to have the opportunity for the new plants to be placed
        for (Animal anim : activeAnimals) anim.move();
        map.place(plant12);
        map.place(plant22);
        for (int i = 0; i < 9; i++) for (Animal anim : activeAnimals) anim.move();
        map.place(animal13);
        map.place(animal22);
        activeAnimals.addAll(Arrays.asList(animal13, animal22));

        engine.feed();

        assertEquals(animal11.getEnergy(), 12);
        assertEquals(animal12.getEnergy(), 12);
        assertEquals(animal13.getEnergy(), 18);

        assertEquals(animal21.getEnergy(), 17);
        assertEquals(animal22.getEnergy(), 17);

        assertEquals(animal31.getEnergy(), 5010);
        assertEquals(animal32.getEnergy(), 5010);

        // third day
        for (Animal anim : activeAnimals) anim.move();
        map.place(plant23);
        for (int i = 0; i < 9; i++) for (Animal anim : activeAnimals) anim.move();

        engine.feed();

        assertEquals(animal11.getEnergy(), 12);
        assertEquals(animal12.getEnergy(), 12);
        assertEquals(animal13.getEnergy(), 18);

        if (animal21.getEnergy() > animal22.getEnergy()) {
            assertEquals(animal21.getEnergy(), 73);
            assertEquals(animal22.getEnergy(), 72);
        } else {
            assertEquals(animal21.getEnergy(), 72);
            assertEquals(animal22.getEnergy(), 73);
        }

        assertEquals(animal31.getEnergy(), 5010);
        assertEquals(animal32.getEnergy(), 5010);

        // fourth day
        for (Animal anim : activeAnimals) anim.move();
        map.place(plant13);
        map.place(plant24);
        map.place(plant32);
        for (int i = 0; i < 9; i++) for (Animal anim : activeAnimals) anim.move();

        engine.feed();

        assertEquals(animal11.getEnergy(), 12);
        assertEquals(animal12.getEnergy(), 12);
        assertEquals(animal13.getEnergy(), 29);
        assertEquals(animal14.getEnergy(), 4);

        if (animal21.getEnergy() > animal22.getEnergy()) {
            assertEquals(animal21.getEnergy(), 74);
            assertEquals(animal22.getEnergy(), 72);
        } else {
            assertEquals(animal21.getEnergy(), 72);
            assertEquals(animal22.getEnergy(), 74);
        }
        assertEquals(animal23.getEnergy(), 4);

        assertEquals(animal31.getEnergy(), 5017);
        assertEquals(animal32.getEnergy(), 5017);
        assertEquals(animal33.getEnergy(), 4);
    }

    @Test
    void bringTogetherTest() {
        JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map = new JungleMap<>(10, 10, 4, 4);
        Engine engine = new Engine(map);
        Vector2dBound vec1 = new Vector2dBound(2, 3, 10, 10);
        Vector2dBound vec2 = new Vector2dBound(5, 5, 10, 10);
        Vector2dBound vec3 = new Vector2dBound(9, 1, 10, 10);

        Plant plant = new Plant(vec1, 0);

        Animal animal11 = new Animal(vec1, 30, 0, genotype, map);
        animal11.eat(plant, -17);
        Animal animal12 = new Animal(vec1, 30, 0, genotype, map);
        Animal animal13 = new Animal(vec1, 13, 0, genotype, map);
        Animal animal14 = new Animal(vec1, 4, 0, genotype, map);

        Animal animal21 = new Animal(vec2, 10, 0, genotype, map);
        Animal animal22 = new Animal(vec2, 15, 0, genotype, map);
        Animal animal23 = new Animal(vec2, 4, 0, genotype, map);

        Animal animal31 = new Animal(vec3, 10, 0, genotype, map);
        Animal animal32 = new Animal(vec3, 10, 0, genotype, map);
        Animal animal33 = new Animal(vec3, 4, 0, genotype, map);

        Animal b11 = new Animal(new Vector2dBound(2, 2, 10, 10), 10, 0, null, map);
        Animal b12 = new Animal(new Vector2dBound(2, 4, 10, 10), 10, 0, null, map);
        Animal b13 = new Animal(new Vector2dBound(1, 2, 10, 10), 10, 0, null, map);
        Animal b14 = new Animal(new Vector2dBound(1, 3, 10, 10), 10, 0, null, map);
        Animal b15 = new Animal(new Vector2dBound(1, 4, 10, 10), 10, 0, null, map);
        Animal b16 = new Animal(new Vector2dBound(3, 2, 10, 10), 10, 0, null, map);
        Animal b17 = new Animal(new Vector2dBound(3, 4, 10, 10), 10, 0, null, map);
        // (3, 3) left

        Animal b21 = new Animal(new Vector2dBound(4, 4, 10, 10), 10, 0, null, map);
        Animal b22 = new Animal(new Vector2dBound(5, 4, 10, 10), 10, 0, null, map);
        Animal b23 = new Animal(new Vector2dBound(6, 4, 10, 10), 10, 0, null, map);
        Animal b24 = new Animal(new Vector2dBound(4, 5, 10, 10), 10, 0, null, map);
        Animal b25 = new Animal(new Vector2dBound(6, 5, 10, 10), 10, 0, null, map);
        Animal b26 = new Animal(new Vector2dBound(4, 6, 10, 10), 10, 0, null, map);
        Animal b27 = new Animal(new Vector2dBound(6, 6, 10, 10), 10, 0, null, map);
        // (5, 6) left

        Animal b31 = new Animal(new Vector2dBound(8, 0, 10, 10), 10, 0, null, map);
        Animal b32 = new Animal(new Vector2dBound(9, 0, 10, 10), 10, 0, null, map);
        Animal b33 = new Animal(new Vector2dBound(0, 0, 10, 10), 10, 0, null, map);
        Animal b34 = new Animal(new Vector2dBound(8, 1, 10, 10), 10, 0, null, map);
        Animal b35 = new Animal(new Vector2dBound(0, 1, 10, 10), 10, 0, null, map);
        Animal b36 = new Animal(new Vector2dBound(8, 2, 10, 10), 10, 0, null, map);
        Animal b37 = new Animal(new Vector2dBound(9, 2, 10, 10), 10, 0, null, map);
        // (0, 2) left

        LinkedList<Animal> activeAnimals = new LinkedList<>(Arrays.asList(animal11, animal12, animal13, animal14,
                animal21, animal22, animal23, animal31, animal32, animal33));
        LinkedList<Animal> boundsAnimals = new LinkedList<>(Arrays.asList(b11, b12, b13, b14, b15, b16, b17,
                b21, b22, b23, b24, b25, b26, b27, b31, b32, b33, b34, b35, b36, b37));

        for (Animal anim : activeAnimals) map.place(anim);
        for (Animal anim : boundsAnimals) map.place(anim);

        Vector2dBound vec1t = new Vector2dBound(3, 3, 10, 10);
        Vector2dBound vec2t = new Vector2dBound(5, 6, 10, 10);
        Vector2dBound vec3t = new Vector2dBound(0, 2, 10, 10);

        assertTrue(map.getCell(vec1t).isEmpty());
        assertTrue(map.getCell(vec2t).isEmpty());
        assertTrue(map.getCell(vec3t).isEmpty());

        engine.bringTogether();

        assertTrue(map.containsCellAt(vec1t));
        assertTrue(map.containsCellAt(vec2t));
        assertTrue(map.containsCellAt(vec3t));

        JungleMapCell cell1t = map.getCell(vec1t).get();
        JungleMapCell cell2t = map.getCell(vec2t).get();
        JungleMapCell cell3t = map.getCell(vec3t).get();

        assertEquals(cell1t.movableElementsCount(), 1);
        assertEquals(cell2t.movableElementsCount(), 1);
        assertEquals(cell3t.movableElementsCount(), 1);

        Animal child1 = (Animal) cell1t.getMaxEnergyElement();
        Animal child2 = (Animal) cell2t.getMaxEnergyElement();
        Animal child3 = (Animal) cell3t.getMaxEnergyElement();

        assertEquals(child1.getEnergy(), 10);
        assertEquals(child2.getEnergy(), 5);
        assertEquals(child3.getEnergy(), 4);

        assertEquals(animal11.getEnergy(), 13);
        assertEquals(animal12.getEnergy(), 23);
        assertEquals(animal13.getEnergy(), 10);
        assertEquals(animal14.getEnergy(), 4);

        assertEquals(animal21.getEnergy(), 8);
        assertEquals(animal22.getEnergy(), 12);
        assertEquals(animal23.getEnergy(), 4);

        assertEquals(animal31.getEnergy(), 8);
        assertEquals(animal32.getEnergy(), 8);
        assertEquals(animal33.getEnergy(), 4);
    }

    @Test
    void energyReductionByMovesTest() {
        JungleMap<JungleMapCell, IJungleMapElement, Plant, Animal> map = new JungleMap<>(10, 10, 4, 4);
        Animal animal = new Animal(new Vector2dBound(4, 4, 10, 10), 160, 44, genotype, map);

        map.place(animal);

        animal.rotate();
        animal.move();

        assertEquals(animal.getEnergy(), 116);

        animal.move();

        assertEquals(animal.getEnergy(), 72);

        animal.rotate();
        animal.move();

        assertEquals(animal.getEnergy(), 28);

        animal.rotate();

        assertEquals(animal.getEnergy(), 28);

        animal.move();

        assertEquals(animal.getEnergy(), -16);
    }

    @Test
    void childMovingTest() {
        JungleMap<JungleMapCell<IJungleMapElement, Plant, Animal>, IJungleMapElement, Plant, Animal> map = new JungleMap<>(10, 10, 4, 4);
        Engine engine = new Engine(map);
        Vector2dBound vec = new Vector2dBound(2, 3, 10, 10);
        Animal animal11 = new Animal(vec, 30, 0, genotype, map);
        Animal animal12 = new Animal(vec, 30, 0, genotype, map);

        Animal b11 = new Animal(new Vector2dBound(2, 2, 10, 10), 10, 0, null, map);
        Animal b12 = new Animal(new Vector2dBound(2, 4, 10, 10), 10, 0, null, map);
        Animal b13 = new Animal(new Vector2dBound(1, 2, 10, 10), 10, 0, null, map);
        Animal b14 = new Animal(new Vector2dBound(1, 3, 10, 10), 10, 0, null, map);
        Animal b15 = new Animal(new Vector2dBound(1, 4, 10, 10), 10, 0, null, map);
        Animal b16 = new Animal(new Vector2dBound(3, 2, 10, 10), 10, 0, null, map);
        Animal b17 = new Animal(new Vector2dBound(3, 4, 10, 10), 10, 0, null, map);
        // (3, 3) left

        LinkedList<Animal> boundsAnimals = new LinkedList<>(Arrays.asList(b11, b12, b13, b14, b15, b16, b17));
        for (Animal anim : boundsAnimals) map.place(anim);

        map.place(animal11);
        map.place(animal12);

        engine.bringTogether();

        Vector2dBound vector = new Vector2dBound(3, 3, 10, 10);

        Plant plant = new Plant(vector, 100);

        assertTrue(map.getCell(vector).isPresent());
        assertEquals(map.getCell(vector).get().movableElementsCount(), 1);
        assertEquals(map.getCell(vector).get().getMaxEnergyElement().getEnergy(), 14);

        Animal child = map.getCell(vector).get().getMaxEnergyElement();
        child.eat(plant, plant.getEnergy());

        child.rotate();

        MapOrientation orient = child.getOrientation();

        child.move();

        Vector2dBound newPosition = vector.add(orient.toUnitVector());

        assertEquals(child.getPosition(), newPosition);
        assertTrue(map.getCell(newPosition).isPresent());
        assertEquals(map.getCell(newPosition).get().getMaxEnergyElement(), child);

        child.move();
        child.move();
        child.move();
        child.move();

        Vector2dEucl inc = orient.toUnitVector();
        newPosition = newPosition.add(inc).add(inc).add(inc).add(inc);

        assertEquals(child.getPosition(), newPosition);
        assertTrue(map.getCell(newPosition).isPresent());
        assertEquals(map.getCell(newPosition).get().getMaxEnergyElement(), child);
    }
}