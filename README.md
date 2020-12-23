# Darwin Evolution Simulation
 The project for the Object Oriented Programming Course at AGH UST

## :gear: Requirements and building

```git
git clone https://github.com/Goader/simulation.git
```

* JDK 15
* Gradle 15
* Dependencies from [build.gradle](https://github.com/Goader/simulation/blob/main/build.gradle) 
* Specify input configuration in [parameters.json](/src/main/resources/parameters.json)

**Using IntelliJ is strongly recommended**

_To run the application use **`Gradle` -> `Tasks` -> `Application`-> `run`**._


## :pencil2: Application features

### :information_source: Marks

Mark | Definition
----------- | -------------
![Savanna Field](/src/main/resources/) | Savanna field
![Jungle Field](/src/main/resources/) | Jungle field
![Plant](/src/main/resources/) | Plant
![High Energy Animal](/src/main/resources/) | High energy animal
![Medium Energy Animal](/src/main/resources/) | Medium energy animal
![Low Energy Animal](/src/main/resources/) | Low energy animal
![Critical Energy Animal](/src/main/resources/) | Critical energy animal
![Currently Observed Animal](/src/main/resources/) | Currently observed animal
![Dominant Gene Animal](/src/main/resources/) | Animal with dominant gene

### :joystick: Controls

* `Start` - runs the simulation
* `Pause` - stops the simulation (still can be run again)
* `Stop` - absolutely stops the simulation (cannot be run again)
* `Write JSON` - creates a JSON file containing average statistics for the whole time
* `Show animals with dominant gene` - animals with the dominant gene will be highlighted 
* `Slider` - indicates the time in ms of refreshing the simulation (ex. 100ms means 10 days are passed per second)

### :film_strip: Usage example

![Usage example](readme_example.gif)

## :keyboard: Created by @Goader
