package pl.edu.agh.cs.app.ui.panes;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.statistics.FamilyLinker;

import java.util.ArrayList;

public class IndividualStatisticsPane extends VBox {
    protected FamilyLinker family;

    public IndividualStatisticsPane(FamilyLinker family) {
        this.family = family;
    }

    protected VBox getGenotypeBox(Genotype genotype) {
        VBox genotypeBox = new VBox(new Label("Selected animal's genes: "));
        ArrayList<Label> genesBoxes = new ArrayList<>();
        ArrayList<Integer> genesCounter = genotype.getGenesCounter();
        for (Integer i = 0; i < Genotype.GENE_TYPES; i++) {
            genesBoxes.add(new Label(i.toString() + ": " + genesCounter.get(i).toString()));
        }
        HBox firstLineBox = new HBox(5);
        HBox secondLineBox = new HBox(5);
        firstLineBox.getChildren().addAll(genesBoxes.subList(0, Genotype.GENE_TYPES / 2));
        secondLineBox.getChildren().addAll(genesBoxes.subList(Genotype.GENE_TYPES / 2, genesBoxes.size()));
        genotypeBox.getChildren().addAll(firstLineBox, secondLineBox);
        return genotypeBox;
    }

    public void update(Animal animal) {
        this.getChildren().clear();
        this.getChildren().add(getGenotypeBox(animal.getGenotype()));
    }
}
