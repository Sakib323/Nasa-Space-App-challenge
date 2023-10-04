package com.itsolution.horizon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class scrape_json {


    public ArrayList get_air_info(String jsonData) {

        String cityName="",state="",country="",dateTimeISO="",aqi="",aqiCategory="",aqiColor="",pollutantType="",pollutantName="",pollutantValue="",pollutantAqi="",pollutantCategory="",pollutantColor="";
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray responseArray = jsonObject.getJSONArray("response");
            JSONObject responseData = responseArray.getJSONObject(0);
            JSONObject location = responseData.getJSONObject("place");
            cityName = location.getString("name");
            state = location.getString("state");
            country = location.getString("country");
            dateTimeISO = responseData.getJSONArray("periods").getJSONObject(0).getString("dateTimeISO");
            aqi = String.valueOf(responseData.getJSONArray("periods").getJSONObject(0).getInt("aqi"));
            aqiCategory = responseData.getJSONArray("periods").getJSONObject(0).getString("category");
            aqiColor = responseData.getJSONArray("periods").getJSONObject(0).getString("color");
            JSONArray pollutants = responseData.getJSONArray("periods").getJSONObject(0).getJSONArray("pollutants");
            for (int i = 0; i < pollutants.length(); i++) {
                JSONObject pollutant = pollutants.getJSONObject(i);
                pollutantType = pollutant.getString("type");
                pollutantName = pollutant.getString("name");
                pollutantValue = String.valueOf(pollutant.getDouble("valueUGM3"));
                pollutantAqi = String.valueOf(pollutant.getInt("aqi"));
                pollutantCategory = pollutant.getString("category");
                pollutantColor = pollutant.getString("color");

            }


        }catch (Exception e){

        }

        ArrayList<String> air_quality_list = new ArrayList<>();
        Collections.addAll(air_quality_list,cityName,state,country,dateTimeISO,aqi,aqiCategory,aqiColor,pollutantType,pollutantName,pollutantValue,pollutantAqi,pollutantCategory,pollutantColor);
        return air_quality_list;
    }

}
