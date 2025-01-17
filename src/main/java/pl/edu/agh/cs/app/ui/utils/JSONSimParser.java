package pl.edu.agh.cs.app.ui.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.edu.agh.cs.app.simulation.utils.SimulationConfiguration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JSONSimParser {
    public static ArrayList<SimulationConfiguration> parse() {
        JSONParser parser = new JSONParser();
        ArrayList<SimulationConfiguration> configs = null;

        try (FileReader reader = new FileReader(JSONSimParser.class.getResource("/parameters.json").getPath())) {
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
        for (Object simulationEntry : array) {
            simulations.add(parseSimulationConfiguration((JSONObject) simulationEntry));
        }
        return simulations;
    }

    protected static SimulationConfiguration parseSimulationConfiguration(JSONObject sim) {
        int width = Math.toIntExact((Long) sim.get("width"));
        int height = Math.toIntExact((Long) sim.get("height"));
        double jungleRatio = (double) sim.get("jungleRatio");
        int startEnergy = Math.toIntExact((Long) sim.get("startEnergy"));
        int moveEnergy = Math.toIntExact((Long) sim.get("moveEnergy"));
        int plantEnergy = Math.toIntExact((Long) sim.get("plantEnergy"));
        int animalCount = Math.toIntExact((Long) sim.get("animalCount"));

        return new SimulationConfiguration(width, height, jungleRatio,
                startEnergy, moveEnergy, plantEnergy, animalCount);
    }
}
