package com.itsolution.horizon;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class tomorrow extends AsyncTask<Void, Void, String> {

    private final String apiUrl = "https://api.tomorrow.io/v4/weather/forecast";
    private final String apiKey = "oBhnbNvXQxvrdRbEk1JNQEztyf4PKEzy"; // Replace with your API key
    private final String location = "42.3478,-71.0466";
    private String weatherResponse; // Field to store the weather response
    private double temperature;
    private int humidity;
    private double dewPoint;
    private int precipitationProbability;
    private double pressureSurfaceLevel;
    private int rainIntensity;
    private int sleetIntensity;
    private int snowIntensity;
    private double temperatureApparent;
    private int uvHealthConcern;
    private int uvIndex;
    private double visibility;
    private int weatherCode;
    private double windDirection;
    private double windGust;
    private double windSpeed;
    private double cloudBase;
    private int cloudCover;

    // Constructor to initialize the weatherResponse field
    public tomorrow() {
        this.weatherResponse = null;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // Create a URL with the API endpoint and query parameters
            String fullUrl = apiUrl + "?location=" + location + "&apikey=" + apiKey;
            URL url = new URL(fullUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Store the response in the weatherResponse field
                weatherResponse = response.toString();
                return weatherResponse;
            } else {
                // Handle the error case here
                Log.e("HTTPError", "HTTP error code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (response != null) {
            // Handle the response here (e.g., parse JSON)
            try {
                JSONObject jsonResponse = new JSONObject(response);

                JSONObject timelines = jsonResponse.getJSONObject("timelines");
                JSONArray minutelyArray = timelines.getJSONArray("minutely");
                if (minutelyArray.length() > 0) {
                    JSONObject minutely = minutelyArray.getJSONObject(0);
                    JSONObject values = minutely.getJSONObject("values");
                    String time = minutely.getString("time");

                    temperature = values.getDouble("temperature");
                    humidity = values.getInt("humidity");
                    dewPoint=values.getDouble("dewPoint");
                    precipitationProbability=values.getInt("precipitationProbability");
                    pressureSurfaceLevel=values.getDouble("pressureSurfaceLevel");
                    rainIntensity=values.getInt("rainIntensity");
                    sleetIntensity=values.getInt("sleetIntensity");
                    snowIntensity=values.getInt("snowIntensity");
                    temperatureApparent=values.getDouble("temperatureApparent");
                    uvHealthConcern=values.getInt("uvHealthConcern");
                    uvIndex=values.getInt("uvIndex");
                    visibility=values.getDouble("visibility");
                    weatherCode=values.getInt("weatherCode");
                    windDirection=values.getDouble("windDirection");
                    windGust=values.getDouble("windGust");
                    windSpeed=values.getDouble("windSpeed");
                    cloudBase=values.getDouble("cloudBase");
                    cloudCover=values.getInt("cloudCover");

                    // Print the scraped data to the log
                    Log.e("WeatherData from class", "Time: " + time);
                    Log.e("WeatherData", "Temperature: " + temperature);
                    Log.e("WeatherData", "Humidity: " + humidity);
                } else {
                    Log.e("WeatherData", "Minutely data not found.");
                }
                // Access the JSON data as needed
                Log.d("WeatherData", "JSON Response: " + jsonResponse.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Handle the error here
            Log.e("Error", "An error occurred during the API request");
        }
    }

    // Getter method to retrieve the weather response
    public String getWeatherResponse() {
        return weatherResponse;
    }


    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public int getPrecipitationProbability() {
        return precipitationProbability;
    }

    public double getPressureSurfaceLevel() {
        return pressureSurfaceLevel;
    }

    public int getRainIntensity() {
        return rainIntensity;
    }

    public int getSleetIntensity() {
        return sleetIntensity;
    }

    public int getSnowIntensity() {
        return snowIntensity;
    }

    public double getTemperatureApparent() {
        return temperatureApparent;
    }

    public int getUvHealthConcern() {
        return uvHealthConcern;
    }

    public int getUvIndex() {
        return uvIndex;
    }

    public double getVisibility() {
        return visibility;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public double getWindGust() {
        return windGust;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getCloudBase() {
        return cloudBase;
    }

    public int getCloudCover() {
        return cloudCover;
    }




}
