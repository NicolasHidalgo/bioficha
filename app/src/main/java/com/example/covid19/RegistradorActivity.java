package com.example.covid19;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class RegistradorActivity extends AppCompatActivity {

    FloatingActionButton btnAgregarRegistrador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrador);
        btnAgregarRegistrador = findViewById(R.id.btnAgregarRegistrador);
        btnAgregarRegistrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(RegistradorActivity.this, ViewPageRegisterEmpleado.class);
                startActivity(dsp);
            }
        });
    }
}
