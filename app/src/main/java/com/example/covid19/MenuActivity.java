package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MenuActivity extends AppCompatActivity {
    LinearLayout btnEmpresa;
    Button btnVerFichas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Spinner spinner = findViewById(R.id.spinner);
        btnVerFichas = findViewById(R.id.btnVerFichas);
        String[] value = {"Municipio","Loza Deportiva", "Parque Central"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner, arrayList);
        spinner.setAdapter(arrayAdapter);
        btnEmpresa = findViewById(R.id.btnEmpresa);
        btnEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuActivity.this, "Empresa", Toast.LENGTH_SHORT).show();
            }
        });
        btnVerFichas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(MenuActivity.this, FichasActivity.class);
                startActivity(dsp);
            }
        });

    }
}
