package pl.edu.agh.cs.app.ui.panes;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import pl.edu.agh.cs.app.simulation.Simulation;
import pl.edu.agh.cs.app.simulation.data.Genotype;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.IStarveObserver;
import pl.edu.agh.cs.app.simulation.statistics.FamilyLinker;
import pl.edu.agh.cs.app.ui.utils.SimulationStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public class IndividualStatisticsPane extends VBox implements IStarveObserver<Animal> {
    protected FamilyLinker family;
    protected SimulationStatus status;
    protected Animal lastAnimal;

    public IndividualStatisticsPane(FamilyLinker family, SimulationStatus status) {
        this.family = family;
        this.status = status;
        lastAnimal = null;
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

    protected LinkedList<XYChart.Data<Integer, Integer>> convertPairsToData(LinkedList<Pair<Integer, Integer>> data) {
        LinkedList<XYChart.Data<Integer, Integer>> convertedData = new LinkedList<>();
        for (Pair<Integer, Integer> pair : data) {
            convertedData.add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
        }
        return convertedData;
    }

    public void update(Animal animal) {
        this.getChildren().clear();
        this.getChildren().add(getGenotypeBox(animal.getGenotype()));

        if (lastAnimal != null) {
            lastAnimal.removeStarveObserver(this);
            lastAnimal.getView().setInFocus(false);
        }
        animal.getView().setInFocus(true);
        animal.addStarveObserver(this);
        lastAnimal = animal;

        generatePlotStage(convertPairsToData(family.getDirectKids(animal)),
                convertPairsToData(family.getOffspring(animal))).show();
    }

    protected LineChart generateLineChart(LinkedList<XYChart.Data<Integer, Integer>> data,
                                          String xLabel, String yLabel, String plotName) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data list passed to the plot constructing function cannot be empty");
        }

        int minX = data.stream().min(Comparator.comparingInt(XYChart.Data::getXValue)).get().getXValue();
        int maxX = data.stream().max(Comparator.comparingInt(XYChart.Data::getXValue)).get().getXValue();
        NumberAxis xAxis = new NumberAxis(minX, maxX + 10, Math.round((maxX - minX) / 15.0));
        xAxis.setLabel(xLabel);

        int maxY = data.stream().max(Comparator.comparingInt(XYChart.Data::getYValue)).get().getYValue();
        NumberAxis yAxis = new NumberAxis(0, Math.ceil(maxY * (10.0 / 9)),
                Math.max(1, Math.round((maxY * (10.0 / 9)) / 15.0)));
        yAxis.setLabel(yLabel);

        LineChart linechart = new LineChart(xAxis, yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName(plotName);

        series.getData().addAll(data);

        linechart.getData().add(series);

        return linechart;
    }

    protected Stage generatePlotStage(LinkedList<XYChart.Data<Integer, Integer>> directKids,
                                      LinkedList<XYChart.Data<Integer, Integer>> wholeOffspring) {
        VBox plots = new VBox(40,
                generateLineChart(directKids, "Days passed", "Direct kids count",
                        "Direct kids after n day passed"),
                generateLineChart(wholeOffspring, "Days passed", "Whole offspring count",
                        "Whole offspring count after n day passed"));

        Scene scene = new Scene(plots,600, 840);

        Stage stage = new Stage();
        stage.setTitle("Offspring plots");
        stage.setScene(scene);

        return stage;
    }

    @Override
    public void starved(Animal starvedElement, IVector2d position) {
        this.getChildren().add(new Label("Death day: " + status.getDay()));
        starvedElement.removeStarveObserver(this);
        lastAnimal.getView().setInFocus(false);
        lastAnimal = null;
    }
}
