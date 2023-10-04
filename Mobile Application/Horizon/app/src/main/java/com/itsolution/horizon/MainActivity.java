package com.itsolution.horizon;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aerisweather.aeris.communication.Action;
import com.aerisweather.aeris.communication.AerisCallback;
import com.aerisweather.aeris.communication.AerisCommunicationTask;
import com.aerisweather.aeris.communication.AerisEngine;
import com.aerisweather.aeris.communication.AerisRequest;
import com.aerisweather.aeris.communication.Endpoint;
import com.aerisweather.aeris.communication.EndpointType;
import com.aerisweather.aeris.communication.parameter.LimitParameter;
import com.aerisweather.aeris.communication.parameter.ParameterBuilder;
import com.aerisweather.aeris.communication.parameter.PlaceParameter;
import com.aerisweather.aeris.model.AerisResponse;
import com.airbnb.lottie.L;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.animation.Interpolator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    BottomNavigationView navigationView;
    private GoogleMap mMap;
    boolean locationPermission = false;
    Location myLocation = null;
    Location myUpdatedLocation = null;
    float Bearing = 0;
    boolean AnimationStatus = false;
    static Marker carMarker;
    Bitmap BitMapMarker,BitMapMarker_,lightening_shield;
    public WebView webView;
    private final static int LOCATION_REQUEST_CODE = 23;
    Dialog wait;

    public HashMap<String, HashMap<String, String>> dataMap;
    public scrape_json scrape_json;;

    private static final String ngrok_temp_url = "https://1a50-103-25-248-225.ngrok-free.app/predict_api"; // Update the URL if necessary
    JSONObject inputJson;

    public String lat_flood=null,long_flood=null;


    FloatingActionButton floatingActionButton;
    private boolean flood_search=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.top_msg_bar));
        }

        scrape_json=new scrape_json();


        navigationView = findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId() == R.id.lightening) {

                    wait=new Dialog(MainActivity.this);
                    wait.setContentView(R.layout.wait_);
                    wait.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    wait.setCancelable(true);
                    wait.show();


                    tomorrow weather = new tomorrow();
                    weather.execute();

                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            double temperature = weather.getTemperature();
                            int humidity = weather.getHumidity();
                            double dewPoint = weather.getDewPoint();
                            int precipitationProbability = weather.getPrecipitationProbability()/100;
                            double pressureSurfaceLevel = weather.getPressureSurfaceLevel();
                            int rainIntensity = weather.getRainIntensity();
                            int sleetIntensity = weather.getSleetIntensity();
                            int snowIntensity = weather.getSnowIntensity();
                            double temperatureApparent = weather.getTemperatureApparent();
                            int uvHealthConcern = weather.getUvHealthConcern();
                            int uvIndex = weather.getUvIndex();
                            double visibility = weather.getVisibility();
                            int weatherCode = weather.getWeatherCode();
                            double windDirection = weather.getWindDirection();
                            double windGust = weather.getWindGust();
                            double windSpeed = weather.getWindSpeed();
                            double cloudBase = weather.getCloudBase();
                            int cloudCover = weather.getCloudCover();



                            double e = 6.112 * Math.exp((17.67 * dewPoint) / (dewPoint + 243.5));
                            double T_wb = (dewPoint + 273.15) - ((2.5e6 / (1005 + 0.00163 * pressureSurfaceLevel)) * ((e / 100) - (dewPoint + 273.15)));


                            double alpha = ((17.27 * temperature) / (237.7 + temperature)) + Math.log(dewPoint / 100.0);
                            double vapor_pressure = 6.112 * Math.exp(alpha);
                            double alpha2 = ((17.27 * temperature) / (237.7 + temperature)) + Math.log(temperature / 100.0);
                            double es = 6.112 * Math.exp(alpha2);
                            double relativeHumidity = (vapor_pressure / es) * 100.0;
                            double specificHumidity = (0.622 * es) / (pressureSurfaceLevel - (0.378 * es));


                            double roughnessLength = 0.1;
                            double windSpeedAt50m = windSpeed * (Math.log(50 / roughnessLength) / Math.log(10 / roughnessLength));



                            inputJson = new JSONObject();
                            try {
                                inputJson.put("T2M", temperature);
                                inputJson.put("T2MDEW", dewPoint);
                                inputJson.put("T2MWET", T_wb);
                                inputJson.put("QV2M", specificHumidity);
                                inputJson.put("RH2M", relativeHumidity);
                                inputJson.put("PRECTOTCORR", precipitationProbability);
                                inputJson.put("PS", pressureSurfaceLevel*0.02952998751);
                                inputJson.put("WS10M", windSpeed);
                                inputJson.put("WD10M", windDirection);
                                inputJson.put("WS50M", windSpeedAt50m);
                                inputJson.put("WD50M", windDirection);
                            } catch (JSONException error) {
                                error.printStackTrace();
                            }
                            String prediction=lightening();

                            
                            wait.dismiss();

                            Dialog lightening=new Dialog(MainActivity.this);
                            lightening.setContentView(R.layout.lightening_alert);
                            lightening.getWindow().setBackgroundDrawableResource(R.color.transparent);
                            lightening.setCancelable(true);

                            TextView T2M_,pred_,dewPoint_,T_wb_,specificHumidity_,relativeHumidity_,precipitationProbability_,pressureSurfaceLevel_,
                                    windSpeed_,windDirection_AT_10M,windSpeedAt50m_,windDirection_AT_50M;

                            T2M_=lightening.findViewById(R.id.T2M);
                            pred_=lightening.findViewById(R.id.pred);
                            dewPoint_=lightening.findViewById(R.id.dewPoint);
                            T_wb_=lightening.findViewById(R.id.T_wb);
                            specificHumidity_=lightening.findViewById(R.id.specificHumidity);
                            relativeHumidity_=lightening.findViewById(R.id.relativeHumidity);
                            precipitationProbability_=lightening.findViewById(R.id.precipitationProbability);
                            pressureSurfaceLevel_=lightening.findViewById(R.id.pressureSurfaceLevel);
                            windSpeed_=lightening.findViewById(R.id.windSpeed);
                            windDirection_AT_10M=lightening.findViewById(R.id.windDirection);
                            windSpeedAt50m_=lightening.findViewById(R.id.windSpeedAt50m);
                            windDirection_AT_50M=lightening.findViewById(R.id.windDirectionAt50m);



                            DecimalFormat decimalFormat = new DecimalFormat("0.000");

                            T2M_.setText(String.valueOf(temperature));
                            pred_.setText(String.valueOf(prediction)+"0");
                            dewPoint_.setText(String.valueOf(decimalFormat.format(dewPoint)));
                            T_wb_.setText(String.valueOf(decimalFormat.format(T_wb)));
                            specificHumidity_.setText(String.valueOf(decimalFormat.format(specificHumidity)));
                            relativeHumidity_.setText(String.valueOf(decimalFormat.format(relativeHumidity)));
                            precipitationProbability_.setText(String.valueOf(precipitationProbability));
                            pressureSurfaceLevel_.setText(String.valueOf(pressureSurfaceLevel));
                            windSpeed_.setText(String.valueOf(windSpeed));
                            windDirection_AT_10M.setText(String.valueOf(windDirection));
                            windDirection_AT_50M.setText(String.valueOf(windDirection));
                            windSpeedAt50m_.setText(String.valueOf(decimalFormat.format(windSpeedAt50m)));

                            lightening.show();


                        }
                    }, 3000);



                    return true;
                }


                if (item.getItemId() == R.id.flood) {
                    if(lat_flood!=null && long_flood!=null){

                        floodhub(lat_flood,long_flood);
                    }


                    return true;
                }

                if (item.getItemId() == R.id.msg) {


                    Intent intent=new Intent(MainActivity.this,node_mcu_msg.class);
                    startActivity(intent);
                    return true;
                }
                if(item.getItemId()==R.id.cyclone){

                    wait=new Dialog(MainActivity.this);
                    wait.setContentView(R.layout.wait);
                    wait.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    wait.setCancelable(true);
                    wait.show();

                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    String apiKey = "WekWAE5JirrFSnNegBx7OFFNAatnBzKnFunAj7ps";
                    String url = "https://eonet.gsfc.nasa.gov/api/v3/events/geojson?status=open";

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    dataMap = JsonParser.parseJson(response);
                                    CHANGE_ON_EONET_MAP(dataMap);

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error", "error here");
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("User-Agent", "YourApp");
                            headers.put("Authorization", "Bearer " + apiKey);
                            return headers;
                        }
                    };

                    queue.add(stringRequest);


                    return true;

                }


                return false;
            }
        });

        requestPermision();
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.my_location);
        Bitmap b = bitmapdraw.getBitmap();
        BitMapMarker = Bitmap.createScaledBitmap(b, 210, 120, false);


        BitmapDrawable bitmapdraw_ = (BitmapDrawable) getResources().getDrawable(R.drawable.event_location);
        Bitmap b_ = bitmapdraw_.getBitmap();
        BitMapMarker_ = Bitmap.createScaledBitmap(b_, 210, 120, false);


        BitmapDrawable shield = (BitmapDrawable) getResources().getDrawable(R.drawable.lightening_shield);
        Bitmap lightening = shield.getBitmap();
        lightening_shield = Bitmap.createScaledBitmap(lightening, 210, 120, false);


        CardView add_shelter=findViewById(R.id.add_shelter);
        add_shelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog add_location_=new Dialog(MainActivity.this);
                add_location_.setContentView(R.layout.add_location);
                add_location_.getWindow().setBackgroundDrawableResource(R.color.transparent);
                add_location_.setCancelable(true);
                add_location_.show();
            }
        });


    }


    private String lightening(){
        final String[] pred = {""};
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ngrok_temp_url, inputJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pred[0] =response.toString();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Error", "Volley error: " + error.toString());
                    }
                });
        requestQueue.add(jsonObjectRequest);
        return pred[0];
    }





    private void floodhub(String lati,String lang){
        Dialog flood_hub=new Dialog(MainActivity.this);
        flood_hub.setContentView(R.layout.flood);
        flood_hub.getWindow().setBackgroundDrawableResource(R.color.transparent);
        WebView webView=flood_hub.findViewById(R.id.google_flood_hub);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://sites.research.google/floods/l/"+lati+"/"+lang+"/7.7065");
        Log.e("langlat", "https://sites.research.google/floods/l/"+lati+"/"+lang+"/7.7065");
        flood_hub.setCancelable(true);
        flood_hub.show();
    }






    //to get user location
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if (AnimationStatus) {
                    myUpdatedLocation = location;
                }
                else {
                    myLocation = location;
                    myUpdatedLocation = location;
                    LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    lat_flood=String.valueOf(location.getLatitude());
                    long_flood=String.valueOf(location.getLongitude());



                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                        String ssid = wifiInfo.getSSID();
                        carMarker = mMap.addMarker(new MarkerOptions().position(latlng).flat(true).icon(BitmapDescriptorFactory.fromBitmap(lightening_shield)));

                    }else {
                        carMarker = mMap.addMarker(new MarkerOptions().position(latlng).flat(true).icon(BitmapDescriptorFactory.fromBitmap(BitMapMarker)));
                    }


                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 17f);
                    mMap.animateCamera(cameraUpdate);
                }
                Bearing = location.getBearing();
                LatLng updatedLatLng = new LatLng(myUpdatedLocation.getLatitude(), myUpdatedLocation.getLongitude());
                changePositionSmoothly(carMarker, updatedLatLng, Bearing);

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getMyLocation();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng LatLang=marker.getPosition();
                if(!dataMap.isEmpty()){

                    HashMap<String, String> featureData = dataMap.get( LatLang.latitude + "," + LatLang.longitude);
                    String id = featureData.get("id");
                    String title = featureData.get("title");
                    String description = featureData.get("description");
                    String link = featureData.get("link");
                    String date = featureData.get("date");
                    String magnitudeValue = featureData.get("magnitudeValue");
                    String magnitudeUnit = featureData.get("magnitudeUnit");

                    List<String> categoryIds = getListFromString(featureData.get("categoryIds"));
                    List<String> categoryTitles = getListFromString(featureData.get("categoryTitles"));
                    List<String> sourceIds = getListFromString(featureData.get("sourceIds"));
                    List<String> sourceUrls = getListFromString(featureData.get("sourceUrls"));


                    Dialog onclick_marker=new Dialog(MainActivity.this);
                    onclick_marker.setContentView(R.layout.onclickmarker);
                    onclick_marker.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    onclick_marker.setCancelable(true);

                    TextView id_,title_,description_,link_,date_,magnitudeValue_,magnitudeUnit_;

                    id_=onclick_marker.findViewById(R.id.id);
                    title_=onclick_marker.findViewById(R.id.title);
                    description_=onclick_marker.findViewById(R.id.description);
                    link_=onclick_marker.findViewById(R.id.link);
                    date_=onclick_marker.findViewById(R.id.date);
                    magnitudeValue_=onclick_marker.findViewById(R.id.magnitudeValue);
                    magnitudeUnit_=onclick_marker.findViewById(R.id.magnitudeUnit);

                    id_.setText(id);
                    title_.setText(title);
                    description_.setText(description);
                    link_.setText(link);
                    date_.setText(date);
                    magnitudeValue_.setText(magnitudeValue);
                    magnitudeUnit_.setText(magnitudeUnit);

                    onclick_marker.show();


                }
                return false;
            }
        });
    }


    void CHANGE_ON_EONET_MAP(HashMap<String, HashMap<String, String>> dataMap) {


        for (Map.Entry<String, HashMap<String, String>> entry : dataMap.entrySet()) {
            String coordinate = entry.getKey();
            Log.e("coordinate", String.valueOf(coordinate));

            String[] parts = coordinate.split(",");
            if (parts.length == 2) {
                try {
                    double latitude = Double.parseDouble(parts[0]);
                    double longitude = Double.parseDouble(parts[1]);
                    LatLng coor = new LatLng(latitude, longitude);
                    carMarker = mMap.addMarker(new MarkerOptions().position(coor).flat(true).icon(BitmapDescriptorFactory.fromBitmap(BitMapMarker_)));

                } catch (NumberFormatException e) {

                    Log.e("MainActivity", "Error parsing latitude and longitude.");
                }
            } else {
                // Handle invalid coordinate format
                Log.e("MainActivity", "Invalid coordinate format.");
            }
        }
        wait.dismiss();
    }


    void changePositionSmoothly(final Marker myMarker, final LatLng newLatLng, final Float bearing) {

        final LatLng startPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        final LatLng finalPosition = newLatLng;
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;
        final boolean hideMarker = false;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                myMarker.setRotation(bearing);

                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + finalPosition.latitude * t,
                        startPosition.longitude * (1 - t) + finalPosition.longitude * t);

                myMarker.setPosition(currentPosition);

                if (t < 1) {

                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        myMarker.setVisible(false);
                    } else {
                        myMarker.setVisible(true);
                    }
                }
                myLocation.setLatitude(newLatLng.latitude);
                myLocation.setLongitude(newLatLng.longitude);
            }
        });
    }

    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            LocationstatusCheck();
            locationPermission = true;
            //init google map fragment to show map.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
            mapFragment.getMapAsync(this);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationstatusCheck();
                    //if permission granted.
                    locationPermission = true;
                    //init google map fragment to show map.
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    mapFragment.getMapAsync(this);
                    // getMyLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    public void LocationstatusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    // Helper method to convert a comma-separated string to a list of strings
    private List<String> getListFromString(String input) {
        input = input.replaceAll("\\[|\\]", ""); // Remove brackets
        String[] items = input.split(", ");
        return Arrays.asList(items);
    }


}