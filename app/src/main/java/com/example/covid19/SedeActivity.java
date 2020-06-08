package com.example.covid19;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import beans.SedeBean;
import db.DatabaseManagerSede;
import helper.Session;

public class SedeActivity extends AppCompatActivity {
    Context context;
    DatabaseManagerSede dbSede;
    List<SedeBean> listaSede;
    Session session;
    String nID[];
    String nInfo1[];
    String nInfo2[];
    String nInfo3[];
    ListView lvSede;
    FloatingActionButton btnAgregarSede;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sede);
        context = this;
        session = new Session(context);
        dbSede = new DatabaseManagerSede(context);
        listaSede = dbSede.ListarPorSedeXEmpresa(session.getIdEmpresa());
        SedeBean sede = null;
        int len = listaSede.size();
        nID = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];
        for (int i = 0; i < listaSede.size(); i++) {
            sede = listaSede.get(i);
            nID[i] = sede.getID();
            nInfo1[i] = sede.getNOMBRE_SEDE();
            nInfo2[i] = sede.getDIRECCION();
            nInfo3[i] = sede.getFEC_CREACION();
        }
        btnAgregarSede = findViewById(R.id.btnAgregarSede);
        lvSede = findViewById(R.id.lvSede);
        SedeActivity.MyAdapter adapter = new SedeActivity.MyAdapter(this, nID, nInfo1, nInfo2, nInfo3);
        lvSede.setAdapter(adapter);
        btnAgregarSede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dsp = new Intent(SedeActivity.this, ViewPageRegisterSede.class);
                session.setIdSede("");
                startActivity(dsp);
            }
        });
        lvSede.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent dsp = new Intent(SedeActivity.this, ViewPageRegisterSede.class);
                String ide =(String) ((TextView) view.findViewById(R.id.txtInfoSedeId)).getText();
                session.setIdSede(ide);
                startActivity(dsp);
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String nID[];
        String nInfo1[];
        String nInfo2[];
        String nInfo3[];

        MyAdapter(Context c, String ID[], String Info1[], String Info2[], String Info3[]) {
            super(c, R.layout.row_sede, R.id.txtInfoSedeId, ID);
            this.context = c;
            this.nID = ID;
            this.nInfo1 = Info1;
            this.nInfo2 = Info2;
            this.nInfo3 = Info3;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_sede, parent, false);
            TextView id = row.findViewById(R.id.txtInfoSedeId);
            TextView nombre_sede = row.findViewById(R.id.txtInfoSede1);
            TextView direccion = row.findViewById(R.id.txtInfoSede2);
            TextView hora = row.findViewById(R.id.txtInfoSede3);
            id.setText(nID[position]);
            nombre_sede.setText(nInfo1[position]);
            direccion.setText(nInfo2[position]);
            hora.setText(nInfo3[position]);
            return row;
        }
    }
}

