package com.example.covid19;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import helper.Session;

public class EmpresasActivity extends AppCompatActivity {
    FloatingActionButton btnAgregarEmpresa;
    ListView listView;
    Context context;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresas);
        btnAgregarEmpresa = findViewById(R.id.btnAgregarEmpresa);
        listView = findViewById(R.id.listview);
        context = this;
        session = new Session(context);
        session.getIdEmpresa();
        btnAgregarEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(EmpresasActivity.this, RegistroEmpresasActivity.class);
                startActivity(dsp);
            }
        });
    }
}
