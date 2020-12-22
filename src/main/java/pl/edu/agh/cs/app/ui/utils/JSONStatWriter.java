package pl.edu.agh.cs.app.ui.utils;

import javafx.scene.control.Alert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class JSONStatWriter {
    public static void write(float plantsCount,
                             float animalsCount,
                             float averageEnergy,
                             float averageLifetime,
                             float averageKidsCount,
                             ArrayList<Float> averageGenotype) {
        JSONObject stats = new JSONObject();
        stats.put("Average plants count", plantsCount);
        stats.put("Average animals count", animalsCount);
        stats.put("Average energy", averageEnergy);
        stats.put("Average lifetime", averageLifetime);
        stats.put("Average kids count", averageKidsCount);

        JSONArray genotype = new JSONArray();
        genotype.addAll(averageGenotype);

        stats.put("Average genotype", genotype);

        JSONArray statsArray = null;

        try (FileReader reader = new FileReader(JSONSimParser.class.getResource("/statistics.json").getPath())) {
            Object parsed = new JSONParser().parse(reader);
            statsArray = (JSONArray) parsed;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-55);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.exit(-56);
        }

        if (statsArray == null) {
            statsArray = new JSONArray();
        }
        statsArray.add(stats);

        try (FileWriter writer = new FileWriter(JSONSimParser.class.getResource("/statistics.json").getPath(), false)) {
            statsArray.writeJSONString(writer);
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-55);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-56);
        }

        Alert msg = new Alert(Alert.AlertType.INFORMATION);
        msg.setTitle("JSON written");
        msg.setHeaderText("JSON file containing statistics has been written to the build/resources");
        msg.show();
    }
}
