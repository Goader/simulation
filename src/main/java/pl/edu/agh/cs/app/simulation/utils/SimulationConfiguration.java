package pl.edu.agh.cs.app.simulation.utils;

public class SimulationConfiguration {
    protected final int width;
    protected final int height;
    protected final int jungleWidth;
    protected final int jungleHeight;
    protected final int startEnergy;
    protected final int moveEnergy;
    protected final int plantEnergy;
    protected final int animalCount;

    public SimulationConfiguration(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy, int animalCount) {
        this.width = width;
        this.height = height;

        this.jungleWidth = (int) (jungleRatio * width);
        this.jungleHeight = (int) (jungleRatio * height);

        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.animalCount = animalCount;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getJungleWidth() {
        return jungleWidth;
    }

    public int getJungleHeight() {
        return jungleHeight;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public int getAnimalCount() {
        return animalCount;
    }


}
