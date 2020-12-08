package pl.edu.agh.cs.app.simulation.data;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenotypeTest {
    @Test
    void representationTest() {
        List<Integer> genesCounter = Arrays.asList(4, 4, 4, 4, 4, 4, 4, 4);
        List<Integer> genesList = Arrays.asList(0,0,0,0,1,1,1,1,2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7);

        assertEquals(Genotype.genesCounterToList(genesCounter), genesList);
        assertEquals(Genotype.genesListToCounter(genesList), genesCounter);
    }

}