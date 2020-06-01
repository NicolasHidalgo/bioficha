package com.example.covid19;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class RegistroSedesActivity extends AppCompatActivity{
    Context context;
    Button btnBuscarXY;
    EditText txtLatitud, txtLongitud;
    int request_code = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_sedes);
        btnBuscarXY = findViewById(R.id.btnBuscarXY);
        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);

        btnBuscarXY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(RegistroSedesActivity.this, MapActivity.class);
                startActivityForResult(dsp, request_code);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode == request_code) && (resultCode == RESULT_OK)){
            Bundle parametros = data.getExtras();
            Double latitud = parametros.getDouble("latitud");
            Double longitud = parametros.getDouble("longitud");
            txtLatitud.setText(Double.toString(latitud));
            txtLongitud.setText(Double.toString(longitud));
        }
    }

}


