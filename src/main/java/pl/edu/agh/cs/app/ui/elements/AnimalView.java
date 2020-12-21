package pl.edu.agh.cs.app.ui.elements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IEatObserver;
import pl.edu.agh.cs.app.simulation.observers.IEnergyChangeObserver;

import java.io.InputStream;

public class AnimalView extends ImageView implements IEnergyChangeObserver<Animal>, IEatObserver<Animal, Plant>, IBreedObserver<Animal> {
    protected InputStream critical;
    protected InputStream low;
    protected InputStream medium;
    protected InputStream high;

    protected Image criticalEnergyIcon;
    protected Image lowEnergyIcon;
    protected Image mediumEnergyIcon;
    protected Image highEnergyIcon;

    protected final Animal animal;

    protected int lastEnergy;

    protected int imageSideSize = 20;

    public AnimalView(Animal animal) {
        super();
        critical = AnimalView.class.getResourceAsStream("/paw_critical.png");
        low = AnimalView.class.getResourceAsStream("/paw_low.png");
        medium = AnimalView.class.getResourceAsStream("/paw_med.png");
        high = AnimalView.class.getResourceAsStream("/paw_high.png");

        // needs change, we wanna know width and height from arguments
        criticalEnergyIcon = new Image(critical, imageSideSize, imageSideSize, true, true);
        lowEnergyIcon = new Image(low, imageSideSize, imageSideSize, true, true);
        mediumEnergyIcon = new Image(medium, imageSideSize, imageSideSize, true, true);
        highEnergyIcon = new Image(high, imageSideSize, imageSideSize, true, true);

        this.animal = animal;
        this.lastEnergy = animal.getEnergy();
        this.setImage(getImage(animal));
    }

    protected Image getImage(Animal animal) {
        int energy = animal.getEnergy();
        int moveCost = animal.getMoveEnergyCost();

        if (energy < 2 * moveCost) {
            return criticalEnergyIcon;
        }
        if (energy < 5 * moveCost) {
            return lowEnergyIcon;
        }
        if (energy < 10 * moveCost) {
            return mediumEnergyIcon;
        }
        return highEnergyIcon;
    }

    public int getImageSideSize() {
        return imageSideSize;
    }

    public void setImageSideSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be a positive integer");
        }
        imageSideSize = size;

        critical = AnimalView.class.getResourceAsStream("/paw_critical.png");
        low = AnimalView.class.getResourceAsStream("/paw_low.png");
        medium = AnimalView.class.getResourceAsStream("/paw_med.png");
        high = AnimalView.class.getResourceAsStream("/paw_high.png");

        criticalEnergyIcon = new Image(critical, size, size, true, true);
        lowEnergyIcon = new Image(low, size, size, true, true);
        mediumEnergyIcon = new Image(medium, size, size, true, true);
        highEnergyIcon = new Image(high, size, size, true, true);

        this.lastEnergy = animal.getEnergy();
        this.setImage(getImage(animal));
    }

    public void update() {
        this.setImage(getImage(this.animal));
    }

    @Override
    public void bred(Animal firstElement, Animal secondElement, Animal newElement, int firstEnergyBefore, int secondEnergyBefore, IVector2d position) {
        newElement.getView().setImageSideSize(imageSideSize);
        update();
    }

    @Override
    public void ate(Animal ateElement, Plant eatenElement, int eatenEnergy, IVector2d position) {
        update();
    }

    @Override
    public void energyChanged(Animal element, int energyBefore, int energyAfter) {
        update();
    }
}
