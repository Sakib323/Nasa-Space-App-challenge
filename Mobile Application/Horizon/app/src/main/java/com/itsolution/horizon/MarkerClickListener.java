package com.itsolution.horizon;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerClickListener implements GoogleMap.OnMarkerClickListener {

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Handle the marker click event here
        // You can perform actions like displaying an info window, opening a new activity, etc.
        // Example: Show an info window when the marker is clicked
        marker.showInfoWindow();

        return true; // Return true to consume the event and prevent default behavior
    }
}
