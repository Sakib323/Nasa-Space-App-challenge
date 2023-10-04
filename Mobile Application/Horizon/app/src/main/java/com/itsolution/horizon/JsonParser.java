package com.itsolution.horizon;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    public static HashMap<String, HashMap<String, String>> parseJson(String jsonString) {
        HashMap<String, HashMap<String, String>> dataMap = new HashMap<>();

        try {
            Log.e("json", jsonString);

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
            JsonArray features = jsonObject.getAsJsonArray("features");

            for (JsonElement featureElement : features) {
                JsonObject feature = featureElement.getAsJsonObject();
                JsonObject properties = feature.getAsJsonObject("properties");
                JsonObject geometry = feature.getAsJsonObject("geometry");

                String id = properties.get("id").getAsString();
                String title = properties.get("title").getAsString();
                String description = properties.get("description").isJsonNull() ? "" : properties.get("description").getAsString();
                String link = properties.get("link").getAsString();
                String date = properties.get("date").getAsString();
                String magnitudeValue = properties.get("magnitudeValue").getAsString();
                String magnitudeUnit = properties.get("magnitudeUnit").getAsString();

                // Categories
                JsonArray categoriesArray = properties.getAsJsonArray("categories");
                List<String> categoryIds = new ArrayList<>();
                List<String> categoryTitles = new ArrayList<>();
                for (JsonElement categoryElement : categoriesArray) {
                    JsonObject category = categoryElement.getAsJsonObject();
                    categoryIds.add(category.get("id").getAsString());
                    categoryTitles.add(category.get("title").getAsString());
                }

                // Sources
                JsonArray sourcesArray = properties.getAsJsonArray("sources");
                List<String> sourceIds = new ArrayList<>();
                List<String> sourceUrls = new ArrayList<>();
                for (JsonElement sourceElement : sourcesArray) {
                    JsonObject source = sourceElement.getAsJsonObject();
                    sourceIds.add(source.get("id").getAsString());
                    sourceUrls.add(source.get("url").getAsString());
                }

                // Geometry coordinates
                JsonArray coordinates = geometry.getAsJsonArray("coordinates");
                double latitude = coordinates.get(1).getAsDouble();
                double longitude = coordinates.get(0).getAsDouble();
                String coordinate = latitude + "," + longitude;


                HashMap<String, String> featureData = new HashMap<>();
                featureData.put("id", ""+id);
                featureData.put("title", ""+title);
                featureData.put("description", "___"+description);
                featureData.put("link", ""+link);
                featureData.put("date", ""+date);
                featureData.put("magnitudeValue", ""+magnitudeValue);
                featureData.put("magnitudeUnit", ""+magnitudeUnit);
                featureData.put("categoryIds", ""+categoryIds.toString());
                featureData.put("categoryTitles", ""+categoryTitles.toString());
                featureData.put("sourceIds", ""+sourceIds.toString());
                featureData.put("sourceUrls", ""+sourceUrls.toString());

                // Add the feature data to the main HashMap with coordinates as the key
                dataMap.put(coordinate, featureData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataMap;
    }
}
