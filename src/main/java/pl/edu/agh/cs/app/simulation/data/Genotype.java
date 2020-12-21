package pl.edu.agh.cs.app.simulation.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Genotype {
    /*
    We suppose there are 2 representations for the genotype: genes counter and genes list.

    Genes counter is a GENE_TYPES-element list, each index represents its gene type, and value is the count of genes
    of this type.

    Genes list is a GENES_COUNT-element list, where each position is a gene, and value at the position points
    what type of gene it is.
     */

    private final ArrayList<Integer> genesCounter;
    public static final int GENES_COUNT = 32;
    public static final int GENE_TYPES = 8;

    private static void validateCounter(List<Integer> genesCounter) {
        if (genesCounter.size() != GENE_TYPES) {
            throw new IllegalArgumentException("There are exactly " + GENE_TYPES + " different gene types. " +
                    "Genes counter must have the length of " + GENE_TYPES + ".");
        }
        if (genesCounter.stream().mapToInt(Integer::intValue).sum() != GENES_COUNT) {
            throw new IllegalArgumentException("The values inside the genes counter must sum up to " + GENES_COUNT +
                    ", as there must be exactly " + GENES_COUNT + " genes.");
        }
        if (!genesCounter.stream().allMatch(x -> x > 0)) {
            throw new IllegalArgumentException("The values inside the genes counter must be positive.");
        }
    }

    private static void validateList(List<Integer> genesList) {
        // omits checking whether every type is represented with at least 1 gene
        if (genesList.size() != GENES_COUNT) {
            throw new IllegalArgumentException("There are exactly " + GENES_COUNT + " genes. " +
                    "Genes list must have the length of " + GENES_COUNT + ".");
        }
        if (!genesList.stream().allMatch(x -> x >= 0 && x < GENE_TYPES)) {
            throw new IllegalArgumentException("The values inside the genes list must be in the range of " +
                    "[0, " + GENE_TYPES + "].");
        }
    }

    public Genotype(List<Integer> genesCounter) {
        validateCounter(genesCounter);
        this.genesCounter = new ArrayList<>(genesCounter);
    }

    public static ArrayList<Integer> genesCounterToList(List<Integer> genesCounter) {
        validateCounter(genesCounter);
        ArrayList<Integer> genesList = new ArrayList<>(GENES_COUNT);
        while (genesList.size() < GENES_COUNT) genesList.add(0);
        for (int geneType = 0, i = 0; geneType < GENE_TYPES; geneType++) {
            for (int geneCount = 0; geneCount < genesCounter.get(geneType); geneCount++, i++) {
                genesList.set(i, geneType);
            }
        }
        return genesList;
    }

    public static ArrayList<Integer> genesListToCounter(List<Integer> genesList) {
        validateList(genesList);
        ArrayList<Integer> genesCounter = new ArrayList<>(GENE_TYPES);
        while (genesCounter.size() < GENE_TYPES) genesCounter.add(0);
        for (Integer geneType : genesList) {
            genesCounter.set(geneType, genesCounter.get(geneType) + 1);
        }
        return genesCounter;
    }

    private static ArrayList<Integer> findIndicesWhere(List<Integer> list, Predicate<Integer> predicate) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (predicate.test(list.get(i))) {
                indices.add(i);
            }
        }
        return indices;
    }

    // how the groups are selected is not defined
    public static Genotype fromTwoGenotypes(Genotype first, Genotype second) {
        int dividingIndex = (int) (Math.random() * GENES_COUNT);

        List<Integer> firstGenesList = genesCounterToList(first.genesCounter).subList(0, dividingIndex);
        List<Integer> secondGenesList = genesCounterToList(second.genesCounter).subList(dividingIndex, GENES_COUNT);

        firstGenesList.addAll(secondGenesList);

        ArrayList<Integer> childCounter = genesListToCounter(firstGenesList);

        while (!childCounter.stream().allMatch(x -> x > 0)) {
            ArrayList<Integer> indices = findIndicesWhere(childCounter, x -> x > 1);
            int closestZero = childCounter.indexOf(0);
            int randomIndex = indices.get((int) (Math.random() * indices.size()));
            childCounter.set(closestZero, childCounter.get(closestZero) + 1);
            childCounter.set(randomIndex, childCounter.get(randomIndex) - 1);
        }

        validateCounter(childCounter);

        return new Genotype(childCounter);
    }

    public ArrayList<Integer> getGenesCounter() {
        return (ArrayList<Integer>) genesCounter.clone();
    }

    public int getRandomRotation() {
        ArrayList<Integer> genesList = genesCounterToList(genesCounter);
        return genesList.get((int) (Math.random() * genesList.size()));
    }

    public static Genotype generateRandomGenotype() {
        ArrayList<Integer> genesCounter = new ArrayList<>();
        for (int i = 0; i < GENE_TYPES; i++) {
            genesCounter.add(1);
        }

        for (int i = GENE_TYPES; i < GENES_COUNT; i++) {
            int idx = (int) (Math.random() * GENE_TYPES);
            genesCounter.set(idx, genesCounter.get(idx) + 1);
        }
        return new Genotype(genesCounter);
    }
}
