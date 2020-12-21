package pl.edu.agh.cs.app.ui.panes;

import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.statistics.PopulationStatistics;

import java.util.ArrayList;

public class StatisticsViewPane extends VBox {
    protected PopulationStatistics statistics;

    public StatisticsViewPane(int width, int height, PopulationStatistics statistics) {
        this.setMaxSize(width, height);
        this.setMinWidth(width);

        this.statistics = statistics;

        this.getChildren().add(getLabelsBox("Current plants count: ", statistics.plantsCountProperty().asString()));
        this.getChildren().add(getLabelsBox("Current animals count: ", statistics.animalsCountProperty().asString()));
        this.getChildren().add(getLabelsBox("Average animals energy: ", statistics.averageEnergyProperty().asString("%.2f")));
        this.getChildren().add(getGenotypeBox());
        this.getChildren().add(getLabelsBox("Average lifetime: ", statistics.averageLifetimeProperty().asString("%.2f")));
        this.getChildren().add(getLabelsBox("Average kids count: ", statistics.averageKidsCountProperty().asString("%.2f")));
    }

    protected VBox getGenotypeBox() {
        VBox genotypeBox = new VBox(new Label("Average genes count: "));
        ArrayList<HBox> genesBoxes = new ArrayList<>();
        for (Integer i = 0; i < Genotype.GENE_TYPES; i++) {
            genesBoxes.add(i, getLabelsBox(i.toString() + ": ", statistics.averageGenotypeProperties().get(i).asString("%.2f")));
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
