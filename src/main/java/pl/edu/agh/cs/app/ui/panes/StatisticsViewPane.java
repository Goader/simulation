package pl.edu.agh.cs.app.ui.panes;

import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.statistics.PopulationStatistics;
import pl.edu.agh.cs.app.ui.utils.SimulationStatus;

import java.util.ArrayList;

public class StatisticsViewPane extends VBox {
    protected PopulationStatistics statistics;
    protected IndividualStatisticsPane individualStatistics;
    protected SimulationStatus status;

    public StatisticsViewPane(int width, int height, PopulationStatistics statistics, SimulationStatus status) {
        this.setMaxSize(width, height);
        this.setMinWidth(width);

        this.status = status;
        this.statistics = statistics;

        this.getChildren().add(getLabelsBox("Current plants count: ", statistics.plantsCountProperty().asString()));
        this.getChildren().add(getLabelsBox("Current animals count: ", statistics.animalsCountProperty().asString()));
        this.getChildren().add(getLabelsBox("Average animals energy: ", statistics.averageEnergyProperty().asString("%.2f")));
        this.getChildren().add(getGenotypeBox());
        this.getChildren().add(getLabelsBox("Dominating gene: ", statistics.dominatingGeneProperty().asString()));
        this.getChildren().add(getLabelsBox("Average lifetime: ", statistics.averageLifetimeProperty().asString("%.2f")));
        this.getChildren().add(getLabelsBox("Average kids count: ", statistics.averageKidsCountProperty().asString("%.2f")));

        Region blankSpace = new Region();
        blankSpace.setMinHeight(20);
        this.getChildren().add(blankSpace);

        individualStatistics = new IndividualStatisticsPane(statistics.getFamily(), status);
        this.getChildren().add(individualStatistics);
    }

    public void drawIndividualStatistics(Animal animal) {
        individualStatistics.update(animal);
    }

    protected VBox getGenotypeBox() {
        VBox genotypeBox = new VBox(new Label("Average genes count: "));
        ArrayList<HBox> genesBoxes = new ArrayList<>();
        for (Integer i = 0; i < Genotype.GENE_TYPES; i++) {
            genesBoxes.add(getLabelsBox(i.toString() + ": ", statistics.averageGenotypeProperties().get(i).asString("%.2f")));
        }
        HBox firstLineBox = new HBox(5);
        HBox secondLineBox = new HBox(5);
        firstLineBox.getChildren().addAll(genesBoxes.subList(0, Genotype.GENE_TYPES / 2));
        secondLineBox.getChildren().addAll(genesBoxes.subList(Genotype.GENE_TYPES / 2, genesBoxes.size()));
        genotypeBox.getChildren().addAll(firstLineBox, secondLineBox);
        return genotypeBox;
    }

    protected static HBox getLabelsBox(String text, StringBinding binding) {
        Label firstLabel = new Label(text);
        Label secondLabel = new Label();
        secondLabel.textProperty().bind(binding);
        return new HBox(0, firstLabel, secondLabel);
    }
}
