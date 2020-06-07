package com.example.covid19;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    Context context;
    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Location mLocation;
    private int zoom;
    double latitud, longitud;
    Button btnPicker,btnDraw, btnClear;
    CheckBox checkBox;
    Polygon polygon;
    List<LatLng> latLngsList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context = this.getApplication();
        btnPicker = findViewById(R.id.btnPicker);
        checkBox = findViewById(R.id.check_box);
        btnClear = findViewById(R.id.btnClear);
        btnDraw = findViewById(R.id.btnDraw);
        btnPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                if(latLngsList.size() != 4)  { Toast.makeText(context, "Primero debe dibujar 4 puntos", Toast.LENGTH_SHORT).show(); return; }
                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);

                // Set a title for alert dialog
                builder.setTitle("ALERTA");

                // Ask the final question
                builder.setMessage("¿Estás seguro de elegir esta posición?");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent dsp = new Intent();
                                dsp.putExtra("list", (Serializable) latLngsList);
                                setResult(MapActivity.RESULT_OK,dsp);
                                finish();

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Si", dialogClickListener);

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No",dialogClickListener);

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
                },10);
                return;
            }
        }
        gpsTracker = new GPSTracker(getApplicationContext());
        mLocation = gpsTracker.getLocation();
        if(mLocation ==null) {
            Toast.makeText(MapActivity.this, "Para una mejor precisión, necesita habilitar el GPS", Toast.LENGTH_SHORT).show();
            latitud =-12.0262676;
            longitud =-77.1278635;
            zoom=11;
        } else {
            latitud = mLocation.getLatitude();
            longitud = mLocation.getLongitude();
            zoom = 15;
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(latLngsList.size() != 4)  {Toast.makeText(context, "Primero debe dibujar 4 puntos", Toast.LENGTH_SHORT).show(); checkBox.setChecked(false); return; }
                    if (isChecked) {
                        if (polygon == null) return;
                        polygon.setFillColor(Color.RED);
                    } else {
                        polygon.setFillColor(Color.TRANSPARENT);

                    }

            }
        });
        btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latLngsList.size() == 4) {
                    if (polygon != null) polygon.remove();
                    PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngsList).clickable(true);
                    polygon = mMap.addPolygon(polygonOptions);
                    polygon.setStrokeColor(Color.RED);
                    if (checkBox.isChecked())
                        polygon.setFillColor(Color.RED);
                }else{
                    Toast.makeText(context, "Es necesario dibujar 4 puntos", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(polygon!=null) polygon.remove();
                for (Marker marker: markerList) marker.remove();
                latLngsList.clear();
                markerList.clear();
                checkBox.setChecked(false);

            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        LatLng peru = new LatLng(latitud,longitud);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(peru,zoom));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(latLngsList.size()>3){
                    Toast.makeText(context, "Solo puede dibujar 4 puntos", Toast.LENGTH_SHORT).show();
                    return;
                }
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                Marker marker = googleMap.addMarker(markerOptions);
                latLngsList.add(latLng);
                markerList.add(marker);

            }
        });

    }
    }

