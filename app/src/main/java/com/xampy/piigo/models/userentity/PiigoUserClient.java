package com.xampy.piigo.models.userentity;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xampy.piigo.R;
import com.xampy.piigo.models.tools.PiigoBitmapCreator;

public class PiigoUserClient extends AbstractPiigoUser {

    public PiigoUserClient(Context context) {
        super(context);
    }

    @Override
    public void drawMarker(GoogleMap googleMap) {

        LatLng latLng = new LatLng(this.altitude, this.longitude);
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title(this.message);
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
                PiigoBitmapCreator.createCustomMarker(
                        this.context,
                        R.drawable.ic_client_white,
                        "")));

        this.marker = googleMap.addMarker(markerOptions);
        this.marker.setTag(0); //0 is identifier for the mark

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
    }
}
