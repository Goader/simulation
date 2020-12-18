package pl.edu.agh.cs.app.ui.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONSimParser {
    public static ArrayList<SimulationConfiguration> parse() {
        JSONParser parser = new JSONParser();
        ArrayList<SimulationConfiguration> configs = null;

        try (FileReader reader = new FileReader("C:\\FILES_IN_USE\\java\\simulation\\src\\main\\resources\\parameters.json")) {
            Object parsed = parser.parse(reader);

            JSONArray sims = (JSONArray) parsed;
            configs = parseJSONArray(sims);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-55);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.exit(-56);
        }
        return configs;
    }

    protected static ArrayList<SimulationConfiguration> parseJSONArray(JSONArray array) {
        ArrayList<SimulationConfiguration> simulations = new ArrayList<>();
        for (Object simulationEntry: array) {
            simulations.add(parseSimulationConfiguration((JSONObject) simulationEntry));
        }
        return simulations;
    }

    protected static SimulationConfiguration parseSimulationConfiguration(JSONObject sim) {
        int width = (int) sim.get("width");
        int height = (int) sim.get("height");
        int jungleRatio = (int) sim.get("jungleRatio");
        int startEnergy = (int) sim.get("startEnergy");
        int moveEnergy = (int) sim.get("moveEnergy");
        int plantEnergy = (int) sim.get("plantEnergy");
        int animalCount = (int) sim.get("animalCount");

        SimulationConfiguration config = new SimulationConfiguration(width, height, jungleRatio,
                startEnergy, moveEnergy, plantEnergy, animalCount);

        return config;
    }
}
