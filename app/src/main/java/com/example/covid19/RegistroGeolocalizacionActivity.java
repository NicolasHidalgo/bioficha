package com.example.covid19;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegistroGeolocalizacionActivity extends Fragment {
    Context context;
    Button btnBuscarXY;
    EditText txtLatitud, txtLongitud;
    int request_code = 1;
    View view;
    public RegistroGeolocalizacionActivity(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_registro_geolocalizacion,container,false);
        context = this.getActivity();
        btnBuscarXY = view.findViewById(R.id.btnBuscarXY);
        txtLatitud = view.findViewById(R.id.txtLatitud);
        txtLongitud = view.findViewById(R.id.txtLongitud);

        btnBuscarXY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            Double latitud = parametros.getDouble("latitud");
            Double longitud = parametros.getDouble("longitud");
            txtLatitud.setText(Double.toString(latitud));
            txtLongitud.setText(Double.toString(longitud));
        }
    }
}
