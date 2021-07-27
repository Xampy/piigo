package com.xampy.piigo.models.userentity;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public abstract class AbstractPiigoUser {

    //[START class variables ]
    protected String username;
    protected String message;

    protected double altitude;
    protected double longitude;

    protected Marker marker;
    protected Context context;

    //[START class variables ]

    protected int identifier; //This help to identify the matching marker

    public AbstractPiigoUser(Context context){
        this.context = context;

        this.altitude = this.longitude = 0.0f;
        this.message = "Message";
        this.identifier = 0; //This may changed
    }

    public double getAltitude() {
        return this.altitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setAltitude(double altitude){
        this.altitude = altitude;
    }

    public String getUsername() {
        return this.username;
    }

    public int getIdentifier() {
        return this.identifier;
    }


    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }


    public Marker getMarker() {
        return this.marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    protected void drawMarker(GoogleMap googleMap) {
        LatLng latLng = new LatLng(this.altitude, this.longitude);
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title(this.message);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_thumb_up_white_24dp) );

        this.marker = googleMap.addMarker(markerOptions);
    }
}
