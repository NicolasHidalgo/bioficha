package com.example.covid19;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import beans.SedePoligonoBean;
import db.DatabaseManagerSedePoligono;
import helper.Session;
import util.Util;

public class RegistroGeolocalizacionActivity extends Fragment {
    Context context;
    Button btnBuscarXY;
    Session session;
    EditText txtCoordenadaX1, txtCoordenadaX2,txtCoordenadaX3,txtCoordenadaX4;
    EditText txtCoordenadaY1, txtCoordenadaY2,txtCoordenadaY3,txtCoordenadaY4;
    int request_code = 1;
    View view;
    DatabaseManagerSedePoligono dbSedePoligono;
    List<LatLng> latLng;
    public RegistroGeolocalizacionActivity(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_registro_geolocalizacion,container,false);
        context = this.getActivity();
        session = new Session(context);
        dbSedePoligono = new DatabaseManagerSedePoligono(context);
        btnBuscarXY = view.findViewById(R.id.btnBuscarXY);
        txtCoordenadaX1 = view.findViewById(R.id.txtCoordenadaX1);
        txtCoordenadaX2 = view.findViewById(R.id.txtCoordenadaX2);
        txtCoordenadaX3 = view.findViewById(R.id.txtCoordenadaX3);
        txtCoordenadaX4 = view.findViewById(R.id.txtCoordenadaX4);
        txtCoordenadaY1 = view.findViewById(R.id.txtCoordenadaY1);
        txtCoordenadaY2 = view.findViewById(R.id.txtCoordenadaY2);
        txtCoordenadaY3 = view.findViewById(R.id.txtCoordenadaY3);
        txtCoordenadaY4 = view.findViewById(R.id.txtCoordenadaY4);

        String ide = session.getIdSede();
        if (!(ide.isEmpty())){
            TraerGeolocalizacion(ide);
        }
        btnBuscarXY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo Location Already on  ... start
                final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                // Todo Location Already on  ... end
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Util.hasGPSDevice(context)) {
                    Toast.makeText(context, "Necesita habilitar el GPS", Toast.LENGTH_LONG).show();
                    return;
                }
                
                Intent dsp = new Intent(context, MapActivity.class);
                startActivityForResult(dsp, request_code);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode == request_code) && (resultCode == Activity.RESULT_OK)){
            Bundle parametros = data.getExtras();
            List<LatLng> latLngsList = (List<LatLng>)parametros.getSerializable("list");
            txtCoordenadaX1.setText(String.valueOf(latLngsList.get(0).latitude));
            txtCoordenadaY1.setText(String.valueOf(latLngsList.get(0).longitude));
            txtCoordenadaX2.setText(String.valueOf(latLngsList.get(1).latitude));
            txtCoordenadaY2.setText(String.valueOf(latLngsList.get(1).longitude));
            txtCoordenadaX3.setText(String.valueOf(latLngsList.get(2).latitude));
            txtCoordenadaY3.setText(String.valueOf(latLngsList.get(2).longitude));
            txtCoordenadaX4.setText(String.valueOf(latLngsList.get(3).latitude));
            txtCoordenadaY4.setText(String.valueOf(latLngsList.get(3).longitude));

        }
    }

    public void TraerGeolocalizacion(String ide){
        List<SedePoligonoBean> sedePoligonoBean = dbSedePoligono.getList(ide);
        for (int i=0;i<sedePoligonoBean.size();i++){
            if(i==0){
                txtCoordenadaX1.setText(sedePoligonoBean.get(i).getLATITUD().toString());
                txtCoordenadaY1.setText(sedePoligonoBean.get(i).getLONGITUD().toString());
            }else if(i==1){
                txtCoordenadaX2.setText(sedePoligonoBean.get(i).getLATITUD().toString());
                txtCoordenadaY2.setText(sedePoligonoBean.get(i).getLONGITUD().toString());
            }else if(i==2){
                txtCoordenadaX3.setText(sedePoligonoBean.get(i).getLATITUD().toString());
                txtCoordenadaY3.setText(sedePoligonoBean.get(i).getLONGITUD().toString());
            }else{
                txtCoordenadaX4.setText(sedePoligonoBean.get(i).getLATITUD().toString());
                txtCoordenadaY4.setText(sedePoligonoBean.get(i).getLONGITUD().toString());
            }
        }
    }
}
