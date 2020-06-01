package com.example.covid19;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class SedeActivity extends AppCompatActivity {

    FloatingActionButton btnAgregarSede;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sede);
        btnAgregarSede = findViewById(R.id.btnAgregarSede);
        btnAgregarSede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(SedeActivity.this, RegistroSedesActivity.class);
                startActivity(dsp);
            }
        });
    }
}
