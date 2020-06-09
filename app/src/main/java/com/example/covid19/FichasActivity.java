package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import beans.BioFichaBean;
import db.DatabaseManagerBioFicha;
import helper.Session;
import util.Util;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;

public class FichasActivity extends AppCompatActivity {

    Context context;
    Session session;
    DatabaseManagerBioFicha dbFicha;
    List<BioFichaBean> listaFicha;

    FloatingActionButton btnAgregarFicha;
    ListView lvFicha;

    String nInfoId[];
    String nInfo1[];
    String nInfo2[];
    String nInfo3[];

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fichas);
        context = this;
        session = new Session(context);
        dbFicha = new DatabaseManagerBioFicha(context);

        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        listaFicha = dbFicha.ListarPorSedeFechaV2(session.getIdSede(),fechaHoy);

        BioFichaBean bio = null;
        int len = listaFicha.size();
        nInfoId = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];

        for (int i=0; i<listaFicha.size(); i++) {
            bio = listaFicha.get(i);
            nInfoId[i] = bio.getID();
            nInfo1[i] = bio.getNOMBRES();
            nInfo2[i] = bio.getNUM_DOCUMENTO();
            nInfo3[i] = bio.getFEC_CREACION();
        }

        btnAgregarFicha = findViewById(R.id.btnAgregarFicha);
        lvFicha = findViewById(R.id.lvFicha);
        MyAdapter adapter = new MyAdapter(this, nInfoId,nInfo1,nInfo2,nInfo3);
        lvFicha.setAdapter(adapter);

        btnAgregarFicha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo Location Already on  ... start
                final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                // Todo Location Already on  ... end
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Util.hasGPSDevice(context)) {
                    Toast.makeText(context, "Necesita habilitar el GPS", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent dsp = new Intent(FichasActivity.this, ViewPageRegister.class);
                session.setIdFicha("");
                startActivity(dsp);
            }
        });

        lvFicha.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Todo Location Already on  ... start
                final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                // Todo Location Already on  ... end
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Util.hasGPSDevice(context)) {
                    Toast.makeText(context, "Necesita habilitar el GPS", Toast.LENGTH_LONG).show();
                    return;
                }

                String ide =(String) ((TextView) view.findViewById(R.id.txtInfoFichaId)).getText();
                session.setIdFicha(ide);
                Intent intent = new Intent(context,ViewPageRegister.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        listaFicha = dbFicha.ListarPorSedeFechaV2(session.getIdSede(),fechaHoy);
        BioFichaBean bio = null;
        int len = listaFicha.size();
        nInfoId = new String[len];
        nInfo1 = new String[len];
        nInfo2 = new String[len];
        nInfo3 = new String[len];

        for (int i=0; i<listaFicha.size(); i++) {
            bio = listaFicha.get(i);
            nInfoId[i] = bio.getID();
            nInfo1[i] = bio.getNOMBRES();
            nInfo2[i] = bio.getNUM_DOCUMENTO();
            nInfo3[i] = bio.getFEC_CREACION();
        }
        btnAgregarFicha = findViewById(R.id.btnAgregarFicha);
        lvFicha = findViewById(R.id.lvFicha);
        MyAdapter adapter = new MyAdapter(this, nInfoId, nInfo1, nInfo2, nInfo3);
        lvFicha.setAdapter(adapter);

    }

    class MyAdapter extends ArrayAdapter<String>{
     Context context;

    String nInfoId[];
    String nInfo1[];
    String nInfo2[];
    String nInfo3[];

     MyAdapter(Context c, String nInfoId[], String nInfo1[], String nInfo2[], String nInfo3[]){
         super(c,R.layout.row, R.id.txtInfoFichaId, nInfoId);
         this.context = c;
         this.nInfoId = nInfoId;
         this.nInfo1 = nInfo1;
         this.nInfo2 = nInfo2;
         this.nInfo3 = nInfo3;
     }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView id = row.findViewById(R.id.txtInfoFichaId);
            TextView ficha = row.findViewById(R.id.txtInfoFicha1);
            TextView empleado = row.findViewById(R.id.txtInfoFicha2);
            TextView hora = row.findViewById(R.id.txtInfoFicha3);

            id.setText(nInfoId[position]);
            ficha.setText(nInfo1[position]);
            empleado.setText(nInfo2[position]);
            hora.setText(nInfo3[position]);

            return row;
        }
    }
}
