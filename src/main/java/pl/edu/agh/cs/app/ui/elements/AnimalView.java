package pl.edu.agh.cs.app.ui.elements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.AbstractJungleMapNonMovableElement;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Animal;
import pl.edu.agh.cs.app.simulation.entities.mirrormap.junglemap.Plant;
import pl.edu.agh.cs.app.simulation.geometry.IVector2d;
import pl.edu.agh.cs.app.simulation.observers.IBreedObserver;
import pl.edu.agh.cs.app.simulation.observers.IEatObserver;
import pl.edu.agh.cs.app.simulation.observers.IEnergyChangeObserver;
import pl.edu.agh.cs.app.simulation.observers.IMoveObserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AnimalView extends ImageView implements IEnergyChangeObserver<Animal>, IEatObserver<Animal, Plant>, IBreedObserver<Animal> {
    // wanted to make them static, but there appears problem with initializing
    protected final Image criticalEnergyIcon;
    protected final Image lowEnergyIcon;
    protected final Image mediumEnergyIcon;
    protected final Image highEnergyIcon;

    protected final Animal animal;

    protected int lastEnergy;

    public AnimalView(Animal animal) {
        super();
        InputStream critical = InputStream.nullInputStream();
        InputStream low = InputStream.nullInputStream();
        InputStream medium = InputStream.nullInputStream();
        InputStream high = InputStream.nullInputStream();
        try {
            critical = new FileInputStream("C:\\FILES_IN_USE\\java\\simulation\\src\\main\\resources\\paw_critical.png");
            low = new FileInputStream("C:\\FILES_IN_USE\\java\\simulation\\src\\main\\resources\\paw_low.png");
            medium = new FileInputStream("C:\\FILES_IN_USE\\java\\simulation\\src\\main\\resources\\paw_med.png");
            high = new FileInputStream("C:\\FILES_IN_USE\\java\\simulation\\src\\main\\resources\\paw_high.png");
        } catch (FileNotFoundException ex) {
            System.exit(-54);  // couldn't find a better way to handle it
            // of course, we could have used some default textures from JavaFX, but it's not beautiful :)
        }
        criticalEnergyIcon = new Image(critical);
        lowEnergyIcon = new Image(low);
        mediumEnergyIcon = new Image(medium);
        highEnergyIcon = new Image(high);

        this.animal = animal;
        this.lastEnergy = animal.getEnergy();
        this.setImage(getImage(animal));
    }

    public Image getImage(Animal animal) {
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

    public void update() {
        this.setImage(getImage(this.animal));
    }

    @Override
    public void bred(Animal firstElement, Animal secondElement, Animal newElement, int firstEnergyBefore, int secondEnergyBefore, IVector2d position) {
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
