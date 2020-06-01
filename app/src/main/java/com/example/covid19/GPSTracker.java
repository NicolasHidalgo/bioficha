package com.example.covid19;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GPSTracker extends Service  implements LocationListener {

    private final Context context;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean cantGetLocation = false;
    Location location;
    protected LocationManager locationManager;
    public GPSTracker(Context context){
        this.context = context;
    }

    public Location getLocation(){
        try{
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
            isNetworkEnabled=locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (isGPSEnabled) {
                            if (location == null) {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
                                if (locationManager != null) {
                                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                }
                            }
                        }
                        if (location == null) {
                            if (isNetworkEnabled) {
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
                                if (locationManager != null) {
                                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                }

                            }
                        }
                    }
                 }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return  location;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
